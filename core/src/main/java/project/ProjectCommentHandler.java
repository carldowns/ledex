package project;

import cmd.Cmd;
import cmd.CmdMgr;
import cmd.CmdRuntimeException;
import cmd.ICmdHandler;
import cmd.dao.CmdRec2;
import com.google.inject.Singleton;

/**
 *
 */

@Singleton public class ProjectCommentHandler implements ICmdHandler<ProjectComment> {

    @Override
    public String getCmdType() {
        return ProjectComment.class.getSimpleName();
    }

    @Override
    public void process(CmdMgr mgr, CmdRec2 cmdRecord) {
        throw new CmdRuntimeException("not implemented");
    }

    @Override
    public <C extends Cmd> C convert(CmdRec2 cmdRecord) {
        throw new CmdRuntimeException("not implemented");
    }
}
