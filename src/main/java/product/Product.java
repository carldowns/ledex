package product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * represents a set of ProductPart table rows.
 *
 * <p>The combined set of ProductPart rows for a given productID makes up a product definition in the catalog.
 * When configured into a ProductItem, all variables are expressed to quantify a concrete product that can be
 * cost-analysed, priced, and quoted and ordered</p>
 *
 *
 *
 */
public class Product {

    @JsonProperty
    private String productID;

    @JsonProperty
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

    @Override
    public String toString() {
        return "Product{" +
                "productID='" + productID + '\'' +
                ", pParts=" + pParts +
                '}';
    }
}
