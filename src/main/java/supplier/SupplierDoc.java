package supplier;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents a row of the SupplierDoc table.
 *
 */
public class SupplierDoc {
    private String supplierID;
    private String supplierDocID;
    private String doc;
    private Timestamp ts;

    public SupplierDoc() {
    }

    public SupplierDoc(String supplierID, String docID, String doc, Timestamp ts) {
        this.supplierID = supplierID;
        this.supplierDocID = docID;
        this.doc = doc;
        this.ts = ts;
    }
    
    @JsonProperty
    public String getSupplierID() {
        return supplierID;
    }

    @JsonProperty
    public String getSupplierDocID() {
        return supplierDocID;
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