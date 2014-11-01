package part;

import com.fasterxml.jackson.annotation.JsonProperty;
import catalog.FunctionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit representation:  we have chosen to support in-place specification of the
 * currency, weights and measure units defining a part.  That means a specifier
 * can be tagged onto the end of the declaration itself.  We will convert from that as needed.
 * So a weight can be "5KG", "12LBS", "12 lbs", "5000 G", etc.
 */
public class Part {

    @JsonProperty ("partID")
    String partID;

    @JsonProperty ("supplierID")
    String supplierID;

    @JsonProperty ("name")
    String name;

    @JsonProperty ("function")
    FunctionType function;

    @JsonProperty ("properties")
    List<PartProperty> properties = new ArrayList<PartProperty>();

    @JsonProperty ("costs")
    List<PartCost> costs = new ArrayList<PartCost>();

    @JsonProperty ("metrics")
    PartMetric metrics;

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
        this.function = FunctionType.valueOf(functionName);
    }

    /////////////////////////////
    // Part Identity
    /////////////////////////////


    public String getPartID() {
        return partID;
    }

    public void setPartID(String partID) {
        this.partID = partID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    /////////////////////////
    // Methods
    /////////////////////////

    public Part setMetrics(PartMetric metrics) {
       this.metrics = metrics;
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
        return function.name();
    }

    
    public void setFunction(String function) {
        this.function = FunctionType.valueOf(function);
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

    
    public PartMetric getMetric() {
        return metrics;
    }

    
    public void setMetric(PartMetric metric) {
        this.metrics = metric;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Part{" +
                "partID='" + partID + '\'' +
                ", supplierID='" + supplierID + '\'' +
                ", name='" + name + '\'' +
                ", function=" + function +
//                ", properties=" + properties +
//                ", costs=" + costs +
//                ", metrics=" + metrics +
                '}';
    }
}
