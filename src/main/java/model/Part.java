package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit representation:  we have chosen to support in-place specification of the 
 * currency, weights and measure units defining a part.  That means a specifier
 * can be tagged onto the end of the declaration itself.  We will convert from that as needed.
 * So a weight can be "5KG", "12LBS", "12 lbs", "5000 G", etc. 
 * @see UnitConverterOLD 
 * 
 * @author carl_downs
 *
 */
public class Part implements Cloneable {

    /**
     * Part name
     */
    @JsonProperty ("name")
    String name;
    
    /**
     * function in the set
     */
    @JsonProperty ("function")
    String function;
    
    /**
     * properties
     */
    @JsonProperty ("properties")
    List<PartProperty> properties = new ArrayList<PartProperty>();
    
    /**
     * quantity pricing
     */
    @JsonProperty ("costs")
    List<PartCost> costs = new ArrayList<PartCost>();
    
    /**
     * part dimensions, baseline.  These are a fixed set of values but can be 
     * modified incrementally 
     * @see PartDimsIncrement
     */
    PartDims dims;

    /////////////////////////
    // Constructors
    /////////////////////////

    /**
     * default constructor for Jackson
     */
    public Part () {
    }

    /**
     * copy constructor
     * @param source
     */
    public Part (Part source) {
       throw new UnsupportedOperationException ();    
    }
    
    public Part (String partName, String functionName) {
        this.name = partName;
        this.function = functionName;
    }


    /////////////////////////
    // Methods
    /////////////////////////

    public Part setDims (PartDims dims) {
       this.dims = dims; 
       return this;    
    }
    
    public Part addProperty(PartProperty property) {
        properties.add (property);
        return this;
    }

    public Part addCost(PartCost price) {
        costs.add (price);
        return this;
    }

    public String getName () {
        return name;       
    }
    
    
    public String getFunction() {
        return function;
    }

    
    public void setFunction(String function) {
        this.function = function;
    }

    
    public List<PartProperty> getProperties() {
        return properties;
    }

    
    public void setProperties(List<PartProperty> properties) {
        this.properties = properties;
    }

    
    public List<PartCost> getCosts() {
        return costs;
    }

    
    public void setCosts(List<PartCost> costs) {
        this.costs = costs;
    }

    
    public PartDims getDimensions() {
        return dims;
    }

    
    public void setDimensions(PartDims dimensions) {
        this.dims = dimensions;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Part clone () {
        throw new UnsupportedOperationException ();    
    }

    @Override
    public String toString () {
        return "name:" + name + " function:" + function;
    }
}
