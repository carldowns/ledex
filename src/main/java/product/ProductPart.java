package product;


import catalog.Function;
import catalog.FunctionType;

/**
 * represents a row of the ProductPart table
 */
public class ProductPart {

    private String productID;

    private String partID;
    private String partDocID;
    private FunctionType function;

    private String assemblyID;
    private String assemblyDocID;

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
}
