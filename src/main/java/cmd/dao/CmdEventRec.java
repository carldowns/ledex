package cmd.dao;


import cmd.Cmd;
import com.google.common.base.Preconditions;
import java.util.Objects;

// Table "public.cmdeventrec"
//        Column     |            Type             |       Modifiers
//        ---------------+-----------------------------+------------------------
//        eventid       | text                        | not null
//        eventtype     | text                        | not null
//        eventstate    | text                        | not null
//        eventdue      | timestamp without time zone | not null default now()
//        cmdsourceid   | text                        |
//        cmdsourcetype | text                        |
//        cmdtargetid   | text                        |
//        cmdtargettype | text                        |
//        ts            | timestamp without time zone | not null default now()
//        userid        | text                        |
//        Indexes:
//        "cmdeventrec_pkey" PRIMARY KEY, btree (eventid)
//        "cmdeventrec_state_idx" btree (eventstate)


/**
 * each event has to carry some information.
 * At the least, it needs a type to know what type of event it is.
 * Then we can either carry data in the event proper, or refer cmd-to-cmd to get data / context.
 * The current model holds that Cmds are where data and code is preserved and events are the
 * connection paths between commands from a reference standpoint as well as a means for
 * controlling concurrency and also allocating execution time slice at the right times.
 */
public class CmdEventRec {
    /**
     * required: must have a event ID when persisted
     */
    private final String _eventID;

    /**
     * required: type tells the receiving cmd what to do
     */
    private final String _eventType;

    /**
     * required: state tells the system what needs to happen next
     */
    private CmdEventState _eventState = CmdEventState.pending;

    /**
     * required: an event may itself cause a target cmd to be created
     */
    private String _cmdTargetType;

    /**
     * optional: address of a specific target cmd.  If missing, the implication
     * is that the target is to be created
     */
    private String _cmdTargetID;

    /**
     * optional: some events originate a cmd/event relationship.
     * TODO: is this what we want?
     */
    private String _cmdSourceID;

    /**
     * optional: kept for debugging / clarity
     */
    private String _cmdSourceType;


    //////////////////////
    // Constructors
    //////////////////////

    public CmdEventRec (String eventID, String eventType, String cmdTargetType) {
        _eventType = eventType;
        _eventID = eventID;
        _cmdTargetType = cmdTargetType;
    }

    public <C extends Cmd> CmdEventRec (String eventID, String eventType, Class<C> cmdTarget) {
        _eventType = eventType;
        _eventID = eventID;
        _cmdTargetType = cmdTarget.getSimpleName();
    }

    ///////////////////
    // Validation
    ///////////////////

    public void validate () {
        Preconditions.checkNotNull(_eventID, "_eventID required");
        Preconditions.checkNotNull(_eventType, "_eventType required");
        Preconditions.checkNotNull(_eventState, "_eventState required");
        Preconditions.checkNotNull(_cmdTargetType, "_cmdTargetType required");
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

    ///////////////
    // Source Cmd
    ///////////////

    public String getCmdSourceID() {
        return _cmdSourceID;
    }

    public CmdEventRec setCmdSourceID(String _sourceCmdID) {
        this._cmdSourceID = _sourceCmdID;
        return this;
    }

    public String getCmdSourceType() {
        return _cmdSourceType;
    }

    public CmdEventRec setCmdSourceType(String _cmdSourceType) {
        this._cmdSourceType = _cmdSourceType;
        return this;
    }

    ///////////////
    // Target Cmd
    ///////////////

    public void setCmdTarget (Cmd target) {
        setCmdTargetID(target.getID());
        setCmdTargetType(target.getType());
    }

    public String getCmdTargetID() {
        return _cmdTargetID;
    }

    public CmdEventRec setCmdTargetID(String _targetCmdID) {
        this._cmdTargetID = _targetCmdID;
        return this;
    }

    public String getCmdTargetType() {
        return _cmdTargetType;
    }

    public CmdEventRec setCmdTargetType(String _cmdTargetType) {
        this._cmdTargetType = _cmdTargetType;
        return this;
    }

    /////////////////
    // Event State
    /////////////////

    public CmdEventState getEventState() {
        return _eventState;
    }

    public CmdEventRec setEventState(String eventState) {
        return setEventState(CmdEventState.valueOf(eventState));
    }

    public CmdEventRec setEventState(CmdEventState eventState) {
        _eventState = eventState;
        return this;
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

        if (_cmdSourceID != null ? !_cmdSourceID.equals(that._cmdSourceID) : that._cmdSourceID != null)
            return false;

        if (_cmdSourceType != null ? !_cmdSourceType.equals(that._cmdSourceType) : that._cmdSourceType != null)
            return false;

        if (_cmdTargetID != null ? !_cmdTargetID.equals(that._cmdTargetID) : that._cmdTargetID != null)
            return false;

        return !(_cmdTargetType != null ? !_cmdTargetType.equals(that._cmdTargetType) : that._cmdTargetType != null);

    }

    @Override
    public int hashCode() {
        int result = _eventID.hashCode();
        result = 31 * result + _eventType.hashCode();
        result = 31 * result + _eventState.hashCode();
        result = 31 * result + (_cmdSourceID != null ? _cmdSourceID.hashCode() : 0);
        result = 31 * result + (_cmdSourceType != null ? _cmdSourceType.hashCode() : 0);
        result = 31 * result + (_cmdTargetID != null ? _cmdTargetID.hashCode() : 0);
        result = 31 * result + (_cmdTargetType != null ? _cmdTargetType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CmdEventRec{" +
                "_eventID='" + _eventID + '\'' +
                ", _eventType='" + _eventType + '\'' +
                ", _eventState=" + _eventState +
                ", _cmdSourceID='" + _cmdSourceID + '\'' +
                ", _cmdSourceType='" + _cmdSourceType + '\'' +
                ", _cmdTargetID='" + _cmdTargetID + '\'' +
                ", _cmdTargetType='" + _cmdTargetType + '\'' +
                '}';
    }
}
