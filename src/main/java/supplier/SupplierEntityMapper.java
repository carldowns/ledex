package supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class SupplierEntityMapper implements ResultSetMapper<SupplierEntity> {

    public SupplierEntity map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new SupplierEntity(r.getString("supplierID"), r.getString("name"));
    }
}