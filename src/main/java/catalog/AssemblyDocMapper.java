package catalog;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class AssemblyDocMapper implements ResultSetMapper<AssemblyDoc> {

    public AssemblyDoc map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new AssemblyDoc(r.getString("partID"), r.getString("partDocID"), r.getString("doc"), r.getTimestamp("ts"));
    }
}