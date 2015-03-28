package cmd.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import cmd.CmdEvent;
import cmd.CmdMutex;

import java.util.List;

/**
 */
public interface CmdSQL {

    //////////////////////////////
    // Cmd
    //////////////////////////////

    @SqlQuery("select * from CmdRec where cmdID = :cmdID")
    @Mapper(CmdRowMapper.class)
    public CmdRow getCmdRow(@Bind("cmdID") String cmdID);

    @SqlQuery("select nextID from Cmd.id")
    public String nextCmdID();

    @SqlUpdate ("insert into CmdRec (cmdID, cmdType, cmdState, doc) values (:cmdID, :cmdType, :cmdState, :doc)")
    void insertCmd (@Bind("cmdID") String cmdID, @Bind("cmdType") String cmdType,
                    @Bind("cmdState") String cmdState, @Bind("doc") String doc);

    @SqlUpdate ("update CmdRec set cmdState=:cmdState, doc=:doc where cmdID=:cmdID")
    void updateCmd (@Bind("cmdID") String cmdID, @Bind("cmdState") String cmdState, @Bind("doc") String doc);

    @SqlUpdate ("delete from CmdRec where cmdID=:cmdID")
    void deleteCmd (@Bind("cmdID") String cmdID);

    //////////////////////////////
    // CmdEvent
    //////////////////////////////

    @SqlQuery("select * from CmdEvent where time < 'now'")
    @Mapper(CmdEventMapper.class)
    public List<CmdEvent> getDueEvents ();

    @SqlUpdate ("insert into CmdEvent (eventID, eventType, sourceCmdID, targetCmdID) values (:eventID, :eventType, :sourceCmdID, :targetCmdID)")
    void insertEvent (@Bind("eventID") String eventID, @Bind("eventType") String eventType, @Bind("eventState") String eventState,
                      @Bind("sourceCmdID") String sourceCmdID, @Bind("targetCmdID") String targetCmdID);

    @SqlUpdate ("insert into CmdEvent (eventID, eventType) values (:eventID, :eventType)")
    void insertEvent (@Bind("eventID") String eventID, @Bind("eventType") String eventType);

    //////////////////////////////
    // CmdTimer
    //////////////////////////////

    //////////////////////////////
    // CmdMutex
    //////////////////////////////

    @SqlUpdate("delete from CmdMutex where timeLeaseExpires < 'now'")
    public void deleteAnyAbandonedMutexes();

    @SqlUpdate("delete from CmdMutex where processID=:processID")
    public void deleteProcessMutexes(String processID);

    @SqlUpdate("update CmdMutex set foo = 'now' where processID = :processID")
    public void refreshProcessMutexes (String processID);

    @SqlUpdate("insert CmdMutex set foo = 'now', processID=:processID, mutexID=:mutexID, type=:type")
    public boolean acquireMutex (String processID, String mutexID, CmdMutex.Type type);

    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    public void close();
}
