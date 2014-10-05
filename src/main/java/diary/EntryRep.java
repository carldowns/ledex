package diary;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EntryRep {
    private long id;

    //@Length(max = 3)
    private String content;

    public EntryRep() {
        // Jackson deserialization
    }

    public EntryRep(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
    
    public class EntryRepMapper implements ResultSetMapper<EntryRep>
    {
      public EntryRep map(int index, ResultSet r, StatementContext ctx) throws SQLException
      {
        return new EntryRep(r.getInt("id"), r.getString("name"));
      }
    }
}