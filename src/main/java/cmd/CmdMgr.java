package cmd;

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
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.slf4j.LoggerFactory;
import cmd.dao.CmdEventRec;
import cmd.dao.CmdMutexRec;
import cmd.dao.CmdRec;
import cmd.dao.CmdSQL;
import util.CmdRuntimeException;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    AtomicInteger _counter = new AtomicInteger(100);

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
        // TODO: need a way to assert that the processID is in fact unique in the datastore.
        // TODO: create a mutex for the process ID?
    }

    /////////////////////////
    // Managed
    /////////////////////////

    @Override
    public void start() throws Exception {
        _threadPool.scheduleAtFixedRate(pollEvents, 0, 1, TimeUnit.SECONDS);
        _threadPool.scheduleAtFixedRate(refreshMutexes, 0, 5, TimeUnit.SECONDS);
        _threadPool.scheduleAtFixedRate(clearExpiredMutexes, 0, 15, TimeUnit.MINUTES);
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
                pollForDueEvents();
            }
            catch (Exception e) {
                _log.error("Unable to poll due events", e);
            }
        }
    };

    // periodically update TTL for any mutexes contained in the mutex map owned by this process
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

    // periodically remove any expired mutexes (any process)

    Runnable clearExpiredMutexes = new Runnable () {
        public void run () {
            try {
                _log.info("clearing expired mutex leases...");
                _dao.deleteAllExpiredMutexes();
            }
            catch (Exception e) {
                _log.error("Unable to remove expired mutexes", e);
            }
        }
    };

    //////////////////////////
    // Cmd Handler Interface
    //////////////////////////

    public CmdHandler<?> getCmdHandler (String cmdType) {
        CmdHandler<?> handler = _handlers.get(cmdType);
        Preconditions.checkNotNull(handler, "Handler not found for type %s", cmdType);
        return handler;
    }

    public CmdHandler<?> getCmdHandler (Class cmdType) {
        CmdHandler<?> handler = _handlers.get(cmdType.getSimpleName());
        Preconditions.checkNotNull(handler, "Handler not found for type %s", cmdType);
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
        CmdRec cmdRecord = getCmdRecord(cmdID);
        Preconditions.checkNotNull(cmdRecord, "unable to retrieve cmdRecord for cmdID %s", cmdID);
        return getCmdHandler(cmdRecord.getCmdType()).convert(cmdRecord);
    }

    public CmdRec getCmdRecord (String cmdID) {
        return _dao.getCmd(cmdID);
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
            _dao.updateCmd(cmd.getID(), cmd.getType(), cmd.getState().name(), json);
        }
        catch (Exception e) {
            _log.error("unable to update cmd", e);
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
                event.getCmdSourceID(),
                event.getCmdSourceType(),
                event.getCmdTargetID(),
                event.getCmdTargetType()
            );
        }
        catch (Exception e) {
            _log.error("unable to save event", e);
        }
    }

    public void updateEvent (CmdEventRec event) {
        try {
            _dao.updateEvent(
                    event.getEventID(),
                    event.getEventType(),
                    event.getEventState().toString(),
                    event.getCmdSourceID(),
                    event.getCmdSourceType(),
                    event.getCmdTargetID(),
                    event.getCmdTargetType());
        }
        catch (Exception e) {
            _log.error("updateEvent", e);
            throw new CmdRuntimeException(String.format("unable to update event %s", event));
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

    public CmdRec getSourceCmdForEvent (CmdEventRec event) {
        return getCmdRecord(event.getCmdSourceID());
    }

    public CmdRec getTargetCmdForEvent (CmdEventRec event) {
        return getCmdRecord(event.getCmdTargetID());
    }


    /////////////////////////
    // Event Helpers
    /////////////////////////

    private void pollForDueEvents() {
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
                        event.validate();
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

    //@Transaction(TransactionIsolationLevel.SERIALIZABLE)
    //@Transaction
    private void acceptEvent(CmdEventRec eventRecord) {
        CmdMutexRec eventMutex = null;
        try {
            // acquire a mutex lock for the event or exit
            eventMutex = acquireMutex(eventRecord);
            if (eventMutex == null) {
                _log.warn("unable to acquire mutex for event %s", eventRecord);
                return;
            }

            // events with a target cmd: retrieve it
            // events without a target cmd: pass null forward
            CmdRec cmdRecord =
                    eventRecord.getCmdTargetID() != null ?
                    _dao.getCmd(eventRecord.getCmdTargetID()) :
                    null;

            acceptCmd(eventRecord, cmdRecord);
        }
        catch (Exception e) {
            _log.error("problems accepting event", e);
            throw new CmdRuntimeException(e.getMessage());
        }
        finally {
            // discard event mutex
            releaseMutex(eventMutex);
        }
    }

    /////////////////////////
    // Cmd Helpers
    /////////////////////////

    private void acceptCmd (CmdEventRec eventRecord, @Nullable CmdRec cmdRecord) {
        CmdMutexRec cmdMutex = null;
        try {
            // if we have a Cmd record, acquire a mutex lock for it or exit
            if (cmdRecord != null) {
                cmdMutex = acquireMutex(cmdRecord);
                if (cmdMutex == null) {
                    _log.warn("unable to acquire mutex for cmd %s", cmdRecord);
                    return;
                }
            }

            // at this point we have the event and command mutexes
            execCmd(eventRecord, cmdRecord);
        }
        catch (Exception e) {
            _log.error("problems accepting cmd", e);
        }
        finally {
            // discard command mutex
            releaseMutex(cmdMutex);
        }
    }

    private void execCmd (CmdEventRec eventRecord, @Nullable CmdRec cmdRecord) {

        // handler will deserialize its command implementation
        // or construct new command if event does not reference one

        try {
            CmdHandler<?> handler = getCmdHandler(eventRecord.getCmdTargetType());

            if (cmdRecord != null)
                handler.process(this, cmdRecord, eventRecord);
            else
                handler.process(this, eventRecord);

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
            _dao.refreshMutexes(_processID);
        }
        catch (Exception e) {
            _log.error("unable to refresh all mutexes for process", e);
        }
    }

    private CmdMutexRec acquireMutex (CmdEventRec eventRec) {
        return acquireMutex (eventRec.getEventID(), CmdMutexRec.Type.event);
    }

    private CmdMutexRec acquireMutex (CmdRec cmdRec) {
        return acquireMutex (cmdRec.getCmdID(), CmdMutexRec.Type.cmd);
    }

    private CmdMutexRec acquireMutex (String mutexID, CmdMutexRec.Type type) {

        try {
            // attempt to insert a row to the mutex table; if there already this will fail
            // if already there and TTL expired, the sweeper thread will clear stale mutexes out eventually
            // NOTE: mutexID + type must point to a specific Cmd or Event.
            _dao.insertMutex(_processID, mutexID, type.name());
        }
        catch (UnableToExecuteStatementException e) {
            _log.info("mutex busy %s ", mutexID);
            return null;
        }

        CmdMutexRec mutex = new CmdMutexRec(_processID, mutexID, type);
        _acquiredMutexes.put(mutexID, mutex);
        return mutex;
    }

    private void releaseMutex (CmdMutexRec mutex) {
        if (mutex == null) {
            return;
        }

        if (_dao.deleteMutex(_processID, mutex.getMutexID(), mutex.getType().name()) != 1) {
            _log.warn("Mutex was not properly released! %s", mutex);
        }

        _acquiredMutexes.remove(mutex.getMutexID());
    }

    private boolean isMutexValid (CmdMutexRec mutex) {
        return _dao.selectMutex(_processID, mutex.getMutexID(), mutex.getType().name()) != null;
    }

    /////////////////
    // ID Interface
    /////////////////

    public String newID() {
        return _processID + "." + _counter.addAndGet(1);
    }


}
