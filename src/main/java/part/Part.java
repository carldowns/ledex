package part;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * REST Representation for a Supplier
 * @author carl_downs
 *
 */
@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public class Part {

    @JsonProperty ("partID")
    private String id;

    @JsonProperty ("name")
    private String name;


    public Part() {
    }

    public Part(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}