package catalog;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Represents a row of the AssemblyDoc table.
 * Assembly definitions are composed in Json and uploaded to this table.
 * We maintain a history of changes to assemblies over time.
 */
public class AssemblyDoc {

    @JsonProperty ("assemblyID")
    private String assemblyID;

    @JsonProperty ("assemblyDocID")
    private String assemblyDocID;

    @JsonProperty ("doc")
    private String doc;

    @JsonProperty ("ts")
    private Timestamp ts;

    public AssemblyDoc() {
    }

    public AssemblyDoc(String partID, String partDocID, String doc, Timestamp ts) {
        this.assemblyID = partID;
        this.assemblyDocID = partDocID;
        this.doc = doc;
        this.ts = ts;
    }

    public String getAssemblyID() {
        return assemblyID;
    }

    public String getAssemblyDocID() {
        return assemblyDocID;
    }

    public String getDoc() {
        return doc;
    }

    public Timestamp getTimestamp() {
        return ts;
    }
}
