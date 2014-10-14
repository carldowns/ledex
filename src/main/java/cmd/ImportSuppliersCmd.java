package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * Imports Json Part and Assembly files
 */
public class ImportSuppliersCmd extends BaseCmd {

    @JsonProperty
    private URI inputFilePath;

    public ImportSuppliersCmd() {
        super("supplier", "importSuppliers", "started");
    }

    public URI getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(URI inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
}
