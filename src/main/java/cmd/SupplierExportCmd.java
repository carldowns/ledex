package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class SupplierExportCmd extends Cmd {

    @JsonProperty
    private String outputFilePath;

    public SupplierExportCmd() {
        super();
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}
