package part;

import com.fasterxml.jackson.annotation.JsonProperty;
import util.UnitConverter;

public class PartCostIncrement {

    @JsonProperty("type")
    PartPropertyType type;

    @JsonProperty ("name")
    String name;

    @JsonProperty ("addCost")
    String addCost;

    /**
     * default constructor for Jackson
     */
    public PartCostIncrement() {}

    PartCostIncrement (String type, String addCost) {
        this.type = PartPropertyType.valueOf(type);
        this.addCost = UnitConverter.assertMonetaryType(addCost);
    }
    
    PartCostIncrement (String type, String name, String addCost) {
        this (type, addCost);
        this.name = name;
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

    public String getAddCost() {
        return addCost;
    }

    public void setAddCost(String addCost) {
        this.addCost = addCost;
    }
}
