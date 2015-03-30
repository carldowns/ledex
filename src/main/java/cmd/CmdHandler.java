package cmd;

import cmd.dao.CmdEventRec;
import cmd.dao.CmdRec;
import mgr.CmdMgr;

import javax.annotation.Nullable;


public interface CmdHandler<T extends Cmd>  {

    /**
     * returns the Cmd simple class name.
     */
    String getCmdType();

    /**
     * process the given event record.
     */
    void process (CmdMgr mgr, CmdEventRec eventRecord);

    /**
     * process the given event record against the existing Cmd.
     */
    void process (CmdMgr mgr, CmdRec cmdRecord, CmdEventRec eventRecord);

    // TODO: find a way to quiet these warnings down
    // this form helps the manager not have an error but then handler derivatives need a @SuppressWarnings("unchecked")
    // to get this definition to not show a warning

    <C extends Cmd> C convert (CmdRec cmdRecord);

    // this form causes the WorkflowMgr to have an error because it does not know
    // the return type of calls that use convert()
    // public CC convert (CmdRow cmd);

}








