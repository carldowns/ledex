package resource;

import catalog.AssemblySQL;
import cmd.*;
import com.codahale.metrics.annotation.Timed;
import mgr.CatalogMgr;
import part.PartSQL;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/part")
@Produces(MediaType.APPLICATION_JSON)
public class PartResource {

    private CatalogMgr mgr;

    public PartResource(PartSQL sql, AssemblySQL assemblySQL) {
        this.mgr = new CatalogMgr(sql, assemblySQL);
    }

    //////////////////////////////
    // Command / Manager Methods
    //////////////////////////////

    // @PUT  TODO: REST contract
    @GET
    @Path ("/import")
    @Timed
    public ImportPartsCmd importSuppliers (@QueryParam("pathURI") String pathURI) {
        ImportPartsCmd cmd = new ImportPartsCmd();
        cmd.setInputFilePath(pathURI);
        mgr.importParts(cmd);
        return cmd;
    }

    @GET
    @Path ("/export")
    @Timed
    public ExportPartsCmd exportSuppliers (@QueryParam("pathURI") String pathURI) {
        ExportPartsCmd cmd = new ExportPartsCmd();
        cmd.setOutputFilePath(pathURI);
        mgr.exportParts(cmd);
        return cmd;
    }

    @GET
    @Path ("/get")
    @Timed
    public GetPartCmd getSupplier (@QueryParam("partID") String partID ) {
        GetPartCmd cmd = new GetPartCmd();
        cmd.setPartID(partID);
        mgr.getPart(cmd);
        return cmd;
    }
}