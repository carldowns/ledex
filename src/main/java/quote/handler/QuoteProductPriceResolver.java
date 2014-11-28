package quote.handler;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import quote.Quote;
import quote.cmd.BaseQuoteCmd;

/**
 * handles assessing quote pricing
 */
public class QuoteProductPriceResolver implements QuoteHandlerInterface {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(QuoteChoiceResolver.class);

    @Override
    public void evaluate(BaseQuoteCmd cmd) {

        Quote quote = cmd.getQuote();

        // apply margin rules based on customer profile and quote mode.
        // We have yet to figure out what the market wants here.  Should not over do it.
        // To start off, this will be a configuration of margin parameters sliding scale.

        for (Quote.LineItem lineItem : quote.items) {
            Quote.QuoteProduct qProduct = lineItem.quotedProduct;

            // this handler only applies for product line items
            if (qProduct == null) {
                continue;
            }

            for (Quote.QuotePart qPart : qProduct.quotedParts) {
                evaluateProductPricing(cmd, qPart);
            }
        }
    }

    private void evaluateProductPricing(BaseQuoteCmd cmd, Quote.QuotePart qPart) {
    }

}
