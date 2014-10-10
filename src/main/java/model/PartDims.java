package model;

import java.util.ArrayList;
import java.util.List;

/**
 * All dimensions are normalized to metric base.
 * @author carl_downs
 */
public class PartDims {

    /**
     * weight in milligrams
     */
    String weight;
    
    /**
     * height in millimeters
     */
    String height;
    
    /**
     * length in millimeters
     */
    String length;

    /**
     * width in millimeters
     */
    String width;

    List<PartDimsIncrement> increments;
    
    PartDims(String weight, String height, String length, String width) {
        setWeight(weight);
        setHeight(height);
        setLength(length);
        setWidth(width);
    }

    void setWeight(String measure) {
        weight = UnitConverter.assertWeightType(measure);
    }

    void setHeight(String measure) {
        height =UnitConverter.assertLengthType(measure);
    }

    void setLength(String measure) {
        length = UnitConverter.assertLengthType(measure);
    }

    void setWidth(String measure) {
        width = UnitConverter.assertLengthType(measure);
    }

    PartDims add (PartDimsIncrement increment) {
        if (increments == null) {
            increments = new ArrayList<PartDimsIncrement>();
        }
        
        increments.add(increment);
        return this;
    }
}
