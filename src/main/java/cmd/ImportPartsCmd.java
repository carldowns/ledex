package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * Imports Json Part and Assembly files
 */
public class ImportPartsCmd extends BaseCmd {

    @JsonProperty
    private URI inputFilePath;

    @JsonProperty
    private String supplierID;

    public ImportPartsCmd() {
        super("catalog", "importParts", "started");
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
