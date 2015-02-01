package app.resource;

import cmd.*;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import mgr.CatalogMgr;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/part")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class PartResource {

    private CatalogMgr mgr;

    @Inject
    public PartResource(CatalogMgr mgr) {
        this.mgr = mgr;
    }

    //////////////////////////////
    // Command / Manager Methods
    //////////////////////////////

    // @PUT  TODO: REST contract
    @GET
    @Path ("/import")
    @Timed
    public PartImportCmd importSuppliers (@QueryParam("pathURI") String pathURI) {
        PartImportCmd cmd = new PartImportCmd();
        cmd.setInputFilePath(pathURI);
        mgr.importParts(cmd);
        return cmd;
    }

    @GET
    @Path ("/export")
    @Timed
    public PartExportCmd exportSuppliers (@QueryParam("pathURI") String pathURI) {
        PartExportCmd cmd = new PartExportCmd();
        cmd.setOutputFilePath(pathURI);
        mgr.exportParts(cmd);
        return cmd;
    }

    @GET
    @Path ("/get")
    @Timed
    public PartFetchCmd getSupplier (@QueryParam("partID") String partID ) {
        PartFetchCmd cmd = new PartFetchCmd();
        cmd.setPartID(partID);
        mgr.getPart(cmd);
        return cmd;
    }
}