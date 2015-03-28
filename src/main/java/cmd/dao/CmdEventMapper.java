package cmd.dao;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class CmdEventMapper implements ResultSetMapper<CmdEventRow> {

    public CmdEventRow map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new CmdEventRow();
    }
}