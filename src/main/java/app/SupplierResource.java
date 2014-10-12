package app;

import java.util.Iterator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import supplier.Supplier;
import supplier.SupplierDAO;
import supplier.SupplierDoc;
import com.codahale.metrics.annotation.Timed;

@Path("/supplier")
@Produces(MediaType.APPLICATION_JSON)
public class SupplierResource {
    
    private SupplierDAO dao;
    
    public SupplierResource(SupplierDAO dao) {
        this.dao = dao;
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
        return dao.getAllSuppliers();
    }
    
    @GET
    @Timed
    @Path ("/findAllNames")
    public Iterator<String> findAllNames () {
        return dao.getAllSupplierNames();
    }

    @GET
    @Path ("/find")
    @Timed
    public Supplier getSupplier (@QueryParam("supplierID") String id ) {
        return dao.getSupplierByID(id);
    }

    ///////////////////////////
    // Supplier Doc Methods
    ///////////////////////////

    @GET
    @Path ("/findDoc")
    @Timed
    public SupplierDoc getSupplierDoc (@QueryParam("supplierID") String id ) {
        return dao.getSupplierDoc(id);
    }
    
}