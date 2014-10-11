package supplier;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST Representation for a Supplier
 * @author carl_downs
 *
 */
public class SupplierEntity {
    private String id;
    private String name;

    public SupplierEntity() {
        // Jackson deserialization
    }

    public SupplierEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public void setId(String id)
    {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name)
    {
        this.name = name;
    }

}