package part;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * @author carl_downs
 *
 */
public class PartDoc {
    private String id;
    private String json;
    private Timestamp ts;

    public PartDoc() {
    }

    public PartDoc(String id, String doc, Timestamp ts) {
        this.id = id;
        this.json = doc;
        this.ts = ts;
    }
    
    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getJson() {
        return json;
    }
    
    @JsonProperty
    public Timestamp getTimestamp() {
        return ts;
    }
    
}