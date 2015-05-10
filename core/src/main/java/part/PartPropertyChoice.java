package part;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class PartPropertyChoice {

    @JsonProperty("name")
    String name;

    @JsonProperty("value")
    String value;

    @JsonProperty("description")
    String description;


    /**
     * default constructor for Jackson
     */
    public PartPropertyChoice() {}

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "PartPropertyChoice{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
