package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import util.UnitConverter;

import java.util.ArrayList;
import java.util.List;

public class PartCost {

    @JsonProperty ("qty")
    Integer qty;

    @JsonProperty ("baseCost")
    String baseCost;

    @JsonProperty ("increments")
    List<PartCostIncrement> increments;

    /**
     * default constructor for Jackson
     */
    public PartCost() {}
    
    public PartCost (Integer quantity, String baseCost) {
        this.qty = quantity;
        this.baseCost = UnitConverter.assertMonetaryType(baseCost);
    }
    
    public void add(PartCostIncrement inc) {
        if (increments == null) {
            increments = new ArrayList<PartCostIncrement>();
        }
        increments.add(inc);
    }


}
