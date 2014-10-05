package diary;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class PersonRepMapper implements ResultSetMapper<PersonRep> {

    public PersonRep map(int index, ResultSet r, StatementContext ctx)
            throws SQLException {
        return new PersonRep(r.getInt("personid"), r.getString("name"));
    }
}