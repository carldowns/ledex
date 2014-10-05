package supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class SupplierDocEntityMapper implements ResultSetMapper<SupplierDocEntity> {

    public SupplierDocEntity map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new SupplierDocEntity(r.getString("supplierID"), r.getString("doc"), r.getTimestamp("ts"));
    }
}