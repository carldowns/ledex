package cmd.dao;

/**
 * Corresponds to mutexes existing for this process in the data store
 */
public class CmdMutexRec {

    private String _processID;
    private String _mutexID;
    private String _type;

    public CmdMutexRec(String processID, String mutexID, String type) {
      _processID = processID;
        _mutexID = mutexID;
        _type = type;
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
}