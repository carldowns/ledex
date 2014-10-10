package model;



/**
 * @author carl_downs
 *
 */
public class PartIncrement {
    
    /**
     *  minimum value allowed
     */
    String incMin;
    
    /**
     *  maximum units allowed
     */
    String incMax;
    
    /**
     *  increment must be wholly divisible by this number
     */
    String incDiv;
        
    
    /**
     * sereialization
     */
    PartIncrement () {}
    
    PartIncrement (String min, String max) {
        this.incMin = UnitConverter.assertLengthType(min);
        this.incMax = UnitConverter.assertLengthType(max);
        this.incDiv = incMin;
    }
    
    PartIncrement (String min, String max, String div) {
        this.incMin = UnitConverter.assertLengthType(min);
        this.incMax = UnitConverter.assertLengthType(max);
        this.incDiv = UnitConverter.assertLengthType(div);
    }
}
