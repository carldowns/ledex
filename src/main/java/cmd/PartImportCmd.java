package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class PartImportCmd extends Cmd {

    @JsonProperty
    private String inputFilePath;

    @JsonProperty
    private String supplierID;

    public PartImportCmd() {
        super();
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
}
