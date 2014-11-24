package quote;

import catalog.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import part.PartProperty;
import catalog.dao.CatalogPart;
import part.PartPropertyType;

import java.util.List;
import java.util.Map;

/**
 * Represents a quote based on a catalog product.
 * Purely REST object.  No processing logic here.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Quote {

    @JsonProperty("projectName")
    public String projectName;

    @JsonProperty("customerID")
    public String customerID;

    @JsonProperty("items")
    public List<LineItem> items = Lists.newArrayList();


    public Quote.LineItem addLineItem (String quantity, Product product) {

        Quote.LineItem qItem = new Quote.LineItem();
        items.add(qItem);

        Quote.QuoteProduct qProduct = new Quote.QuoteProduct();
        qItem.quotedProduct = qProduct;
        qItem.quantity = quantity;

        for (CatalogPart catalogPart : product.getParts()) {
            Quote.QuotePart quotePart = new Quote.QuotePart();
            quotePart.catalogPart = catalogPart;
            quotePart.quantity = "1";
            qProduct.quotedParts.add(quotePart);
        }

        return qItem;
    }

    /**
     * represents a single line item in the quote
     * may be either a product or service
     */
    public static class LineItem {

        @JsonProperty("quantity")
        public String quantity;

        @JsonProperty("product")
        public QuoteProduct quotedProduct; // product's parts aggregated into one line item.

        @JsonProperty("service")
        public QuoteService quotedService;

        @JsonProperty("price")
        public QuotePrice quotedPrice;  // aggregate line item pricing (retail -buyer visibility ONLY)

        @JsonProperty("cost")
        public QuoteCost quotedCost; // aggregate line item costs (wholesale)
    }

    /**
     * represents a quoted product which is a collection of quoted parts
     */
    public static class QuoteProduct {

        @JsonProperty("parts")
        public List<QuotePart> quotedParts = Lists.newArrayList();

        @JsonProperty("metrics")
        public QuoteMetric quotedMetrics; // aggregate metrics for this product (single unit)

        public QuotePart getPart (String partID) {
            for (QuotePart qPart : quotedParts) {
                if (qPart.catalogPart.getPartID().equals(partID)) {
                    return qPart;
                }
            }
            return null;
        }
    }

    /**
     * represents one of the parts of a given quoted product.
     */
    public static class QuotePart {

        // may have 1 or N of a given part, depending on linkable nature, connections, etc
        @JsonProperty("quantity")
        public String quantity;

        // original part description (immutable)
        @JsonProperty("part")
        public part.Part part;

        // original catalog product part entry (immutable)
        @JsonProperty("productPart")
        public CatalogPart catalogPart;

        // combined part ID + selection modifiers
        @JsonProperty("label")
        public String label;

        // combined list of attribute value pairs
        @JsonProperty("description")
        public String description;

        // selected properties corresponding to PartProperty entries that have selectors or increments
        @JsonProperty("selections")
        public List<QuoteSelection> selections = Lists.newArrayList();

        public QuoteSelection setSelection (PartPropertyType type, String value) {
            QuoteSelection selection = new QuoteSelection();
            selection.type = type;
            selection.value = value;
            selections.add(selection);
            return selection;
        }
    }

    /**
     * represents the aggregate metrics for the part or product where it appears
     */
    public static class QuoteMetric {
        /**
         * weight in milligrams
         */
        @JsonProperty("weight")
        public String weight;

        /**
         * height in millimeters
         */
        @JsonProperty("height")
        public String height;

        /**
         * length in millimeters
         */
        @JsonProperty("length")
        public String length;

        /**
         * width in millimeters
         */
        @JsonProperty("width")
        public String width;
    }

    public static class QuotePrice {

        @JsonProperty("value")
        public String value;
    }

    public static class QuoteCost {

        @JsonProperty("value")
        public String value;
    }

    public static class QuoteService {

        @JsonProperty("name")
        public String name;

        @JsonProperty("type")
        public String type;

        @JsonProperty("description")
        public String description;
    }

    public static class QuoteSelection {
        // try to get rid of 'name' as a key
        //        @JsonProperty("name")
        //        public String name;

        @JsonProperty("type")
        public PartPropertyType type;

        @JsonProperty("value")
        public String value;
    }
}
