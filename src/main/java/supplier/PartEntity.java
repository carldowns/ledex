package supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PartEntity {
    private long id;

    //@Length(max = 3)
    private String content;

    public PartEntity() {
        // Jackson deserialization
    }

    public PartEntity(long id, String content) {
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
    
    public class PartEntityMapper implements ResultSetMapper<PartEntity>
    {
      public PartEntity map(int index, ResultSet r, StatementContext ctx) throws SQLException
      {
        return new PartEntity(r.getInt("id"), r.getString("name"));
      }
    }
}