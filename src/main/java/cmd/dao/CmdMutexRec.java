package cmd.dao;

/**
 * Corresponds to mutexes existing for this process in the data store
 */
public class CmdMutexRec {

    private String _processID;
    private String _mutexID;
    private Type _type;

    public CmdMutexRec(String processID, String mutexID, Type type) {
      _processID = processID;
        _mutexID = mutexID;
        _type = type;
    }

    public String get_processID() {
        return _processID;
    }

    public String get_mutexID() {
        return _mutexID;
    }

    public Type get_type() {
        return _type;
    }

    public static enum Type {
        event,
        cmd
    }

}