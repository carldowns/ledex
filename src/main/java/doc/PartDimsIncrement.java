package doc;


/**
 * decide if we are going to normalize of not
 * @author carl_downs
 *
 */
public class PartDimsIncrement {

    /**
     * 
     */
    String name;
    
    /**
     * delta weight in milligrams
     */
    String addWeight;
    
    /**
     * height in millimeters
     */
    String addHeight;
    
    /**
     * length in millimeters
     */
    String addLength;

    /**
     * width in millimeters
     */
    String addWidth;
    
    PartDimsIncrement (String name, String addWeight, String addHeight, String addLength, String addWidth) {
        this.name = name;
        this.addWeight = UnitConverter.assertWeightType(addWeight);
        this.addHeight = UnitConverter.assertLengthType(addHeight);
        this.addLength = UnitConverter.assertLengthType(addLength);
        this.addWidth  = UnitConverter.assertLengthType(addWidth);

    }
}
