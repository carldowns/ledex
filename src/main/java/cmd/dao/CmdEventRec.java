package cmd.dao;

// Table "public.cmdeventrec"
//        Column    |            Type             |       Modifiers
//        -------------+-----------------------------+------------------------
//        eventid     | text                        | not null
//        eventtype   | text                        | not null
//        eventstate  | text                        | not null
//        eventdue    | timestamp without time zone | not null default now()
//        targetcmdid | text                        |
//        sourcecmdid | text                        |
//        ts          | timestamp without time zone | not null default now()
//        userid      | text                        |
//        Indexes:
//        "cmdeventrec_pkey" PRIMARY KEY, btree (eventid)
//        "cmdeventrec_state_idx" btree (eventstate)

/**
 *
 */
public class CmdEventRec {
    private final String _eventID;
    private final String _eventType;
    private CmdEventState _eventState = CmdEventState.pending;
    private String _cmdSourceID;
    private String _cmdTargetID;


    public CmdEventRec (String eventID, String eventType) {
        _eventType = eventType;
        _eventID = eventID;
    }

    public CmdEventRec (String eventID, String eventType, CmdEventState state) {
        this (eventID, eventType);
        this.setEventState(state);
    }

    public CmdEventRec (String eventID, String eventType, String state) {
        this(eventID, eventType, CmdEventState.valueOf(state));
    }

    //////////////
    // Event ID
    //////////////

    public String getEventID() {
        return _eventID;
    }

    public boolean hasAssignedID() {
        return _eventID != null;
    }

    ////////////////
    // Event Type
    ////////////////

    public String getEventType() {
        return _eventType;
    }

    //////////////
    // Cmd IDs
    //////////////

    public String getCmdSourceID() {
        return _cmdSourceID;
    }

    public void setCmdSourceID(String _sourceCmdID) {
        this._cmdSourceID = _sourceCmdID;
    }

    public String getCmdTargetID() {
        return _cmdTargetID;
    }

    public void setCmdTargetID(String _targetCmdID) {
        this._cmdTargetID = _targetCmdID;
    }

    /////////////////
    // Event State
    /////////////////

    public CmdEventState getEventState() {
        return _eventState;
    }

    public void setEventState(CmdEventState _eventState) {
        this._eventState = _eventState;
    }

    public enum CmdEventState {
        error,
        pending,
        suspended,
        completed
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CmdEventRec)) return false;

        CmdEventRec that = (CmdEventRec) o;

        if (!_eventID.equals(that._eventID)) return false;
        if (!_eventType.equals(that._eventType)) return false;
        if (_eventState != that._eventState) return false;
        if (_cmdSourceID != null ? !_cmdSourceID.equals(that._cmdSourceID) : that._cmdSourceID != null) return false;
        return !(_cmdTargetID != null ? !_cmdTargetID.equals(that._cmdTargetID) : that._cmdTargetID != null);

    }

    @Override
    public int hashCode() {
        int result = _eventID.hashCode();
        result = 31 * result + _eventType.hashCode();
        result = 31 * result + _eventState.hashCode();
        result = 31 * result + (_cmdSourceID != null ? _cmdSourceID.hashCode() : 0);
        result = 31 * result + (_cmdTargetID != null ? _cmdTargetID.hashCode() : 0);
        return result;
    }
}
