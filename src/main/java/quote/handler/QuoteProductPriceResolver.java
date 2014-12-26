package quote.handler;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import quote.Quote;
import quote.cmd.BaseQuoteCmd;
import util.UnitMath;

import java.math.BigDecimal;

/**
 * handles assessing quote pricing
 */
public class QuoteProductPriceResolver implements QuoteHandlerInterface {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(QuoteChoiceResolver.class);

    @Override
    public void evaluate(BaseQuoteCmd cmd) {

        Quote quote = cmd.getQuote();

        for (Quote.LineItem lineItem : quote.items) {
            Quote.QuoteProduct qProduct = lineItem.quotedProduct;

            // this handler only applies for product line items
            if (qProduct == null) {
                continue;
            }

            calculateLineItemFixedMarginPricing(cmd, lineItem);
        }
    }


    private void calculateLineItemFixedMarginPricing(BaseQuoteCmd cmd, Quote.LineItem lineItem) {

        // TODO: hard-coded margin needs to be configurable generally and also per customer.
        BigDecimal margin = new BigDecimal("1.25");

        // calculate 'quoted price' which is the cost for 1 product
        lineItem.quotedPrice = new Quote.QuotePrice(UnitMath.multiplyBigDecimal(lineItem.quotedCost.value, margin, 2));

        // calculate 'total price' which is the cost for N products where N is line item quantity
        lineItem.totalPrice = new Quote.QuotePrice(UnitMath.multiplyBigDecimal(lineItem.totalCost.value, margin, 2));
    }

}
