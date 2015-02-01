package app.resource;

import java.util.Iterator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cmd.SupplierExportCmd;
import cmd.SupplierFetchCmd;
import cmd.SupplierImportCmd;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import mgr.SupplierMgr;
import supplier.dao.SupplierSQL;
import com.codahale.metrics.annotation.Timed;

@Path("/supplier")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class SupplierResource {

    private SupplierMgr mgr;
    private SupplierSQL sql;

    @Inject
    public SupplierResource(SupplierMgr mgr, SupplierSQL sql) {
        this.mgr = mgr;
        this.sql = sql;
    }

    //////////////////////////////
    // Command / Manager Methods
    //////////////////////////////

    @GET
    @Path ("/import")
    @Timed
    public SupplierImportCmd importSuppliers (@QueryParam("pathURI") String pathURI) {
        SupplierImportCmd cmd = new SupplierImportCmd();
        cmd.setInputFilePath(pathURI);
        mgr.exec(cmd);
        return cmd;
    }

    @GET
    @Path ("/export")
    @Timed
    public SupplierExportCmd exportSuppliers (@QueryParam("pathURI") String pathURI) {
        SupplierExportCmd cmd = new SupplierExportCmd();
        cmd.setOutputFilePath(pathURI);
        mgr.exec(cmd);
        return cmd;
    }

    @GET
    @Path ("/get")
    @Timed
    public SupplierFetchCmd getSupplier (@QueryParam("supplierID") String id ) {
        SupplierFetchCmd cmd = new SupplierFetchCmd();
        cmd.setSupplierID(id);
        mgr.exec(cmd);
        return cmd;
    }

    ////////////////////////////
    // Direct SQL Methods
    ////////////////////////////

    @GET
    @Timed
    @Path ("/names")
    public Iterator<String> findAllNames () {
        return sql.getAllSupplierNames();
    }

}