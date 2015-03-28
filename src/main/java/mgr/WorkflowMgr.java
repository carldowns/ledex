package mgr;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.LoggerFactory;
import system.Cmd;
import system.CmdEvent;
import system.CmdMutex;
import system.CmdHandler;
import system.dao.CmdRow;
import system.dao.CmdSQL;

import java.util.List;
import java.util.concurrent.*;

/**
 * This class manages the command workflow patterns established within the system.
 * It's purpose is to efficiently manage command / event / timer CRUD as well as
 * workflow state transitions.
 */

@Singleton
public class WorkflowMgr implements Managed {

    private static final Logger _log = (Logger) LoggerFactory.getLogger(WorkflowMgr.class);
    private static int corePoolSize = 2;
    private static int _mutexRefreshCycle = 1000;
    private static final ObjectMapper mapper = new ObjectMapper(); // FIXME should be a shared resource

    /////////////////////////
    // Caching
    /////////////////////////

    // the system reads periodically from data store to retrieve 'due' events
    // due events are cached in an expiring cache to track that they have been encountered thus
    // avoiding unnecessary reprocessing.

    final Cache<String, CmdEvent> _dueEventCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    // FIFO queue for passing events to the worker threads

    final ConcurrentLinkedQueue<CmdEvent> _dueEventQueue = new ConcurrentLinkedQueue<>();

    // mutexes acquired

    final ConcurrentHashMap<String, CmdMutex> _acquiredMutexes = new ConcurrentHashMap<>();

    // worker pool

    final ExecutorService _threadPool = new ScheduledThreadPoolExecutor(corePoolSize);

    // handler list

    final ConcurrentMap<String, CmdHandler> _handlers = Maps.newConcurrentMap();
    //private static SortedMap<Integer,QuoteHandlerInterface> interpreters = new TreeMap<>();

    // runtime identity of this process needed for mutex negotiation
    // process ID which must be random and unique for this run cycle.
    // verify that the process ID does not already exist in the table
    // by adding a mutex for itself, the process guarantees that its ID is unique

    final String _processID;

    // data access
    final CmdSQL _sql;

    /////////////////////////
    //
    /////////////////////////

    @Inject
    public WorkflowMgr (CmdSQL sql, String processID) {
        _sql = Preconditions.checkNotNull(sql, "CmdSQL");
        _processID = Preconditions.checkNotNull(processID, "processID");
    }

    /////////////////////////
    // Managed
    /////////////////////////

    @Override
    public void start() throws Exception {
        _threadPool.submit(pollEvents); // TODO fix this up so we have N of each type of runnable on the go
        _threadPool.submit(processEvents);
        _threadPool.submit(refreshMutexes);
    }

    @Override
    public void stop() throws Exception {
        _threadPool.shutdown();
    }

    /////////////////////////
    // Runnable
    /////////////////////////

    Runnable pollEvents = new Runnable () {
        public void run () {
            try {
                pollForDueEvents();
            }
            catch (Exception e) {
                _log.error("Unable to poll due events", e);
            }
        }
    };

    Runnable processEvents = new Runnable () {
        public void run () {
            try {
                processDueEvents();
            }
            catch (Exception e) {
                _log.error("Unable to process due events", e);
            }
        }
    };

    Runnable refreshMutexes = new Runnable () {
        public void run () {
            try {
                refreshMutexes();
            }
            catch (Exception e) {
                _log.error("Unable to refresh process mutexes", e);
            }
        }
    };

    //////////////////////
    // Handler Interface
    //////////////////////

    public CmdHandler<?> getCmdHandler (CmdRow cmdRecord) {
        CmdHandler<?> handler = _handlers.get(cmdRecord.getCmdType());
        Preconditions.checkNotNull(handler, "Handler not found for type", cmdRecord.getCmdType());
        return handler;
    }

    @Inject
    public void addHandler(CmdHandler handler) {
        _handlers.put(handler.getCmdType(), handler);
    }

    /////////////////////////
    // Cmd Interface
    /////////////////////////

    public <C extends Cmd> C getCmd (String cmdID) {
        CmdRow row = _sql.getCmdRow(cmdID);
        Preconditions.checkNotNull(row, "unable to retrieve cmd for cmdID {}", cmdID);
        return getCmdHandler(row).convert(row);
    }

    public void saveCmd (Cmd cmd) {
        try {
            if (!cmd.hasAssignedID()) {
                //cmd.setID(_sql.nextCmdID()); // FIXME
                cmd.setID("TEST." + ThreadLocalRandom.current().nextLong()); // FIXME
                String json = mapper.writeValueAsString(cmd);
                _sql.insertCmd(cmd.getID(), cmd.getType(), cmd.getState().name(), json);
            }
            else {
                String json = mapper.writeValueAsString(cmd);
                _sql.updateCmd(cmd.getID(), cmd.getState().name(), json);
            }
        }
        catch (Exception e) {
            _log.error("unable to save cmd {}", cmd, e);
        }
    }

