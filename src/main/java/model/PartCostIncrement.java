package model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}
