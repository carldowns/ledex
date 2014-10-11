package model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("weight")
    String weight;
    
    /**
     * height in millimeters
     */
    @JsonProperty("height")
    String height;
    
    /**
     * length in millimeters
     */
    @JsonProperty("length")
    String length;

    /**
     * width in millimeters
     */
    @JsonProperty("width")
    String width;

    /**
     * means of assigning incremental metrics changes
     */
    @JsonProperty("increments")
    List<PartDimsIncrement> increments;

    /**
     * default constructor for Jackson
     */
    public PartDims() {}

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
