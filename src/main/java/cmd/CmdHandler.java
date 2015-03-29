package cmd;

import cmd.dao.CmdEventRec;
import cmd.dao.CmdRec;


public interface CmdHandler<CC extends Cmd>  {

    /**
     * returns the Cmd simple class name.
     */
    String getCmdType();

    /**
     * process the given Cmd row and event
     */
    void process (CmdRec row, CmdEventRec event);

    // TODO: find a way to quiet these warnings down
    // this form helps the manager not have an error but then handler derivatives need a @SuppressWarnings("unchecked")
    // to get this definition to not show a warning

    <C extends Cmd> C convert (CmdRec cmd);

    // this form causes the WorkflowMgr to have an error because it does not know
    // the return type of calls that use convert()
    // public CC convert (CmdRow cmd);

}








