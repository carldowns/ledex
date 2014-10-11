package dao;

import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import supplier.SupplierDAO;

import java.util.Iterator;

/**
 * Testing
 * Created by carl_downs on 10/11/14.
 */
public class SupplierDAOIntegrationTest {

private SupplierDAO dao;

    @Before
    public void setup() {
        // TODO change this to work with a DW configuration
        DBI dbi = new DBI("jdbc:postgresql:catalog", "program", "fiddlesticks");
        dao = dbi.onDemand(SupplierDAO.class);
    }

    @Test
    public void shouldFind() {
        Iterator<String> results = dao.getAllSupplierNames();
        org.junit.Assert.assertNotNull(results);
        while (results.hasNext()) {
            String name = results.next();
            System.out.println (name);
        }
    }
}
