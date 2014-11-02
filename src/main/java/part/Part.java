package part;

import com.fasterxml.jackson.annotation.JsonProperty;
import catalog.FunctionType;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit representation:  we have chosen to support in-place specification of the
 * currency, weights and measure units defining a part.  That means a specifier
 * can be tagged onto the end of the declaration itself.  We will convert from that as needed.
 * So a weight can be "5KG", "12LBS", "12 lbs", "5000 G", etc.
 */
public class Part {

    @JsonProperty("partID")
    String partID;

    // not persisted
    String partDocID;

    @JsonProperty("supplierID")
    String supplierID;

    @JsonProperty("name")
    String name;

    @JsonProperty("function")
    FunctionType function;

    @JsonProperty("properties")
    List<PartProperty> properties = new ArrayList<PartProperty>();

    @JsonProperty("costs")
    List<PartCost> costs = new ArrayList<PartCost>();

    @JsonProperty("metrics")
    PartMetric metrics;

    /////////////////////////
    // Constructors
    /////////////////////////

    /**
     * default constructor for Jackson
     */
    public Part() {
    }

    /**
     * copy constructor
     *
     * @param source
     */
    public Part(Part source) {
        throw new UnsupportedOperationException();
    }

    public Part(String partName, String functionName) {
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

    public String getPartDocID() {
        return partDocID;
    }

    public void setPartDocID(String partDocID) {
        this.partDocID = partDocID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /////////////////////////
    // Function
    /////////////////////////

    public String getFunctionName() {
        return function.name();
    }

    public FunctionType getFunctionType() {
        return function;
    }

    public void setFunction(String function) {
        this.function = FunctionType.valueOf(function);
    }


    /////////////////////////
    // Property
    /////////////////////////


    public Part addProperty(PartProperty property) {
        properties.add(property);
        return this;
    }

    public List<PartProperty> getProperties() {
        return properties;
    }

    public List<PartProperty> getPropertiesOfType(PartPropertyType type) {
        List<PartProperty> results = Lists.newArrayList();
        for (PartProperty pp : properties) {
            if (pp.getType().equals(type)) {
                results.add(pp);
            }
        }
        return results;
    }

    public void setProperties(List<PartProperty> properties) {
        this.properties = properties;
    }

    public boolean isLinkable () {
        for (PartProperty pp : properties) {
            if (pp.getType() == PartPropertyType.LINKABLE)
                return true;
        }
        return false;
    }

    /////////////////////////
    // Cost
    /////////////////////////


    public Part addCost(PartCost price) {
        costs.add(price);
        return this;
    }

    public List<PartCost> getCosts() {
        return costs;
    }

    public void setCosts(List<PartCost> costs) {
        this.costs = costs;
    }


    /////////////////////////
    // Metric
    /////////////////////////

    public Part setMetrics(PartMetric metrics) {
        this.metrics = metrics;
        return this;
    }

    public PartMetric getMetric() {
        return metrics;
    }

    public void setMetric(PartMetric metric) {
        this.metrics = metric;
    }

    /////////////////////////
    // Overrides
    /////////////////////////

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
