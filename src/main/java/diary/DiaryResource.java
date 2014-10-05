package diary;

import java.util.Iterator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.codahale.metrics.annotation.Timed;

@Path("/diary")
@Produces(MediaType.APPLICATION_JSON)
public class DiaryResource {
    
    private DiaryDAO dao;
    
    public DiaryResource(DiaryDAO dao) {
        this.dao = dao;
    }

//    @GET
//    @Timed
//    public EntryRep addEntry (@QueryParam("name") Optional<String> name, @QueryParam("entry") Optional<String> entry) {
//        return new EntryRep();
//    }
    
    @GET
    @Timed
    public Iterator<String> findAllNames () {
        return dao.findAllNames();
    }
    
    @GET 
    @Path ("/persons")
    @Timed
    public Iterator<PersonRep> findAll () {
        return dao.findAll();
    }
    
}