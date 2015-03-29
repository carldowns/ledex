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
import cmd.Cmd;
import cmd.dao.CmdEventRec;
import cmd.dao.CmdMutexRec;
import cmd.CmdHandler;
import cmd.dao.CmdRec;
import cmd.dao.CmdSQL;

import java.util.List;
import java.util.concurrent.*;

/**
 * This class manages the command workflow patterns established within the system.
 * It's purpose is to efficiently manage command / event / timer CRUD as well as
 * workflow state transitions.
 */

@Singleton
public class CmdMgr implements Managed {

    private static final Logger _log = (Logger) LoggerFactory.getLogger(CmdMgr.class);
    private static final ObjectMapper mapper = new ObjectMapper(); // FIXME should be a shared resource

    /////////////////////////
    // Concurrency Control
    /////////////////////////

    // runtime identity of this process needed for mutex negotiation
    // process ID which must be random and unique for this run cycle.
    // verify that the process ID does not already exist in the table
    // by adding a mutex for itself, the process guarantees that its ID is unique

    final String _processID;

    // mutexes are acquired for Cmd and Event objects before they can be processed.
    // This is list is maintained purely for periodic refresh of the lease period.

    final ConcurrentHashMap<String, CmdMutexRec> _acquiredMutexes = new ConcurrentHashMap<>();

    /////////////////////////
    // Handlers
    /////////////////////////

    // handlers are registered for each type of Cmd.
    // events must have the Cmd type to get processed

    final ConcurrentMap<String, CmdHandler> _handlers = Maps.newConcurrentMap();

    /////////////////////////
    // Caching
    /////////////////////////

    // the system reads periodically from data store to retrieve 'due' events
    // due events are cached in an expiring cache to track that they have been evaluated
    // thus avoiding unnecessary repetitive processing.  As a later optimization, the keys
    // of this cache can be used as an exclusion list when retrieving from the store.

    final Cache<String, CmdEventRec> _dueEventCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    /////////////////////////
    // Threading
    /////////////////////////

    final ScheduledThreadPoolExecutor _threadPool = new ScheduledThreadPoolExecutor(5);

    /////////////////////////
    // DAO
    /////////////////////////

    final CmdSQL _dao;

    /////////////////////////
    // Constructors
    /////////////////////////

    @Inject
    public CmdMgr(CmdSQL sql, String processID) {
        _dao = Preconditions.checkNotNull(sql, "CmdSQL");
        _processID = Preconditions.checkNotNull(processID, "processID");
    }

    /////////////////////////
    // Managed
    /////////////////////////

    @Override
    public void start() throws Exception {
        _threadPool.scheduleAtFixedRate(pollEvents, 0, 1, TimeUnit.SECONDS);
        _threadPool.scheduleAtFixedRate(refreshMutexes, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws Exception {
        _threadPool.shutdown();
    }

    /////////////////////////
    // Periodic Actions
    /////////////////////////

    Runnable pollEvents = new Runnable () {
        public void run () {
            try {
                _log.info("polling for events...");
                pollStoreForDueEvents();
            }
            catch (Exception e) {
                _log.error("Unable to poll due events", e);
            }
        }
    };

    // repeatedly update TTL for any mutexes contained in the mutex map owned by this process
    // if one fails, free it from the map.

    Runnable refreshMutexes = new Runnable () {
        public void run () {
            try {
                _log.info("refreshing mutex leases...");
                refreshMutexes();
            }
            catch (Exception e) {
                _log.error("Unable to refresh mutexes", e);
            }
        }
    };

    //////////////////////////
    // Cmd Handler Interface
    //////////////////////////

    public CmdHandler<?> getCmdHandler (CmdRec cmdRecord) {
        CmdHandler<?> handler = _handlers.get(cmdRecord.getCmdType());
        Preconditions.checkNotNull(handler, "Handler not found for type %s", cmdRecord.getCmdType());
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
        CmdRec row = _dao.getCmd(cmdID);
        Preconditions.checkNotNull(row, "unable to retrieve cmd for cmdID %s", cmdID);
        return getCmdHandler(row).convert(row);
    }

    public void createCmd (Cmd cmd) {
        try {
            String json = mapper.writeValueAsString(cmd);
            _dao.insertCmd(cmd.getID(), cmd.getType(), cmd.getState().name(), json);
        }
        catch (Exception e) {
            _log.error("unable to create cmd %s %s", cmd, e);
        }
    }

    public void updateCmd (Cmd cmd) {
        try {
            String json = mapper.writeValueAsString(cmd);
            _dao.updateCmd(cmd.getID(), cmd.getState().name(), json);
        }
        catch (Exception e) {
            _log.error("unable to update cmd %s %s", cmd, e);
        }
    }

    /////////////////////////
    // Event Interface
    /////////////////////////

    public void createEvent (CmdEventRec event) {
        try {
            _dao.insertEvent(
                event.getEventID(),
                event.getEventType(),
                event.getEventState().toString(),
                event.getSourceCmdID(),
                event.getTargetCmdID());
        }
        catch (Exception e) {
            _log.error("unable to save event %s %s", event, e);
        }
    }

    public void updateEvent (CmdEventRec event) {
        try {
                _dao.updateEvent(
                    event.getEventID(),
                    event.getEventState().toString(),
                    event.getSourceCmdID(),
                    event.getTargetCmdID());
        }
        catch (Exception e) {
            _log.error("unable to save event %s %s", event, e);
        }
    }

    public CmdEventRec getEvent (String eventID) {
        Preconditions.checkNotNull(eventID, "eventID %s", eventID);
        return _dao.getEvent(eventID);
    }

    public List<CmdEventRec> getEventsForSourceCmd (Cmd cmd) {
        return ImmutableList.of(); // FIXME
    }

    public List<CmdEventRec> getEventsForTargetCmd (Cmd cmd) {
        return ImmutableList.of(); // FIXME
    }

    public Cmd getSourceCmdForEvent (CmdEventRec event) {
        return getCmd(event.getSourceCmdID());
    }

    public Cmd getTargetCmdForEvent (CmdEventRec event) {
        return getCmd(event.getTargetCmdID());
    }


    /////////////////////////
    // Event Helpers
    /////////////////////////

    private void pollStoreForDueEvents() {
        // take out any expired / abandoned mutexes
        _dao.deleteAnyAbandonedMutexes();

        // poll event store for events that are due (AND do not have a mutex (table join))
        for (CmdEventRec event : _dao.getDueEvents()) {

            // if the cache has the event ignore it
            if (_dueEventCache.asMap().containsKey(event.getEventID())) {
                continue;
            }
            queueEvent(event);
        }
    }

    private void queueEvent(final CmdEventRec event) {
        try {
            _threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        acceptEvent(event);
                    }
                    catch (Exception e) {
                        _log.error("Unable to accept event", e);
                    }
                }
            });
        }
        catch (Exception e) {
            _log.error("Unable to queue event", e);
        }
    }

