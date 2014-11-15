package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 *
 */
public class ExportSuppliersCmd extends BaseCmd {

    @JsonProperty
    private String outputFilePath;

    public ExportSuppliersCmd() {
        super();
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}
