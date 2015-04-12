package cmd;

import cmd.dao.CmdMutexRec;
import cmd.dao.CmdRec;
import cmd.dao.CmdSQL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.lifecycle.Managed;
import org.joda.time.Instant;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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

    final ConcurrentMap<String, ICmdHandler> _handlers = Maps.newConcurrentMap();

    /////////////////////////
    // Caching
    /////////////////////////

    // the system reads periodically from data store to retrieve 'due' commands
    // due commands are cached in an expiring cache to track that they have been evaluated
    // thus avoiding unnecessary repetitive processing.  As a later optimization, the keys
    // of this cache can be used as an exclusion list when retrieving from the store.

    final Cache<String, CmdRec> _dueCmdCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
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
        _threadPool.scheduleAtFixedRate(pollDueCmds, 0, 1, TimeUnit.SECONDS);
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

    Runnable pollDueCmds = new Runnable () {
        public void run () {
            try {
                _log.info("polling for due cmds...");
                pollForDueCmds();
            }
            catch (Exception e) {
                _log.error("Unable to poll due cmds", e);
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

    public ICmdHandler<?> getCmdHandler (String cmdType) {
        ICmdHandler<?> handler = _handlers.get(cmdType);
        Preconditions.checkNotNull(handler, "Handler not found for type %s", cmdType);
        return handler;
    }

    @Inject
    public void registerHandlers(Set<ICmdHandler> handlers) {
        for (ICmdHandler handler : handlers) {
            registerHandler (handler);
        }
    }

    @VisibleForTesting
    public void registerHandler(ICmdHandler handler) {
        _log.info("registered cmd handler for type " + handler.getCmdType());
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

    // FIXME this time operator needs thought - time units, etc
    public void scheduleCmd (Cmd cmd, Long millisAhead) {
        try {
            String json = mapper.writeValueAsString(cmd);
            _dao.scheduleCmd(cmd.getID(), cmd.getType(), cmd.getState().name(), json,
                    new Timestamp(Instant.now().getMillis() + millisAhead));
        }
        catch (Exception e) {
            _log.error("unable to schedule cmd", e);
        }
    }

    ////////////////////////
    // Cmd Threading
    ////////////////////////

    private void pollForDueCmds() {
        // poll for cmds that are due (AND do not have a mutex (table join))
        for (CmdRec cmd : _dao.getDueCmds()) {

            // if the cache has the event ignore it
            if (_dueCmdCache.asMap().containsKey(cmd.getCmdID())) {
                continue;
            }
            queueCmd(cmd);
        }
    }

    private void queueCmd(final CmdRec cmd) {
        try {
            _threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        acceptCmd(cmd);
                    } catch (Exception e) {
                        _log.error("Unable to accept cmd", e);
                    }
                }
            });
        }
        catch (Exception e) {
            _log.error("Unable to queue cmd", e);
        }
    }

    /////////////////////////
    // Cmd Helpers
    /////////////////////////

    //@Transaction(TransactionIsolationLevel.SERIALIZABLE)
    //@Transaction
    private void acceptCmd (CmdRec cmdRecord) {
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
            execCmd(cmdRecord);
        }
        catch (Exception e) {
            _log.error("problems accepting cmd", e);
        }
        finally {
            // discard command mutex
            releaseMutex(cmdMutex);
        }
    }

    private void execCmd (CmdRec cmdRecord) {
        try {
            // handler will deserialize its command implementation
            ICmdHandler<?> handler = getCmdHandler(cmdRecord.getCmdType());
            handler.process(this, cmdRecord);
        }
        catch (Exception e) {
            _log.error("unable to execute cmd", e);
            _dao.updateCmdState(cmdRecord.getCmdID(), CmdState.failed.name());
        }
        finally {
            removeCmdFromCache(cmdRecord.getCmdID());
        }
    }

    private void removeCmdFromCache (String cmdID) {
        _dueCmdCache.asMap().remove(cmdID);
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


    private CmdMutexRec acquireMutex (CmdRec cmdRecord) {
        String mutexID = cmdRecord.getCmdID();
        String type = cmdRecord.getCmdType();

        try {
            // attempt to insert a row to the mutex table; if there already this will fail
            // if already there and TTL expired, the sweeper thread will clear stale mutexes out eventually
            // NOTE: mutexID + type must point to a specific Cmd or Event.
            _dao.insertMutex(_processID, mutexID, type);
        }
        catch (UnableToExecuteStatementException e) {
            _log.info("mutex busy %s ", cmdRecord.getCmdID());
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

        if (_dao.deleteMutex(_processID, mutex.getMutexID(), mutex.getType()) != 1) {
            _log.warn("Mutex was not properly released! %s", mutex);
        }

        _acquiredMutexes.remove(mutex.getMutexID());
    }

    private boolean isMutexValid (CmdMutexRec mutex) {
        return _dao.selectMutex(_processID, mutex.getMutexID(), mutex.getType()) != null;
    }

    /////////////////
    // ID Interface
    /////////////////

    public String newID() {
        return _processID + "." + _counter.addAndGet(1);
    }
}
