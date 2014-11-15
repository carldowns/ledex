package supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * Used by JDBI to map SQL Object result set objects to POJOs
 */
public class SupplierMapper implements ResultSetMapper<Supplier> {

    public Supplier map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new Supplier(r.getString("supplierID"), r.getString("name"));
    }
}