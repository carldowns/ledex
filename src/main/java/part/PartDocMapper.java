package part;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartDocMapper implements ResultSetMapper<PartDoc> {

    public PartDoc map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new PartDoc(r.getString("partID"), r.getString("partDocID"), r.getString("doc"), r.getTimestamp("ts"));
    }
}