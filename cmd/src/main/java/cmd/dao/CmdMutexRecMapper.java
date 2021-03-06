package cmd.dao;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class CmdMutexRecMapper implements ResultSetMapper<CmdMutexRec> {

    public CmdMutexRec map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new CmdMutexRec(
                r.getString("mutexOwner"),
                r.getString("mutexID"),
                r.getString("mutexType"),
                r.getTimestamp("expireTs").getTime());
    }
}