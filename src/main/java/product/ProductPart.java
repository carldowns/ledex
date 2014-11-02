package product;


import catalog.Function;
import catalog.FunctionType;

/**
 * Represents a row of the ProductPart table.
 *
 * <h3>Linkable Declaration</h3>
 *
 * <p>Linkable parts have the ability to be daisy-chained together.  This makes it possible to configure any number
 * of the part within a given ProductItem subject to other Rule restrictions.</p>
 *
 * <p>As an example, Users can configure a Light Set to have 2,3,4,5 etc., pcs of a given light strip part
 * provided the part is declared as linkable.</p>
 *
 * <p>If multiple Parts for a given Function are Linkable, they can be mixed and matched in multiples.</p>
 *
 */
public class ProductPart {

    private String productID;

    private String partID;
    private String partDocID;
    private FunctionType function;

    private String assemblyID;
    private String assemblyDocID;

    private Boolean linkable;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

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

    public FunctionType getFunction() {
        return function;
    }

    public void setFunction(FunctionType function) {
        this.function = function;
    }

    public String getAssemblyID() {
        return assemblyID;
    }

    public void setAssemblyID(String assemblyID) {
        this.assemblyID = assemblyID;
    }

    public String getAssemblyDocID() {
        return assemblyDocID;
    }

    public void setAssemblyDocID(String assemblyDocID) {
        this.assemblyDocID = assemblyDocID;
    }

    public Boolean getLinkable() {
        return linkable;
    }

    public void setLinkable(Boolean linkable) {
        this.linkable = linkable;
    }
}
