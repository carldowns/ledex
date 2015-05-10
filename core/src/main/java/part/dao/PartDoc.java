package part.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 */
public class PartDoc {
    private String partID;
    private String partDocID;
    private String doc;
    private Timestamp ts;

    public PartDoc() {
    }

    public PartDoc(String partID, String partDocID, String doc, Timestamp ts) {
        this.partID = partID;
        this.partDocID = partDocID;
        this.doc = doc;
        this.ts = ts;
    }
    
    @JsonProperty
    public String getPartID() {
        return partID;
    }

    @JsonProperty
    public String getDoc() {
        return doc;
    }

    @JsonProperty
    public String getPartDocID() {
        return partDocID;
    }

    @JsonProperty
    public Timestamp getTimestamp() {
        return ts;
    }

    @Override
    public String toString() {
        return "PartDoc{" +
                "partDocID='" + partDocID + '\'' +
                ", partID='" + partID + '\'' +
                ", ts=" + ts +
                '}';
    }
}