package cmd;

import cmd.dao.CmdRec2;


public interface ICmdHandler2<T extends Cmd2>  {

    String getCmdType();
    void process(CmdMgr2 mgr, CmdRec2 cmdRecord);
    <C extends Cmd2> C convert(CmdRec2 cmdRecord);
}








