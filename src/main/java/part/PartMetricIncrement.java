package part;


import com.fasterxml.jackson.annotation.JsonProperty;
import util.Unit;

/**
 * decide if we are going to normalize of not
 * @author carl_downs
 *
 */
public class PartMetricIncrement {

    @JsonProperty("name")
    String name;
    
    /**
     * delta weight in milligrams
     */
    @JsonProperty("addWeight")
    String addWeight;
    
    /**
     * height in millimeters
     */
    @JsonProperty("addHeight")
    String addHeight;
    
    /**
     * length in millimeters
     */
    @JsonProperty("addLength")
    String addLength;

    /**
     * width in millimeters
     */
    @JsonProperty("addWidth")
    String addWidth;

    /**
     * default constructor for Jackson
     */
    public PartMetricIncrement() {}

    PartMetricIncrement(String name, String addWeight, String addHeight, String addLength, String addWidth) {
        this.name = name;
        this.addWeight = Unit.assertWeightType(addWeight);
        this.addHeight = Unit.assertLengthType(addHeight);
        this.addLength = Unit.assertLengthType(addLength);
        this.addWidth  = Unit.assertLengthType(addWidth);

    }
}
