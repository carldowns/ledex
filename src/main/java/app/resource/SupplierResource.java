package app.resource;

import java.util.Iterator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cmd.ExportSuppliersCmd;
import cmd.GetSupplierCmd;
import cmd.ImportSuppliersCmd;
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
    public ImportSuppliersCmd importSuppliers (@QueryParam("pathURI") String pathURI) {
        ImportSuppliersCmd cmd = new ImportSuppliersCmd();
        cmd.setInputFilePath(pathURI);
        mgr.importSuppliers(cmd);
        return cmd;
    }

    @GET
    @Path ("/export")
    @Timed
    public ExportSuppliersCmd exportSuppliers (@QueryParam("pathURI") String pathURI) {
        ExportSuppliersCmd cmd = new ExportSuppliersCmd();
        cmd.setOutputFilePath(pathURI);
        mgr.exportSuppliers(cmd);
        return cmd;
    }

    @GET
    @Path ("/get")
    @Timed
    public GetSupplierCmd getSupplier (@QueryParam("supplierID") String id ) {
        GetSupplierCmd cmd = new GetSupplierCmd();
        cmd.setSupplierID(id);
        mgr.getSupplier(cmd);
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