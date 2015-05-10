package cmd;

import cmd.dao.CmdRec;


public interface ICmdHandler<T extends Cmd>  {

    String getCmdType();
    void process(CmdMgr mgr, CmdRec cmdRecord);
    <C extends Cmd> C convert(CmdRec cmdRecord);
}








