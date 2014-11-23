package app.resource;

import cmd.*;
import com.codahale.metrics.annotation.Timed;
import catalog.AssemblyEngine;
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
    private AssemblyEngine assemblyEngine;

//    @Inject
//    public AssemblyResource(PartSQL partSQL, AssemblySQL assemblySQL) {
//        this.mgr = new CatalogMgr(partSQL, assemblySQL);
//        assemblyMgr = new CatalogEngine(mgr);
//    }

    @Inject
    public AssemblyResource(CatalogMgr catalogMgr, AssemblyEngine assemblyEngine) {
        this.catalogMgr = catalogMgr;
        this.assemblyEngine = assemblyEngine;
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
        catalogMgr.importAssemblies(cmd);
        return cmd;
    }

    @GET
    @Path ("/export")
    @Timed
    public ExportAssembliesCmd exportSuppliers (@QueryParam("pathURI") String pathURI) {
        ExportAssembliesCmd cmd = new ExportAssembliesCmd();
        cmd.setOutputFilePath(pathURI);
        catalogMgr.exportAssemblies(cmd);
        return cmd;
    }

    @GET
    @Path ("/get")
    @Timed
    public GetAssemblyCmd getSupplier (@QueryParam("assemblyID") String assemblyID ) {
        GetAssemblyCmd cmd = new GetAssemblyCmd();
        cmd.setAssemblyID(assemblyID);
        catalogMgr.getAssembly(cmd);
        return cmd;
    }

    @GET
    @Path ("/update-catalog")
    @Timed
    public UpdateCatalogCmd getSupplier () {
        UpdateCatalogCmd cmd = new UpdateCatalogCmd();
        assemblyEngine.rebuildCatalog(cmd);
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