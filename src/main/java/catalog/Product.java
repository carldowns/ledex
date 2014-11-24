package catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import catalog.dao.CatalogPart;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * represents a set of ProductPart table rows.
 *
 * <p>The combined set of ProductPart rows for a given productID makes up a product definition in the catalog.
 * When configured into a ProductItem, all variables are expressed to quantify a concrete product that can be
 * priced, quoted and ordered</p>
 */
public class Product {

    @JsonProperty
    private String productID;

    @JsonProperty
    private Set<CatalogPart> catParts = Sets.newHashSet();

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void addPart (CatalogPart catPart) {
        catParts.add(catPart);
    }

    public Set<CatalogPart> getParts() {
        return catParts;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID='" + productID + '\'' +
                ", parts=" + catParts +
                '}';
    }
}
