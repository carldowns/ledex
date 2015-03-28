package part.cmd;

import system.Cmd;
import com.fasterxml.jackson.annotation.JsonProperty;
import part.Part;

/**
 *
 */
public class PartFetchCmd extends Cmd {

    @JsonProperty
    private String partID;

    @JsonProperty
    private Part part;

    public PartFetchCmd() {
        super();
    }

    public String getPartID() {
        return partID;
    }

    public void setPartID(String partID) {
        this.partID = partID;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }
}
