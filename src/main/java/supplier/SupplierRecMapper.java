package supplier;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class SupplierRecMapper implements ResultSetMapper<SupplierRec> {

    public SupplierRec map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new SupplierRec(r.getString("supplierID"), r.getString("name"));
    }
}