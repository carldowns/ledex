package model;

public class PartCostIncrement {

    PartPropertyType type;
    String name;
    String addCost;
    
    PartCostIncrement (String type, String addCost) {
        this.type = PartPropertyType.valueOf(type);
        this.addCost = UnitConverter.assertMonetaryType(addCost);
    }
    
    PartCostIncrement (String type, String name, String addCost) {
        this (type, addCost);
        this.name = name;
    }
}
