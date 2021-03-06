package cmd.dao;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class CmdRecMapper implements ResultSetMapper<CmdRec> {

    public CmdRec map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        CmdRec rec = new CmdRec();
        rec.setCmdID(r.getString("cmdID"));
        rec.setCmdType(r.getString("cmdType"));
        rec.setCmdState(r.getString("cmdState"));
        rec.setUserID(r.getString("userID"));
        rec.setDoc(r.getString("doc"));
        //rec.setTs(new DateTime (r.getTimestamp(""))); // FIXME
        return rec;
    }
}