package app.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import mgr.CatalogMgr;
import mgr.WorkflowMgr;
import part.cmd.PartExportCmd;
import part.cmd.PartFetchCmd;
import part.cmd.PartImportCmd;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/flow")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class WorkflowResource {

    private WorkflowMgr mgr;

    @Inject
    public WorkflowResource(WorkflowMgr mgr) {
        this.mgr = mgr;
    }

}