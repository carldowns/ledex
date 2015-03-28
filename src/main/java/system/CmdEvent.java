package system;

/**
 *
 */
public class CmdEvent {

    private final String _eventID;
    private final String _eventType;
    private String _sourceCmdID;
    private String _targetCmdID;

    public CmdEvent (String eventType, String eventID) {
        _eventType = eventType;
        _eventID = eventID;
    }

    public CmdEvent (String eventType, String eventID, String sourceCmdID) {
        this (eventType, eventID);
        _sourceCmdID = sourceCmdID;
    }

    public CmdEvent (String eventType, String eventID, String sourceCmdID, String targetCmdID) {
        this (eventType, eventID, sourceCmdID);
        _targetCmdID = targetCmdID;
    }

    public boolean hasSource () {
        return _sourceCmdID != null;
    }

    public String getEventType() {
        return _eventType;
    }

    public String getEventID() {
        return _eventID;
    }

    public boolean hasAssignedID() {
        return _eventID != null;
    }

    public String getTargetCmdID() {
        return _targetCmdID;
    }

    public String getSourceCmdID() {
        return _sourceCmdID;
    }

}
