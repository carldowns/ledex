package catalog.cmd;

import catalog.Assembly;
import cmd.Cmd;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class AssemblyFetchCmd extends Cmd {

    @JsonProperty
    private String assemblyID;

    @JsonProperty
    private Assembly assembly;

    public AssemblyFetchCmd() {
        super();
    }

    public String getAssemblyID() {
        return assemblyID;
    }

    public void setAssemblyID(String assemblyID) {
        this.assemblyID = assemblyID;
    }

    public Assembly getAssembly() {
        return assembly;
    }

    public void setAssembly(Assembly assembly) {
        this.assembly = assembly;
    }
}
