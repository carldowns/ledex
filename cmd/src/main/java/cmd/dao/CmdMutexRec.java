package cmd.dao;

import org.postgresql.jdbc2.TimestampUtils;

/**
 * Corresponds to mutexes existing for this process in the data store
 */
public class CmdMutexRec {

    private String _processID;
    private String _mutexID;
    private String _type;
    private Long _expireTs;

    public CmdMutexRec(String processID, String mutexID, String type, Long expireTs) {
      _processID = processID;
        _mutexID = mutexID;
        _type = type;
        _expireTs = expireTs;
    }

    public String getProcessID() {
        return _processID;
    }

    public String getMutexID() {
        return _mutexID;
    }

    public String getType() {
        return _type;
    }

    public Long getExpireTs() {
        return _expireTs;
    }
}