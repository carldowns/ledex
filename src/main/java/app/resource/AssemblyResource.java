package app.resource;

import cmd.*;
import com.codahale.metrics.annotation.Timed;
import mgr.AssemblyMgr;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import mgr.CatalogMgr;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/assembly")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class AssemblyResource {

    private CatalogMgr catalogMgr;
    private AssemblyMgr assemblyMgr;

//    @Inject
//    public AssemblyResource(PartSQL partSQL, AssemblySQL assemblySQL) {
//        this.mgr = new CatalogMgr(partSQL, assemblySQL);
//        assemblyMgr = new CatalogEngine(mgr);
//    }

    @Inject
    public AssemblyResource(CatalogMgr catalogMgr, AssemblyMgr assemblyMgr) {
        this.catalogMgr = catalogMgr;
        this.assemblyMgr = assemblyMgr;
    }

    //////////////////////////////
    // Command / Manager Methods
    //////////////////////////////

    @GET
    @Path ("/import")
    @Timed
    public AssemblyImportCmd importSuppliers (@QueryParam("pathURI") String pathURI) {
        AssemblyImportCmd cmd = new AssemblyImportCmd();
        cmd.setInputFilePath(pathURI);
        catalogMgr.exec(cmd);
        return cmd;
    }

    @GET
    @Path ("/export")
    @Timed
    public AssemblyExportCmd exportSuppliers (@QueryParam("pathURI") String pathURI) {
        AssemblyExportCmd cmd = new AssemblyExportCmd();
        cmd.setOutputFilePath(pathURI);
        catalogMgr.exec(cmd);
        return cmd;
    }

    @GET
    @Path ("/get")
    @Timed
    public AssemblyFetchCmd getSupplier (@QueryParam("assemblyID") String assemblyID ) {
        AssemblyFetchCmd cmd = new AssemblyFetchCmd();
        cmd.setAssemblyID(assemblyID);
        catalogMgr.exec(cmd);
        return cmd;
    }

    @GET
    @Path ("/update-catalog")
    @Timed
    public CatalogUpdateCmd getSupplier () {
        CatalogUpdateCmd cmd = new CatalogUpdateCmd();
        assemblyMgr.exec(cmd);
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