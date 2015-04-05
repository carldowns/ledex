package cmd.dao;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class CmdEventRecMapper implements ResultSetMapper<CmdEventRec> {

    public CmdEventRec map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        CmdEventRec event =
                new CmdEventRec(
                r.getString("eventID"),
                r.getString("eventType"),
                r.getString("cmdTargetType"));

        event.setEventState(r.getString("eventState")).
                setCmdSourceID(r.getString("cmdSourceID")).
                setCmdTargetID(r.getString("cmdTargetID")).
                setCmdSourceType(r.getString("cmdSourceType"));

        return event;
    }
}