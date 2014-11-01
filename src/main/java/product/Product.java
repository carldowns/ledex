package product;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * represents a set of ProductPart table rows.
 */
public class Product {

    private String productID;
    private List<ProductPart> pParts = Lists.newArrayList();

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void addPart (ProductPart pPart) {
        pParts.add(pPart);
    }

    public List<ProductPart> getParts() {
        return pParts;
    }

    public void setParts(List<ProductPart> pParts) {
        this.pParts = pParts;
    }
}
