package catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import catalog.dao.CatalogPart;

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

    // maps ProductPart to quantity as a String to match system-wide representation
    // Linkable parts can be included in multiples so quantity is maintained per part

    @JsonProperty
    private Map<CatalogPart,String> catParts = Maps.newHashMap();

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void addPart (CatalogPart pPart) {
        String quantity = catParts.get(pPart);
        if (quantity == null) {
            catParts.put(pPart, "1");
        }
        else {
            catParts.put(pPart, Integer.toString(Integer.parseInt(quantity) + 1));
        }
    }

    public Set<CatalogPart> getParts() {
        return catParts.keySet();
    }

    public String getQuantityFor (CatalogPart pPart) {
        return catParts.get(pPart);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID='" + productID + '\'' +
                ", catParts=" + catParts +
                '}';
    }
}
