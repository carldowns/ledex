package cmd.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 */
public interface CmdSQL2 {

    //////////////////////////////
    // CmdRec
    //////////////////////////////

    @SqlQuery(
            "select * from CmdRec " +
                    "where cmdID = :cmdID")

    @Mapper(CmdRecMapper.class)
    CmdRec2 getCmd(@Bind("cmdID") String cmdID);

    @SqlUpdate(
            "insert into CmdRec " +
                    "(cmdID, cmdType, cmdState, doc) values " +
                    "(:cmdID, :cmdType, :cmdState, :doc)")

    void insertCmd(@Bind("cmdID") String cmdID,
                   @Bind("cmdType") String cmdType,
                   @Bind("cmdState") String cmdState,
                   @Bind("doc") String doc);

    @SqlUpdate(
            "update CmdRec set " +
                    "cmdState=:cmdState, " +
                    "cmdType=:cmdType, " +
                    "doc=:doc " +
                    "where cmdID=:cmdID")

    void updateCmd(@Bind("cmdID") String cmdID,
                   @Bind("cmdType") String cmdType,
                   @Bind("cmdState") String cmdState,
                   @Bind("doc") String doc);

    @SqlUpdate(
            "update CmdRec set " +
                    "cmdState=:cmdState, " +
                    "cmdType=:cmdType, " +
                    "doc=:doc, " +
                    "dueTs=:timestamp " +
                    "where cmdID=:cmdID")

    void scheduleCmd(@Bind("cmdID") String cmdID,
                   @Bind("cmdType") String cmdType,
                   @Bind("cmdState") String cmdState,
                   @Bind("doc") String doc,
                   @Bind("timestamp") Timestamp timestamp);

    @SqlUpdate(
            "delete from CmdRec " +
                    "where cmdID=:cmdID")
    void deleteCmd(@Bind("cmdID") String cmdID);


    @SqlQuery(
            "select * from CmdRec " +
                    "where dueTs < 'now' " +
                    "and cmdState in ('started', 'waiting', 'pending')")

    @Mapper(CmdRecMapper.class)
    List<CmdRec2> getDueCmds();

    @SqlUpdate(
            "update CmdRec set " +
                    "cmdState=:cmdState " +
                    "where cmdID=:cmdID")

    void updateCmdState(@Bind("cmdID") String cmdID,
                   @Bind("cmdState") String cmdState);

    //////////////////////////////
    // CmdMutexRec
    //////////////////////////////

    @SqlUpdate(
            "insert into CmdMutexRec " +
                    "(mutexOwner, mutexID, mutexType, expireTs) values " +
                    "(:processID, :mutexID, :mutexType, current_timestamp + interval '1 minute')")

    Integer insertMutex(@Bind("processID") String processID,
                        @Bind("mutexID") String mutexID,
                        @Bind("mutexType") String type);

    @SqlUpdate(
            "delete from CmdMutexRec " +
                    "where mutexOwner = :processID " +
                    "and mutexID = mutexID " +
                    "and mutexType = :mutexType")

    Integer deleteMutex(@Bind("processID") String processID,
                        @Bind("mutexID") String mutexID,
                        @Bind("mutexType") String type);

    @SqlUpdate(
            "select from CmdMutexRec " +
                    "where mutexOwner = :processID " +
                    "and mutexID = mutexID" +
                    "and mutexType = :mutexType" +
                    "and expireTs > 'current_timestamp")

    @Mapper(CmdMutexRecMapper.class)
    CmdMutexRec2 selectMutex(@Bind("processID") String processID,
                            @Bind("mutexID") String mutexID,
                            @Bind("mutexType") String type);

    @SqlUpdate(
            "update CmdMutexRec set " +
                    "expireTs = current_timestamp + interval '1 minute' " +
                    "where mutexOwner = :processID")

    Integer refreshMutexes(@Bind("processID") String processID);

    @SqlUpdate(
            "delete from CmdMutexRec " +
                    "where mutexOwner = :processID")

    Integer deleteProcessMutexes(@Bind("processID") String processID);

    @SqlUpdate(
            "delete from CmdMutexRec " +
                    "where expireTs < 'now'")

    Integer deleteAllExpiredMutexes();

    //////////////////////////////
    // CmdTimerRec
    //////////////////////////////

    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    void close();
}
