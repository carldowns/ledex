package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class AssemblyExportCmd extends AbstractBaseCmd {

    @JsonProperty
    private String outputFilePath;

    public AssemblyExportCmd() {
        super();
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}
