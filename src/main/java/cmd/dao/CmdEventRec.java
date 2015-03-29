package cmd.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String _sourceCmdID;
    private String _targetCmdID;


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

    public String getSourceCmdID() {
        return _sourceCmdID;
    }

    public void setSourceCmdID(String _sourceCmdID) {
        this._sourceCmdID = _sourceCmdID;
    }

    public String getTargetCmdID() {
        return _targetCmdID;
    }

    public void setTargetCmdID(String _targetCmdID) {
        this._targetCmdID = _targetCmdID;
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
        if (_sourceCmdID != null ? !_sourceCmdID.equals(that._sourceCmdID) : that._sourceCmdID != null) return false;
        return !(_targetCmdID != null ? !_targetCmdID.equals(that._targetCmdID) : that._targetCmdID != null);

    }

    @Override
    public int hashCode() {
        int result = _eventID.hashCode();
        result = 31 * result + _eventType.hashCode();
        result = 31 * result + _eventState.hashCode();
        result = 31 * result + (_sourceCmdID != null ? _sourceCmdID.hashCode() : 0);
        result = 31 * result + (_targetCmdID != null ? _targetCmdID.hashCode() : 0);
        return result;
    }
}
