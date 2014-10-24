package part;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Used by JDBI to map SQL Object result set objects to POJOs
 */
public class PartMapper implements ResultSetMapper<Part> {

    public Part map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new Part(r.getString("partID"), r.getString("name"));
    }
}