package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 *
 */
public class ExportPartsCmd extends BaseCmd {

    @JsonProperty
    private URI outputFilePath;

    public ExportPartsCmd() {
        super();
    }

    public URI getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(URI outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}
