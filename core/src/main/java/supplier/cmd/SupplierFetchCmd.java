package supplier.cmd;

import cmd.Cmd;
import com.fasterxml.jackson.annotation.JsonProperty;
import supplier.Supplier;

/**
 *
 */
public class SupplierFetchCmd extends Cmd {

    @JsonProperty
    private String supplierID;

    @JsonProperty
    private Supplier supplier;

    public SupplierFetchCmd() {
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
