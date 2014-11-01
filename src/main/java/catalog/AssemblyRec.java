package catalog;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a row of the AssemblyRec table.
 * Assembly definitions are composed in Json and uploaded.
 */
public class AssemblyRec {

    @JsonProperty("assemblyID")
    private String assemblyID;

    @JsonProperty ("name")
    private String name;

    public AssemblyRec(String assemblyID, String name) {
        this.assemblyID = assemblyID;
        this.name = name;
    }

    public String getAssemblyID() {
        return assemblyID;
    }

    public void setAssemblyID(String assemblyID) {
        this.assemblyID = assemblyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
