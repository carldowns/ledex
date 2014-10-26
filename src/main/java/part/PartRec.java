package part;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * represents the partrec table in the database
 */
@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public class PartRec {

    @JsonProperty ("partID")
    private String partID;

    @JsonProperty ("supplierID")
    private String supplierID;

    @JsonProperty ("name")
    private String name;

    @JsonProperty ("function")
    private String function;

    public PartRec(String partID, String supplierID, String name, String function) {
        this.partID = partID;
        this.supplierID = supplierID;
        this.name = name;
        this.function = function;
    }

    public String getPartId() {
        return partID;
    }

    public void setPartId(String partID)
    {
        this.partID = partID;
    }

    public String getSupplierId() {
        return supplierID;
    }

    public void setSupplierId(String supplierID)
    {
        this.supplierID = supplierID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}