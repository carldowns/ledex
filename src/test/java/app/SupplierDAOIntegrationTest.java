package app;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import supplier.dao.SupplierSQL;

import java.util.Iterator;

/**
 * Testing a direct call to local PostresSQL database instance.
 * Created by carl_downs on 10/11/14.
 */
public class SupplierDAOIntegrationTest {

    private SupplierSQL dao;

    @Before
    public void setup() {
        // TODO change this to work with a DW configuration
        DBI dbi = new DBI("jdbc:postgresql:catalog", "program", "fiddlesticks");
        dao = dbi.onDemand(SupplierSQL.class);
    }

    @Test
    //@Ignore
    public void shouldFind() {
        Iterator<String> results = dao.getAllSupplierNames();
        org.junit.Assert.assertNotNull(results);
        while (results.hasNext()) {
            String name = results.next();
            System.out.println(name);
        }
    }
}
