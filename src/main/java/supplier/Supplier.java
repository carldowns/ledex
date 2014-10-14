package supplier;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST Representation for a Supplier
 * @author carl_downs
 *
 */
public class Supplier {

    @JsonProperty ("supplierID")
    private String id;

    @JsonProperty ("name")
    private String name;

    public Supplier() {
    }

    public Supplier(String id, String name) {
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