package cmd.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

/**
 */
public interface CmdSQL {

    //////////////////////////////
    // CmdRec
    //////////////////////////////

    @SqlQuery("select * from CmdRec where cmdID = :cmdID")
    @Mapper(CmdRecMapper.class)
    CmdRec getCmd(@Bind("cmdID") String cmdID);

    @SqlUpdate ("insert into CmdRec (cmdID, cmdType, cmdState, doc) values (:cmdID, :cmdType, :cmdState, :doc)")
    void insertCmd (@Bind("cmdID") String cmdID,
                    @Bind("cmdType") String cmdType,
                    @Bind("cmdState") String cmdState,
                    @Bind("doc") String doc);

    @SqlUpdate ("update CmdRec set cmdState=:cmdState, cmdType=:cmdType, doc=:doc where cmdID=:cmdID")
    void updateCmd (@Bind("cmdID") String cmdID,
                    @Bind("cmdType") String cmdType,
                    @Bind("cmdState") String cmdState,
                    @Bind("doc") String doc);

    @SqlUpdate ("delete from CmdRec where cmdID=:cmdID")
    void deleteCmd (@Bind("cmdID") String cmdID);

    //////////////////////////////
    // CmdEventRec
    //////////////////////////////

    @SqlUpdate ("insert into CmdEventRec (eventID, eventType, eventState, cmdSourceID, cmdTargetID) " +
                "values (:eventID, :eventType, :eventState, :cmdSourceID, :cmdTargetID)")
    void insertEvent (@Bind("eventID") String eventID,
                      @Bind("eventType") String eventType,
                      @Bind("eventState") String eventState,
                      @Bind("cmdSourceID") String cmdSourceID,
                      @Bind("cmdTargetID") String cmdTargetID);

    @SqlUpdate ("update CmdEventRec set eventState=:eventState, cmdSourceID=:cmdSourceID, cmdTargetID=:cmdTargetID " +
                "where eventID=:eventID")
    void updateEvent (@Bind("eventID") String eventID,
                      @Bind("eventState") String eventState,
                      @Bind("cmdSourceID") String cmdSourceID,
                      @Bind("cmdTargetID") String cmdTargetID);

//    @SqlUpdate ("insert into CmdEventRec (eventID, eventType) values (:eventID, :eventType)")
//    void insertEvent (@Bind("eventID") String eventID, @Bind("eventType") String eventType);

    @SqlQuery("select * from CmdEventRec where eventID=:eventID")
    @Mapper(CmdEventRecMapper.class)
    CmdEventRec getEvent (@Bind("eventID") String eventID);

    @SqlQuery("select * from CmdEventRec where eventDue < 'now' and eventState='pending'")
    @Mapper(CmdEventRecMapper.class)
    List<CmdEventRec> getDueEvents ();

    @SqlUpdate ("delete from CmdEventRec where eventID=:eventID")
    void deleteEvent (@Bind("eventID") String eventID);

    //////////////////////////////
    // CmdTimerRec
    //////////////////////////////

    //////////////////////////////
    // CmdMutexRec
    //////////////////////////////

    @SqlUpdate("delete from CmdMutexRec where expireTs < 'now'")
    void deleteAnyAbandonedMutexes();

    @SqlUpdate("delete from CmdMutexRec where mutexOwner=:processID")
    void deleteProcessMutexes(String processID);

    @SqlUpdate("update CmdMutexRec set expireTs = (now() + '00:01:00'::interval) where mutexOwner=:processID")
    void refreshMutexes(String processID);

    @SqlUpdate("insert CmdMutexRec set expireTs = (now() + '00:01:00'::interval), mutexOwner=:processID, mutexID=:mutexID, mutexType=:type")
    boolean acquireMutex (String processID, String mutexID, CmdMutexRec.Type type);

    @SqlUpdate("delete from CmdMutexRec where mutexOwner=:processID and mutexID = mutexID")
    void deleteMutex(String processID, CmdMutexRec mutex);

    @SqlUpdate("select from CmdMutexRec where mutexOwner=:processID and mutexID = mutexID")
    @Mapper(CmdMutexRecMapper.class)
    CmdMutexRec selectMutex(String processID, CmdMutexRec mutex);

    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    void close();
}
