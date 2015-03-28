package system;

import system.dao.CmdRow;


public interface CmdHandler<CC extends Cmd>  {
    public String getCmdType();
    public void process (CmdRow row, CmdEvent event);

    // this form helps the manager not have an error but then handler derivatives need a @SuppressWarnings("unchecked")
    // to get this definition to not show a warning
    public <C extends Cmd> C convert (CmdRow cmd);

    // this form causes the WorkflowMgr to have an error because it does not know
    // the return type of calls that use convert()
    // public CC convert (CmdRow cmd);

}








