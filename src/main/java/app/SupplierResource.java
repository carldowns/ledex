package app;

import java.net.URI;
import java.util.Iterator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cmd.ExportSuppliersCmd;
import cmd.ImportSuppliersCmd;
import logic.SupplierMgr;
import supplier.Supplier;
import supplier.SupplierSQL;
import supplier.SupplierDoc;
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

//    @GET
//    @Timed
//    public EntryRep addEntry (@QueryParam("name") Optional<String> name, @QueryParam("entry") Optional<String> entry) {
//        return new EntryRep();
//    }
    
    @GET 
    @Path ("/findAll")
    @Timed
    public Iterator<Supplier> getAllSuppliers () {
        return sql.getAllSuppliers();
    }

    @GET
    @Timed
    @Path ("/findAllNames")
    public Iterator<String> findAllNames () {
        return sql.getAllSupplierNames();
    }

    @GET
    @Path ("/find")
    @Timed
    public Supplier getSupplier (@QueryParam("supplierID") String id ) {
        return sql.getSupplierByID(id);
    }

    ///////////////////////////
    // Supplier Doc Methods
    ///////////////////////////

    @GET
    @Path ("/findDoc")
    @Timed
    public SupplierDoc getSupplierDoc (@QueryParam("supplierID") String id ) {
        return sql.getSupplierDoc(id);
    }

    ////////////////////////////
    // Manager Methods
    ////////////////////////////

    @GET
    @Path ("/importSuppliers")
    @Timed
    public ImportSuppliersCmd importSuppliers (@QueryParam("pathURI") String pathURI) throws Exception {
        ImportSuppliersCmd cmd = new ImportSuppliersCmd();
        cmd.setInputFilePath(new URI(pathURI));
        mgr.importSuppliers(cmd);
        return cmd;
    }

    @GET
    @Path ("/exportSuppliers")
    @Timed
    public ExportSuppliersCmd exportSuppliers (@QueryParam("pathURI") String pathURI) throws Exception {
        ExportSuppliersCmd cmd = new ExportSuppliersCmd();
        cmd.setOutputFilePath(new URI(pathURI));
        mgr.exportSuppliers(cmd);
        return cmd;
    }

}