package quote.handler;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import part.*;
import quote.Quote;
import quote.cmd.BaseQuoteCmd;

import java.util.ArrayList;

/**
 * Builds up a composite part label from all of the properties of the part
 */

@Singleton
public class QuoteSkuLabelResolver implements QuoteHandlerInterface {

    private static final String VALUE_LITERAL = "{value}";

    @Inject
    public QuoteSkuLabelResolver() {
    }

    /**
     * make sure that the command contains all referenced parts
     * @param command
     */
    @Override
    public void evaluate(BaseQuoteCmd cmd) {

        Quote quote = cmd.getQuote();

        for (Quote.LineItem lineItem : quote.items) {
            Quote.QuoteProduct qProduct = lineItem.quotedProduct;

            // this handler only applies for product line items
            if (qProduct == null) {
                continue;
            }

            constructLabelsAndDescriptions(qProduct);
        }
    }

    private void constructLabelsAndDescriptions (Quote.QuoteProduct qProduct) {
        for (Quote.QuotePart qPart : qProduct.quotedParts) {

            ArrayList<String> labelBits = Lists.newArrayList();
            ArrayList<String> descBits = Lists.newArrayList();

            Part part = qPart.part;
            String partName = part.getName();
            String partSkuLabel = part.getSkuLabel();

            labelBits.add(partSkuLabel == null ? format(partName) : format(partSkuLabel));
            descBits.add(formatDesc (partName, null, null, null));

            for (PartProperty pProp : part.getProperties()) {

                String propSkuLabel = pProp.getSkuLabel();
                String propValue = pProp.getValue();
                String propSelection =  getSelection (qPart, pProp);
                String propDescription = pProp.getDescription();
                String propName = pProp.getName();

                // properties should have a value or a selection (not both)
                labelBits.add (formatSku(propSkuLabel, propValue != null ? propValue : propSelection));
                descBits.add (formatDesc(propName, propDescription, propValue, propSelection));
            }

            for (PartConnection pConn : part.getConnections()) {
                String gender = pConn.getGender();
                String type = pConn.getType();
                String mode = pConn.getConnectMode() != null ? pConn.getConnectMode().toString() : null;
                String pins = pConn.getPinOut();
                descBits.add(formatDesc(type, gender, mode, pins));
            }

            if (qPart.part.isLinkable()) {
                labelBits.add("Lk");
            }

            qPart.cfgLabel = (Joiner.on("-").skipNulls().join(labelBits));
            qPart.cfgDescription =  (Joiner.on(" ").skipNulls().join(descBits));
        }
    }

    private String formatDesc (String... fragments) {
        StringBuilder sb = new StringBuilder();
        for (String s : fragments) {
            if (s == null) continue;
            sb.append(" ").append(s);
        }
        sb.append(";");
        return sb.toString().trim();
    }

    private String getSelection (Quote.QuotePart qPart, PartProperty pProp) {
        for (Quote.QuoteSelection selection : qPart.selections) {
            if (selection.type == pProp.getType()) {
                return selection.value;
            }
        }
        return null;
    }

    /**
     * returns a formatted Sku Label.  If a label is not defined, then the
     * value is returned as-is.  If a SKU label is defined, the SKU label may
     * have a '{value}' substitution literal in it.  In that case the value will be substituted in.
     * Otherwise, the SKU label itself will be returned.
     * @param skuLabel
     * @param value
     * @return
     */
    private String formatSku (String skuLabel, String value) {

        if (skuLabel == null) {
            return format(value);
        }

        if (skuLabel.contains(VALUE_LITERAL)) {
            return format(skuLabel.replace(VALUE_LITERAL, value));
        }

        return format(skuLabel);
    }

    private String format(String fragment) {
        if (fragment == null) return null;
        return fragment.replaceAll("\\ ","");
    }
}
