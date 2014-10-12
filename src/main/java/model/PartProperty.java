package model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PartProperty {

    @JsonProperty("type")
    PartPropertyType type;

    @JsonProperty("name")
    String name;

    @JsonProperty("value")
    String value;

    @JsonProperty("skuLabel")
    String skuLabel;

    @JsonProperty("description")
    String description;

    /**
     * Measurement constraint Typical measures are length, size, etc. measures
     * usually impact both price and dimensional weight as the increase
     */
    @JsonProperty("increment")
    PartPropertyIncrement increment;

    /**
     * default constructor for Jackson
     */
    public PartProperty() {}

    public PartProperty(String type, String name, String value) {
        this.type = PartPropertyType.valueOf(type);
        this.name = name;
        this.value = value;
    }

    public PartProperty(PartPropertyType type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public PartProperty(String type, String name, PartPropertyIncrement increment) {
        this.type = PartPropertyType.valueOf(type);
        this.name = name;
        this.increment = increment;
    }

    public PartPropertyType getType() {
        return type;
    }

    public void setType(PartPropertyType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSkuLabel() {
        return skuLabel;
    }

    public void setSkuLabel(String skuLabel) {
        this.skuLabel = skuLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartPropertyIncrement getIncrement() {
        return increment;
    }

    public void setIncrement(PartPropertyIncrement increment) {
        this.increment = increment;
    }

}
