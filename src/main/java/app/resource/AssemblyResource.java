package app.resource;

import catalog.dao.AssemblySQL;
import cmd.*;
import com.codahale.metrics.annotation.Timed;
import catalog.CatalogEngine;
import mgr.CatalogMgr;
import part.dao.PartSQL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/assembly")
@Produces(MediaType.APPLICATION_JSON)
public class AssemblyResource {

    private CatalogMgr mgr;
    private CatalogEngine assemblyMgr;

    public AssemblyResource(PartSQL partSQL, AssemblySQL assemblySQL) {
        this.mgr = new CatalogMgr(partSQL, assemblySQL);
        assemblyMgr = new CatalogEngine(mgr);
    }

    //////////////////////////////
    // Command / Manager Methods
    //////////////////////////////

    @GET
    @Path ("/import")
    @Timed
    public ImportAssembliesCmd importSuppliers (@QueryParam("pathURI") String pathURI) {
        ImportAssembliesCmd cmd = new ImportAssembliesCmd();
        cmd.setInputFilePath(pathURI);
        mgr.importAssemblies(cmd);
        return cmd;
    }

    @GET
    @Path ("/export")
    @Timed
    public ExportAssembliesCmd exportSuppliers (@QueryParam("pathURI") String pathURI) {
        ExportAssembliesCmd cmd = new ExportAssembliesCmd();
        cmd.setOutputFilePath(pathURI);
        mgr.exportAssemblies(cmd);
        return cmd;
    }

    @GET
    @Path ("/get")
    @Timed
    public GetAssemblyCmd getSupplier (@QueryParam("assemblyID") String assemblyID ) {
        GetAssemblyCmd cmd = new GetAssemblyCmd();
        cmd.setAssemblyID(assemblyID);
        mgr.getAssembly(cmd);
        return cmd;
    }

    @GET
    @Path ("/update-catalog")
    @Timed
    public UpdateCatalogCmd getSupplier () {
        UpdateCatalogCmd cmd = new UpdateCatalogCmd();
        assemblyMgr.rebuildCatalog(cmd);
        return cmd;
    }

//
//    ////////////////////////////
//    // Direct SQL Methods
//    ////////////////////////////
//
//    @GET
//    @Timed
//    @Path ("/names")
//    public Iterator<String> findAllNames () {
//        return sql.getAllSupplierNames();
//    }
//
}