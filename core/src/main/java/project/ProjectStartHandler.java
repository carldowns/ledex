package project;

import cmd.Cmd;
import cmd.CmdMgr;
import cmd.CmdRuntimeException;
import cmd.ICmdHandler;
import cmd.dao.CmdRec;
import com.google.inject.Singleton;

/**
 *
 */

@Singleton public class ProjectStartHandler implements ICmdHandler<ProjectStart> {

    @Override
    public String getCmdType() {
        return ProjectStart.class.getSimpleName();
    }

    @Override
    public void process(CmdMgr mgr, CmdRec cmdRecord) {
        throw new CmdRuntimeException("not implemented");
    }

    @Override
    public <C extends Cmd> C convert(CmdRec cmdRecord) {
        throw new CmdRuntimeException("not implemented");
    }
}
