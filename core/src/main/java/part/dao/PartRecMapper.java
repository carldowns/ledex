package part.dao;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class PartRecMapper implements ResultSetMapper<PartRec> {

    public PartRec map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new PartRec(r.getString("partID"), r.getString("supplierID"), r.getString("name"), r.getString("function"));
    }
}