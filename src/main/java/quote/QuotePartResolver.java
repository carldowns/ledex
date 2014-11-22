package quote;

import com.google.common.base.Preconditions;
import mgr.CatalogMgr;
import part.Part;

/**
 * resolves quote referenced Parts
 */
public class QuotePartResolver implements QuoteHandler {

    CatalogMgr catalogMgr;

    /**
     * make sure that the command contains all referenced parts
     * @param command
     */
    @Override
    public void evaluate(BaseQuoteCmd command) {

        Quote quote = command.getQuote();
        for (Quote.LineItem lineItem : quote.items) {
            Quote.QuoteProduct qProduct = lineItem.quotedProduct;

            // only applies for product line items
            if (qProduct == null) {
                continue;
            }

            assureHasParts(qProduct);
            for (Quote.QuotePart qPart : qProduct.quotedParts) {
                Part part = qPart.part;
                if (part != null) {
                    assurePartIDsMatch (qPart, part);
                    continue;
                }

                assureMgrInjected();
                qPart.part = catalogMgr.getPart(qPart.catalogPart.getPartID());
            }
        }
    }

    private void assureHasParts (Quote.QuoteProduct qProduct) {
        Preconditions.checkState(qProduct.quotedParts.size() > 0,"no product parts specified");
    }

    private void assurePartIDsMatch (Quote.QuotePart qPart, Part part) {
        Preconditions.checkState(part.getPartID().equals(qPart.catalogPart.getPartID()), "part IDs don't match");
    }

    private void assureMgrInjected () {
        Preconditions.checkNotNull(catalogMgr,"catalog manager not resolved");
    }
}