    /////////////////////////
    // Event Interface
    /////////////////////////

    public void saveEvent (CmdEvent event) {
        Preconditions.checkArgument(!event.hasAssignedID(), "immutable event already persisted {}", event);
        _sql.insertEvent(event.getEventID(), event.getEventType());
    }

    public List<CmdEvent> getEventsForCmd (Cmd cmd) {
        return ImmutableList.of(); // FIXME
    }

    public Cmd getCmdForEvent (CmdEvent event) {
        return null; // FIXME
    }

    /////////////////////////
    // Event Helpers
    /////////////////////////

    private void pollForDueEvents () {
        // take out any expired / abandoned mutexes
        _sql.deleteAnyAbandonedMutexes();

        // poll event store for events that are due (AND do not have a mutex (table join))
        for (CmdEvent event : _sql.getDueEvents()) {

            // if the cache has the event ignore it
            if (_dueEventCache.asMap().containsKey(event.getEventID())) {
                continue;
            }
            queueEvent(event);
        }
    }

    private void queueEvent(CmdEvent event) {
        // if the queue has room, queue it and map it else ignore it
        // the queue implementation we have used is unbounded so this will always return true
        if (_dueEventQueue.offer(event)) {
            _dueEventCache.put(event.getEventID(), event);
        }
    }

    private void processDueEvents () {
        // get next event from the event queue.
        CmdEvent event = _dueEventQueue.poll();
        if (event == null) {
            return;
        }
        acceptEvent(event);
    }

    private void acceptEvent(CmdEvent event) {
        CmdMutex mutex = null;
        try {
            // acquire a mutex lock for the event or exit
            mutex = acquireMutex(event);
            if (mutex == null) return;

            CmdRow cmdRecord = _sql.getCmdRow(event.getTargetCmdID());
            acceptCmd(event, cmdRecord);
        }
        catch (Exception e) {
            _log.error("problems accepting event", e);
        }
        finally {
            // discard event mutex
            releaseMutex(mutex);
        }
    }

    /////////////////////////
    // Cmd Helpers
    /////////////////////////

    private void acceptCmd (CmdEvent event, CmdRow cmdRecord) {

        CmdMutex mutex = null;

        try {
            // acquire a mutex lock for the command (if applicable) or exit
            mutex = acquireMutex(cmdRecord);
            if (mutex == null) return;

            // at this point we have the event and command mutexes
            execCmd(event, cmdRecord);
        }

        catch (Exception e) {
            _log.error("problems accepting cmd", e);
        }

        finally {
            // discard command mutex
            releaseMutex(mutex);
        }
    }

    private void execCmd (CmdEvent event, CmdRow cmdRecord) {

        // handler will deserialize command implementation or construct new command if event does not reference one

        CmdHandler<?> handler = getCmdHandler(cmdRecord);
        handler.process(cmdRecord, event);

        // mutex validity is maintained by the process through refreshing
        // command completes, possibly creating new events along the way

        // save command state and payload
        // save event state show as completed if normal completion
        // save event state show as error if exceptional exit
    }

    ////////////////////////////
    // Mutex Helpers
    ////////////////////////////

    private void refreshMutexes () {

        try {
            // repeatedly update TTL for any mutexes contained in the mutex map owned by this process
            // if one fails, free it from the map.

            _sql.refreshProcessMutexes(_processID);
            Thread.sleep(_mutexRefreshCycle);
        }
        catch (Exception e) {
            _log.error("unable to refresh all mutexes for process", e);
        }
    }

    private CmdMutex acquireMutex (CmdEvent event) {
        return acquireMutex (event.getEventID(), CmdMutex.Type.event);
    }

    private CmdMutex acquireMutex (CmdRow cmdRec) {
        return acquireMutex (cmdRec.toString(), CmdMutex.Type.cmd);
    }

    private CmdMutex acquireMutex (String mutexID, CmdMutex.Type type) {

        // attempt to add row to the mutex table; if not there insert it
        // if already there and TTL expired, ok grab it; else fail

        if (!_sql.acquireMutex(_processID, mutexID, type)) {
            return null;
        }

        CmdMutex mutex = new CmdMutex(_processID, mutexID, type);
        _acquiredMutexes.put(mutexID, mutex); // FIXME
        return mutex;
    }

    private void releaseMutex (CmdMutex mutex) {

        // attempt to delete the row in the mutex table
        // if not there error
    }

    private boolean isMutexValid (CmdMutex mutex) {
        return true;
    }

}
