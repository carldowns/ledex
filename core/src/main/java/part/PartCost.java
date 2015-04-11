package part;

import com.fasterxml.jackson.annotation.JsonProperty;
import util.Unit;

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
        this.baseCost = Unit.assertMonetaryType(baseCost);
    }
    
    public void add(PartCostIncrement inc) {
        if (increments == null) {
            increments = new ArrayList<PartCostIncrement>();
        }
        increments.add(inc);
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(String baseCost) {
        this.baseCost = baseCost;
    }

    public List<PartCostIncrement> getIncrements() {
        return increments;
    }

    public void setIncrements(List<PartCostIncrement> increments) {
        this.increments = increments;
    }
}
