package cmd.dao;

///////////////////////////////////////////////////////////////////////
//  Column  |            Type             |       Modifiers
//  --------+-----------------------------+------------------------
// cmdid    | text                        | not null
// cmdtype  | text                        | not null
// cmdstate | text                        | not null
// doc      | text                        | not null
// ts       | timestamp without time zone | not null default now()
// userid   | text                        |
///////////////////////////////////////////////////////////////////////

import org.joda.time.DateTime;

/**
 * Discerning between Cmd and CmdRec: basically pass the CmdRec as database row representation
 * around until we are ready to construct the correct Cmd operational derivative.  That step is
 * deferred to the corresponding CmdHandler per Cmd type.
 */
public class CmdRec {

    String cmdID;
    String cmdType;
    String cmdState;
    String doc;
    DateTime ts;
    String userID;

    public String getCmdID() {
        return cmdID;
    }

    public void setCmdID(String cmdID) {
        this.cmdID = cmdID;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getCmdState() {
        return cmdState;
    }

    public void setCmdState(String cmdState) {
        this.cmdState = cmdState;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public DateTime getTs() {
        return ts;
    }

    public void setTs(DateTime ts) {
        this.ts = ts;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
