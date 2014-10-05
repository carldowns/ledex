package diary;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST Representation for a Person
 * @author carl_downs
 *
 */
public class PersonRep {
    private long id;

    private String name;

    public PersonRep() {
        // Jackson deserialization
    }

    public PersonRep(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
    
}