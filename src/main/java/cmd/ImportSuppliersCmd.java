package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * Imports Json Part and Assembly files
 */
public class ImportSuppliersCmd extends BaseCmd {

    @JsonProperty
    private String inputFilePath;

    public ImportSuppliersCmd() {
        super();
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
}
