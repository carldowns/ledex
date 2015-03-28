package part.cmd;

import system.Cmd;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class PartExportCmd extends Cmd {

    @JsonProperty
    private String outputFilePath;

    public PartExportCmd() {
        super();
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}
