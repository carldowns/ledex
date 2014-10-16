package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * Imports Json Part and Assembly files
 */
public class ExportSuppliersCmd extends BaseCmd {

    @JsonProperty
    private URI outputFilePath;

    public ExportSuppliersCmd() {
        super("supplier", "exportSuppliers", "started");
    }

    public URI getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(URI outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}