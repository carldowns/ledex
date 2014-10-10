package app;

import java.util.Iterator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import supplier.SupplierDAO;
import supplier.SupplierDocEntity;
import supplier.SupplierEntity;
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
    @Path ("/list")
    @Timed
    public Iterator<SupplierEntity> getAllSuppliers () {
        return dao.findAll();
    }
    
    @GET
    @Timed
    public Iterator<String> findAllNames () {
        return dao.findAllNames();
    }
    
    @GET 
    @Path ("/detail")
    @Timed
    public SupplierDocEntity getSupplierDoc (@QueryParam("supplierID") String id ) {
        System.out.println ("got here with Id " + id);
        return dao.getSupplierDoc(id);
    }
    
}