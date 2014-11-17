package quote;

import com.fasterxml.jackson.annotation.JsonProperty;
import part.Part;
import part.PartProperty;
import product.ProductPart;

import java.util.List;

/**
 *
 */
public class Quote {

    @JsonProperty("projectName")
    String projectName;

    @JsonProperty("customerID")
    String customerID;

    @JsonProperty("items")
    List<QuoteLineItem> items;

    /**
     * represents a single line item in the quote
     * may be either a product or service
     */
    public static class QuoteLineItem {

        @JsonProperty("quantity")
        String quantity;

        @JsonProperty("product")
        QuotedProduct product; // product's parts aggregated into one line item.

        // @JsonProperty("service")
        // QuotedService service;

        @JsonProperty("price")
        QuotedBuyerPrice price;  // aggregate line item pricing (retail -buyer visibility ONLY)

        @JsonProperty("cost")
        QuotedSupplierCost cost; // aggregate line item costs (wholesale)
    }

    /**
     * represents a quoted product which is a collection of quoted parts
     */
    public static class QuotedProduct {

        @JsonProperty("parts")
        List<QuotedPart> parts;

        @JsonProperty("metrics")
        QuotedMetric metrics; // aggregate metrics for this product
    }

    /**
     * represents one of the parts of a given quoted product.
     */
    public static class QuotedPart {

        @JsonProperty("quantity")
        String quantity; // may have N of a part, depending on linkable nature, connections, etc

        @JsonProperty("part")
        Part part; // original part description (immutable)

        @JsonProperty("productPart")
        ProductPart productPart; // original catalog product part entry (immutable)

        @JsonProperty("selections")
        List<PartProperty> selections; // formerly incremental -- now selected properties
    }

    /**
     * represents the aggregate metrics for the part or product where it appears
     */
    public static class QuotedMetric {
        /**
         * weight in milligrams
         */
        @JsonProperty("weight")
        String weight;

        /**
         * height in millimeters
         */
        @JsonProperty("height")
        String height;

        /**
         * length in millimeters
         */
        @JsonProperty("length")
        String length;

        /**
         * width in millimeters
         */
        @JsonProperty("width")
        String width;
    }

    public static class QuotedBuyerPrice {
        @JsonProperty("value")
        String value;
    }

    public static class QuotedSupplierCost {
        @JsonProperty("value")
        String value;
    }

//    public static class QuotedService {
//
//        @JsonProperty("name")
//        String name;
//
//        @JsonProperty("type")
//        String type;
//
//        @JsonProperty("description")
//        String description;
//    }

}
