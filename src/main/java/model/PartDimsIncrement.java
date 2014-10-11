package model;


import com.fasterxml.jackson.annotation.JsonProperty;
import util.UnitConverter;

/**
 * decide if we are going to normalize of not
 * @author carl_downs
 *
 */
public class PartDimsIncrement {

    /**
     * 
     */
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
    public PartDimsIncrement() {}

    PartDimsIncrement (String name, String addWeight, String addHeight, String addLength, String addWidth) {
        this.name = name;
        this.addWeight = UnitConverter.assertWeightType(addWeight);
        this.addHeight = UnitConverter.assertLengthType(addHeight);
        this.addLength = UnitConverter.assertLengthType(addLength);
        this.addWidth  = UnitConverter.assertLengthType(addWidth);

    }
}
