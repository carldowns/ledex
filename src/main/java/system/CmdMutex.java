package system;

/**
 * Corresponds to mutexes existing for this process in the data store
 */
public class CmdMutex {

    private String _processID;
    private String _mutexID;
    private Type _type;

    public CmdMutex (String processID, String mutexID, Type type) {
      _processID = processID;
        _mutexID = mutexID;
        _type = type;
    }

    public static enum Type {
        event,
        cmd
    }
}