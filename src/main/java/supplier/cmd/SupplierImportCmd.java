package supplier.cmd;

import system.Cmd;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class SupplierImportCmd extends Cmd {

    @JsonProperty
    private String inputFilePath;

    public SupplierImportCmd() {
        super();
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
}
