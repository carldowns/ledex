package part;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import catalog.FunctionType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Unit representation:  we have chosen to support in-place specification of the
 * currency, weights and measure units defining a part.  That means a specifier
 * can be tagged onto the end of the declaration itself.  We will convert from that as needed.
 * So a weight can be "5KG", "12LBS", "12 lbs", "5000 G", etc.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Part {

    @JsonProperty("partID")
    String partID;

    @JsonIgnore
    String partDocID;

    @JsonProperty("supplierID")
    String supplierID;

    @JsonProperty("name")
    String name;

    @JsonProperty("skuLabel")
    String skuLabel;

    @JsonProperty("function")
    FunctionType function;

    @JsonProperty("connections")
    List<PartConnection> connections = new ArrayList<>();

    @JsonProperty("properties")
    List<PartProperty> properties = new ArrayList<>();

    @JsonProperty("costs")
    List<PartCost> costs = new ArrayList<>();

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

    public String getSkuLabel() {
        return skuLabel;
    }

    public void setSkuLabel(String skuLabel) {
        this.skuLabel = skuLabel;
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
    // Connections
    /////////////////////////

    public List<PartConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<PartConnection> connections) {
        this.connections = connections;
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

    /**
     * part has two connections of same type but opposite gender
     */

    public boolean isLinkable () {
        Map<String,PartConnection> linkable = Maps.newHashMap();
        for (PartConnection connection : connections) {
            PartConnection match = linkable.get(connection.getType());
            if (match != null && !connection.isSameGender(match))
                return true;

            linkable.put(connection.getType(), connection);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Part)) return false;

        Part part = (Part) o;

        if (!partID.equals(part.partID))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return partID.hashCode();
    }
}
