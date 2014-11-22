package quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import part.PartProperty;
import catalog.dao.CatalogPart;

import java.util.List;

/**
 * Represents a quote based on a catalog product.
 * Purely REST object.  No processing logic here.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Quote {

    @JsonProperty("projectName")
    String projectName;

    @JsonProperty("customerID")
    String customerID;

    @JsonProperty("items")
    List<LineItem> items = Lists.newArrayList();

    /**
     * represents a single line item in the quote
     * may be either a product or service
     */
    public static class LineItem {

        @JsonProperty("quantity")
        String quantity;

        @JsonProperty("product")
        QuoteProduct quotedProduct; // product's parts aggregated into one line item.

        @JsonProperty("service")
        QuoteService quotedService;

        @JsonProperty("price")
        QuotePrice quotedPrice;  // aggregate line item pricing (retail -buyer visibility ONLY)

        @JsonProperty("cost")
        QuoteCost quotedCost; // aggregate line item costs (wholesale)
    }

    /**
     * represents a quoted product which is a collection of quoted parts
     */
    public static class QuoteProduct {

        @JsonProperty("parts")
        List<QuotePart> quotedParts = Lists.newArrayList();

        @JsonProperty("metrics")
        QuoteMetric quotedMetrics; // aggregate metrics for this product (single unit)
    }

    /**
     * represents one of the parts of a given quoted product.
     */
    public static class QuotePart {

        @JsonProperty("quantity")
        String quantity; // may have N of a part, depending on linkable nature, connections, etc

        @JsonProperty("part")
        part.Part part; // original part description (immutable)

        @JsonProperty("productPart")
        CatalogPart catalogPart; // original catalog product part entry (immutable)

        @JsonProperty("label")
        String label; // combined part ID + selection modifiers

        @JsonProperty("description")
        String description; // combined list of attribute value pairs

        @JsonProperty("selections")
        List<PartProperty> selections = Lists.newArrayList(); // formerly incremental -- now selected properties
    }

    /**
     * represents the aggregate metrics for the part or product where it appears
     */
    public static class QuoteMetric {
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

    public static class QuotePrice {
        @JsonProperty("value")
        String value;
    }

    public static class QuoteCost {
        @JsonProperty("value")
        String value;
    }

    public static class QuoteService {

        @JsonProperty("name")
        String name;

        @JsonProperty("type")
        String type;

        @JsonProperty("description")
        String description;
    }

}
