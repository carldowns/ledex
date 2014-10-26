package app;

import java.util.Iterator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cmd.ExportSuppliersCmd;
import cmd.GetSupplierCmd;
import cmd.ImportSuppliersCmd;
import logic.SupplierMgr;
import supplier.SupplierSQL;
import com.codahale.metrics.annotation.Timed;

@Path("/supplier")
@Produces(MediaType.APPLICATION_JSON)
public class SupplierResource {

    private SupplierSQL sql;
    private SupplierMgr mgr;

    public SupplierResource(SupplierSQL sql) {
        this.sql = sql;
        this.mgr = new SupplierMgr(sql);
    }

    //////////////////////////////
    // Command / Manager Methods
    //////////////////////////////

    @GET
    @Path ("/importSuppliers")
    @Timed
    public ImportSuppliersCmd importSuppliers (@QueryParam("pathURI") String pathURI) {
        ImportSuppliersCmd cmd = new ImportSuppliersCmd();
        cmd.setInputFilePath(pathURI);
        mgr.importSuppliers(cmd);
        return cmd;
    }

    @GET
    @Path ("/exportSuppliers")
    @Timed
    public ExportSuppliersCmd exportSuppliers (@QueryParam("pathURI") String pathURI) {
        ExportSuppliersCmd cmd = new ExportSuppliersCmd();
        cmd.setOutputFilePath(pathURI);
        mgr.exportSuppliers(cmd);
        return cmd;
    }

    @GET
    @Path ("/getSupplier")
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
    @Path ("/getAllSupplierNames")
    public Iterator<String> findAllNames () {
        return sql.getAllSupplierNames();
    }

}