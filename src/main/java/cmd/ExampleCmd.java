package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 *
 */
public class ExampleCmd extends BaseCmd {

    @JsonProperty
    private URI inputFilePath;

    @JsonProperty
    private String supplierID;

    public ExampleCmd() {
        super();
    }

    /**
     * possible pattern: commands contain the binding to the class that will handle the work.
     * The handler can be injected so we can see the effects of a mock / stand-in object.
     */
    public void exec () {
        //handler.exec (this);
    }
}
