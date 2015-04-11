package cmd;

import cmd.dao.CmdRec2;


public interface ICmdHandler<T extends Cmd>  {

    String getCmdType();
    void process(CmdMgr mgr, CmdRec2 cmdRecord);
    <C extends Cmd> C convert(CmdRec2 cmdRecord);
}








