package supplier;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author carl_downs
 *
 */
public class SupplierDocEntity {
    private String id;
    private String doc;
    private Timestamp ts;

    public SupplierDocEntity() {
        // Jackson deserialization
    }

    public SupplierDocEntity(String id, String doc, Timestamp ts) {
        this.id = id;
        this.doc = doc;
        this.ts = ts;
    }
    
    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getDoc() {
        return doc;
    }
    
    @JsonProperty
    public Timestamp getTimestamp() {
        return ts;
    }
    
}