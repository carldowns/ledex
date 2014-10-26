package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;
import part.Part;

/**
 *
 */
public class GetPartCmd extends BaseCmd {

    @JsonProperty
    private String partID;

    @JsonProperty
    private Part part;

    public GetPartCmd() {
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
