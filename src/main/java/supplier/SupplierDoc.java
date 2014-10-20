package supplier;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author carl_downs
 *
 */
public class SupplierDoc {
    private String id;
    private String json;
    private Timestamp ts;

    public SupplierDoc() {
    }

    public SupplierDoc(String id, String doc, Timestamp ts) {
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