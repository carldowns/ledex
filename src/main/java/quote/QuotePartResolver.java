package quote;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import mgr.CatalogMgr;
import part.Part;

/**
 * resolves, verifies referenced Parts for Product line items.
 */
@Singleton
public class QuotePartResolver implements QuoteHandler {

    CatalogMgr catalogMgr;

    @Inject
    QuotePartResolver (CatalogMgr catalogMgr) {
        this.catalogMgr = catalogMgr;
        checkDependencies();
    }

    /**
     * make sure that the command contains all referenced parts
     * @param command
     */
    @Override
    public void evaluate(BaseQuoteCmd cmd) {

        Quote quote = cmd.getQuote();
        checkQuoteIsSet(cmd, quote);

        for (Quote.LineItem lineItem : quote.items) {
            Quote.QuoteProduct qProduct = lineItem.quotedProduct;

            checkServiceOrProductIsSet(cmd, lineItem);
            // this handler only applies for product line items
            if (qProduct == null) {
                continue;
            }

            checkProductHasParts(cmd, qProduct);
            for (Quote.QuotePart qPart : qProduct.quotedParts) {
                Part part = qPart.part;
                if (part == null) {
                    qPart.part = catalogMgr.getPart(qPart.catalogPart.getPartID());
                }

                checkPartIDsMatch(cmd, qPart, qPart.part);
            }
        }
    }

    private void checkQuoteIsSet (BaseQuoteCmd cmd, Quote quote) {
        cmd.checkNotNull(quote, "quote is not set");
    }

    private void checkServiceOrProductIsSet (BaseQuoteCmd cmd, Quote.LineItem lineItem) {
        cmd.checkNotNull(lineItem, "lineItem is not set");
        cmd.checkState(lineItem.quotedProduct != null || lineItem.quotedService != null, "product or service election required");
    }

    private void checkPartIDsMatch (BaseQuoteCmd cmd, Quote.QuotePart qPart, Part part) {
        cmd.checkNotNull(qPart, "quoted part is not set");
        cmd.checkNotNull(part, "part is not set");
        cmd.checkState(part.getPartID().equals(qPart.catalogPart.getPartID()), "part IDs don't match");
    }

    private void checkProductHasParts (BaseQuoteCmd cmd, Quote.QuoteProduct qProduct) {
        cmd.checkNotNull(qProduct, "quoted product is not set");
        cmd.checkState(qProduct.quotedParts.size() > 0,"no product parts specified");
    }

    private void checkDependencies() {
        Preconditions.checkNotNull(catalogMgr,"catalog manager dependency not resolved");
    }
}
