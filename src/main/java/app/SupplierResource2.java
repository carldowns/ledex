package app;

import com.codahale.metrics.annotation.Timed;
import supplier.Supplier;
import supplier.SupplierDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Iterator;
import java.util.List;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class SupplierResource2 {

    private SupplierDAO dao2;

    public SupplierResource2(SupplierDAO dao2) {
        this.dao2 = dao2;
    }

    @GET
    @Timed
    @Path ("/findAllNames")
    public Iterator<String> findAllNames2 () {
        List<String> result = dao2.getAllSupplierNames();
        return result.iterator();
    }

    @GET
    @Path ("/findAll")
    @Timed
    public Iterator<Supplier> getAllSuppliers () {
        return dao2.getAllSuppliers();
    }

    @GET
    @Path ("/findAll2")
    @Timed
    public Iterator<Supplier> getAllSuppliers2 () {
        return dao2.getAllSuppliers2();
    }

    @GET
    @Path ("/findAll3")
    @Timed
    public Iterator<Supplier> getAllSuppliers3 () {
        return dao2.getAllSuppliers3();
    }
}