package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class ImportAssembliesCmd extends BaseCmd {

    @JsonProperty
    private String inputFilePath;

    @JsonProperty
    private String assemblyID;

    public ImportAssembliesCmd() {
        super();
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getAssemblyID() {
        return assemblyID;
    }

    public void setAssemblyID(String supplierID) {
        this.assemblyID = assemblyID;
    }
}
