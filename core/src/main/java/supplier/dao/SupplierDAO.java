package supplier.dao;

import ch.qos.logback.classic.Logger;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.util.StringMapper;
import org.slf4j.LoggerFactory;
import supplier.Supplier;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is hanging out as an example class.  Not using it at the present.
 * Created by carl_downs on 10/12/14.
 */
class SupplierDAO {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(SupplierDAO.class);
    DBI dbi;

    public SupplierDAO(DBI dbi) {
        this.dbi = dbi;
    }

    public List<Supplier> getSuppliers () {
        return null;
    }

    Supplier getSupplierByID(String supplierID) {
        return null;
    }

    public List<String> getAllSupplierNames() {
        Handle h = null;
        //LOGGER.info("retrieving all supplier names");

        try {
            h = dbi.open();
            Query<Map<String, Object>> q =
                    h.createQuery("select name from supplier");

            Query<String> q2 = q.map(StringMapper.FIRST);
            List<String> rs = q2.list();
            return rs;
        } finally {
            if (h != null)
                h.close();
        }
    }

    public Iterator<SupplierRec> getAllSuppliers() {
        SupplierSQL sql = null;
        try {
            sql = dbi.open(SupplierSQL.class);
            return sql.getAllSuppliers();
        } finally {
            if (sql != null)
                sql.close();
        }
    }

    public Iterator<Supplier> getAllSuppliers2() {
        Handle h = null;
        try {
            h = dbi.open();
            h.registerMapper(new SupplierMapper());// TRY IT HERE... THIS WORKS !!!!!!!!!!!!!!!!!!

            Query<Map<String,Object>> q =
                    h.createQuery("select * from supplier");
            Query<Supplier> q2 = q.mapTo(Supplier.class);// must use mapTo()  THIS WORKS !!!!!!!!!!!!!!!!!!
            List<Supplier> l = q2.list();
            return q2.iterator();

        } finally {
            if (h != null)
                h.close();
        }
    }

    public Iterator<Supplier> getAllSuppliers3() {
        Handle h = null;
        try {
            h = dbi.open();
            h.registerMapper(new SupplierMapper());

            List<Supplier> rs =
            h.createQuery("select * from supplier")
            .map(Supplier.class)
            .list();

            return rs.iterator();

        } finally {
            if (h != null)
                h.close();
        }
    }
}
