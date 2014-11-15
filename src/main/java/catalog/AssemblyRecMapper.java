package catalog;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import part.PartRec;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class AssemblyRecMapper implements ResultSetMapper<AssemblyRec> {

    public AssemblyRec map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new AssemblyRec(r.getString("assemblyID"), r.getString("name"));
    }
}