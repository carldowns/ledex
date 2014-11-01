package supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * represents a row of the SupplierRec table
 */
@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public class SupplierRec {

    @JsonProperty ("supplierID")
    private String supplierID;

    @JsonProperty ("name")
    private String name;

    public SupplierRec() {
    }

    public SupplierRec(String id, String name) {
        this.supplierID = id;
        this.name = name;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String id)
    {
        this.supplierID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}