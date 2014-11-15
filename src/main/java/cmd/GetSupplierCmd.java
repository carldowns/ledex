package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;
import supplier.Supplier;

/**
 *
 */
public class GetSupplierCmd extends BaseCmd {

    @JsonProperty
    private String supplierID;

    @JsonProperty
    private Supplier supplier;

    public GetSupplierCmd() {
        super();
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