//    private void queueEvent2(CmdEvent event) {
//        try {
//            _threadPool.execute(new EventWorker(event));
//        }
//        catch (RejectedExecutionException ree) {
//            _log.error("unable to add event to thread pool queue", ree);
//        }
//    }
//
//    private class EventWorker implements Runnable {
//        CmdEvent _event;
//
//        EventWorker (CmdEvent event) {
//            _event = event;
//        }
//
//        @Override
//        public void run() {
//            acceptEvent(_event);
//        }
//    }

//    private void queueEvent(CmdEvent event) {
//        // if the queue has room, queue it and map it else ignore it
//        // the queue implementation we have used is unbounded so this will always return true
//        if (_dueEventQueue.offer(event)) {
//            _dueEventCache.put(event.getEventID(), event);
//        }
//    }

//    private void processDueEvents () {
//        // get next event from the event queue.
//        CmdEvent event = _dueEventQueue.poll();
//        if (event == null) {
//            return;
//        }
//        acceptEvent(event);
//    }

    private void acceptEvent(CmdEventRec event) {
        CmdMutexRec mutex = null;
        try {
            // acquire a mutex lock for the event or exit
            mutex = acquireMutex(event);
            if (mutex == null) return;

            CmdRec cmdRecord = _dao.getCmd(event.getTargetCmdID());
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

    private void acceptCmd (CmdEventRec event, CmdRec cmdRecord) {
        CmdMutexRec mutex = null;
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

    private void execCmd (CmdEventRec event, CmdRec cmdRecord) {

        // handler will deserialize command implementation or construct new command if event does not reference one

        try {
            CmdHandler<?> handler = getCmdHandler(cmdRecord);
            handler.process(cmdRecord, event);

            // save command state and payload
            // save event state show as completed if normal completion
            // save event state show as error if exceptional exit
        }
        catch (Exception e) {
            _log.error("unable to execute event for cmd", e);
        }
    }

    ////////////////////////////
    // Mutex Helpers
    ////////////////////////////

    /**
     * mutex validity is maintained by the process through refreshing
     * command completes, possibly creating new events along the way
     */
    private void refreshMutexes () {

        try {
            _dao.refreshProcessMutexes(_processID);
        }
        catch (Exception e) {
            _log.error("unable to refresh all mutexes for process", e);
        }
    }

    private CmdMutexRec acquireMutex (CmdEventRec event) {
        return acquireMutex (event.getEventID(), CmdMutexRec.Type.event);
    }

    private CmdMutexRec acquireMutex (CmdRec cmdRec) {
        return acquireMutex (cmdRec.toString(), CmdMutexRec.Type.cmd);
    }

    private CmdMutexRec acquireMutex (String mutexID, CmdMutexRec.Type type) {
        // attempt to add row to the mutex table; if not there insert it
        // if already there and TTL expired, ok grab it; else fail

        if (!_dao.acquireMutex(_processID, mutexID, type)) {
            return null;
        }

        CmdMutexRec mutex = new CmdMutexRec(_processID, mutexID, type);
        _acquiredMutexes.put(mutexID, mutex);
        return mutex;
    }

    private void releaseMutex (CmdMutexRec mutex) {
        // attempt to delete the row in the mutex table
        // if not there error
        _dao.deleteMutex(_processID, mutex);
        _acquiredMutexes.remove(mutex);
    }

    private boolean isMutexValid (CmdMutexRec mutex) {
        return _dao.selectMutex(_processID, mutex) != null;
    }

}
