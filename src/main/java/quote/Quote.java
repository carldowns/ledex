package quote;

import catalog.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import catalog.dao.CatalogPart;
import part.PartPropertyType;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public String toString() {
        return "Quote{" +
                "projectName='" + projectName + '\'' +
                ", customerID='" + customerID + '\'' +
                ", items=" + items +
                '}';
    }

    /**
     * represents a single line item in the quote
     * may be either a product or service
     */
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class LineItem {

        @JsonProperty("quantity")
        public String quantity;

        @JsonProperty("quotedCost")
        public QuoteCost quotedCost; // aggregate line item costs (wholesale)

        @JsonProperty("totalCost")
        public QuoteCost totalCost; // quoted cost * line item quantity = total cost for the line item

        @JsonProperty("quotedPrice")
        public QuotePrice quotedPrice;  // aggregate line item pricing (retail -buyer visibility ONLY)

        @JsonProperty("totalPrice")
        public QuotePrice totalPrice;  // quoted price * line item quantity = total price for the line item

        @JsonProperty("quotedProduct")
        public QuoteProduct quotedProduct; // product's parts aggregated into one line item.

        @JsonProperty("quotedService")
        public QuoteService quotedService;

        @JsonProperty("calculations")
        public List<QuoteNote> calculations = new ArrayList<>();

        @Override
        public String toString() {
            return "LineItem{" +
                    "quantity='" + quantity + '\'' +
                    ", quotedProduct=" + quotedProduct +
                    ", quotedService=" + quotedService +
                    ", quotedPrice=" + quotedPrice +
                    ", quotedCost=" + quotedCost +
                    ", calculations=" + calculations +
                    '}';
        }
    }

    /**
     * represents a quoted product which is a collection of quoted parts
     */
    @JsonIgnoreProperties(ignoreUnknown=true)
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

        @Override
        public String toString() {
            return "QuoteProduct{" +
                    "quotedParts=" + quotedParts +
                    ", quotedMetrics=" + quotedMetrics +
                    '}';
        }
    }

    /**
     * represents one of the parts of a given quoted product.
     */
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class QuotePart {

        // configured sku label, comprised of part properties, choices, selections
        @JsonProperty("cfgLabel")
        public String cfgLabel;

        @JsonProperty("cfgDescription")
        public String cfgDescription;

        // may have 1 or N of a given part, depending on linkable nature, connections, etc
        @JsonProperty("quantity")
        public String quantity;

        @JsonProperty("quotedCost")
        // aggregate base + incremental part costs
        public QuoteCost quotedCost;

        // original part description (immutable)
        @JsonProperty("part")
        public part.Part part;

        // original catalog product part entry (immutable)
        @JsonProperty("productPart")
        public CatalogPart catalogPart;

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

        @Override
        public String toString() {
            return "QuotePart{" +
                    "quantity='" + quantity + '\'' +
                    ", part=" + part +
                    ", catalogPart=" + catalogPart +
                    ", selections=" + selections +
                    '}';
        }
    }

    /**
     * represents the aggregate metrics for the part or product where it appears
     */
    @JsonIgnoreProperties(ignoreUnknown=true)
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

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class QuotePrice {

        public QuotePrice (String value) {
            this.value = value;
        }

        @JsonProperty("value")
        public String value;

        @Override
        public String toString() {
            return "QuotePrice{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class QuoteCost {

        @JsonProperty("value")
        public String value;

        @Override
        public String toString() {
            return "QuoteCost{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class QuoteService {

        @JsonProperty("name")
        public String name;

        @JsonProperty("type")
        public String type;

        @JsonProperty("description")
        public String description;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class QuoteSelection {
        @JsonProperty("name")
        public String name;

        @JsonProperty("type")
        public PartPropertyType type;

        @JsonProperty("value")
        public String value;

        @Override
        public String toString() {
            return "QuoteSelection{" +
                    "name='" + name + '\'' +
                    ", type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class QuoteNote {

        @JsonProperty("type")
        public String type;

        @JsonProperty("type")
        public String value;

        @JsonProperty("treatment")
        public String treatment;

        @Override
        public String toString() {
            return "QuoteNote{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    ", treatment='" + treatment + '\'' +
                    '}';
        }
    }
}
