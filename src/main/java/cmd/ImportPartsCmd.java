package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 *
 */
public class ImportPartsCmd extends BaseCmd {

    @JsonProperty
    private URI inputFilePath;

    @JsonProperty
    private String supplierID;

    public ImportPartsCmd() {
        super();
    }

    public URI getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(URI inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
}
