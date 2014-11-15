package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class ExportAssembliesCmd extends BaseCmd {

    @JsonProperty
    private String outputFilePath;

    public ExportAssembliesCmd() {
        super();
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}
