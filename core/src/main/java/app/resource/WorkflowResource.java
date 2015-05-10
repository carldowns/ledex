package app.resource;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cmd.CmdMgr;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/flow")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class WorkflowResource {

    private CmdMgr mgr;

    @Inject
    public WorkflowResource(CmdMgr mgr) {
        this.mgr = mgr;
    }

}