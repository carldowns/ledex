package catalog.cmd;

import system.Cmd;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class AssemblyExportCmd extends Cmd {

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
