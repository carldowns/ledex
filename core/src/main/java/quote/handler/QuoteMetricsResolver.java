package quote.handler;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import quote.Quote;
import quote.cmd.QuoteBaseCmd;

/**
 *
 */
public class QuoteMetricsResolver implements QuoteHandlerInterface {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(QuoteChoiceResolver.class);

    @Override
    public void evaluate(QuoteBaseCmd cmd) {

        Quote quote = cmd.getQuote();

        // walk through each product's parts
        // look for metrics properties

        for (Quote.LineItem lineItem : quote.items) {
            Quote.QuoteProduct qProduct = lineItem.quotedProduct;

            // this handler only applies for product line items
            if (qProduct == null) {
                continue;
            }

            for (Quote.QuotePart qPart : qProduct.quotedParts) {
                evaluateMetrics(cmd, qPart);
            }
        }
    }

    private void evaluateMetrics (QuoteBaseCmd cmd, Quote.QuotePart qPart) {
    }
}
