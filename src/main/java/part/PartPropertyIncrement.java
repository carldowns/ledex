package part;


import com.fasterxml.jackson.annotation.JsonProperty;
import util.Unit;

/**
 * @author carl_downs
 *
 */
public class PartPropertyIncrement {
    
    /**
     *  minimum value allowed
     */
    @JsonProperty("incMin")
    String incMin;
    
    /**
     *  maximum units allowed
     */
    @JsonProperty("incMax")
    String incMax;
    
    /**
     *  increment must be wholly divisible by this number
     */
    @JsonProperty("incDiv")
    String incDiv;
        
    
    /**
     * Jackson
     */
    PartPropertyIncrement() {}

    PartPropertyIncrement(String min, String max) {
        this.incMin = Unit.assertLengthType(min);
        this.incMax = Unit.assertLengthType(max);
        this.incDiv = incMin;
    }

    PartPropertyIncrement(String min, String max, String div) {
        this.incMin = Unit.assertLengthType(min);
        this.incMax = Unit.assertLengthType(max);
        this.incDiv = Unit.assertLengthType(div);
    }

    public String getIncMin() {
        return incMin;
    }

    public void setIncMin(String incMin) {
        this.incMin = incMin;
    }

    public String getIncMax() {
        return incMax;
    }

    public void setIncMax(String incMax) {
        this.incMax = incMax;
    }

    public String getIncDiv() {
        return incDiv;
    }

    public void setIncDiv(String incDiv) {
        this.incDiv = incDiv;
    }
}
