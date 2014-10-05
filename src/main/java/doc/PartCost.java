package doc;

import java.util.ArrayList;
import java.util.List;

public class PartCost {

    Integer qty;
    String baseCost;
    List<PartCostIncrement> increments;

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
