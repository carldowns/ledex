package supplier.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class SupplierDocMapper implements ResultSetMapper<SupplierDoc> {

    public SupplierDoc map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new SupplierDoc(r.getString("supplierID"), r.getString("supplierDocID"), r.getString("doc"), r.getTimestamp("ts"));
    }
}