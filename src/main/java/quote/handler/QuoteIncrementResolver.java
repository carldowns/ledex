package quote.handler;


import ch.qos.logback.classic.Logger;
import part.Part;
import part.PartProperty;
import part.PartPropertyIncrement;
import quote.cmd.BaseQuoteCmd;
import quote.Quote;
import util.Unit;

import java.math.BigDecimal;

import org.slf4j.LoggerFactory;


/**
 * Handles the incremental aspects of a given Part.  Some parts require incremental property choices
 * such as LED strip length which can affect pricing and metrics. Verify that required incremental
 * selections are specified, compatible and ready for downstream processing.
 */
public class QuoteIncrementResolver implements QuoteHandlerInterface {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(QuoteIncrementResolver.class);

    @Override
    public void evaluate(BaseQuoteCmd cmd) {

        Quote quote = cmd.getQuote();

        // walk through each product's parts
        // look for incremental properties

        for (Quote.LineItem lineItem : quote.items) {
            Quote.QuoteProduct qProduct = lineItem.quotedProduct;

            // this handler only applies for product line items
            if (qProduct == null) {
                continue;
            }

            for (Quote.QuotePart qPart : qProduct.quotedParts) {
                evaluateIncrement(cmd, qPart);
            }
        }
    }

    private void evaluateIncrement(BaseQuoteCmd cmd, Quote.QuotePart qPart) {

        Part part = qPart.part;
        cmd.checkNotNull(part, "part is not set");

        for (PartProperty prop : part.getProperties()) {
            PartPropertyIncrement inc = prop.getIncrement();
            if (inc == null) {
                continue;
            }

            // for incremental properties found:
            // verify that corresponding selections are present
            // verify that selections match specification

            boolean resolved = false;
            for (Quote.QuoteSelection selection : qPart.selections) {

                // TODO can we get rid of name and instead rely on unique property types per part?
                //if (!selection.name.equals(prop.getName())) {
                //   continue;
                //}

                if (!selection.type.equals(prop.getType())) {
                    continue;
                }

                // verify we have a corresponding selection
                checkValueNotBlank(cmd, part, selection.value);

                // get unit converters for selection and increment
                Unit value = new Unit(selection.value);
                Unit div = new Unit(inc.getIncDiv());
                Unit max = new Unit(inc.getIncMax());
                Unit min = new Unit(inc.getIncMin());

                // get everything in the same units of measure
                BigDecimal valueMM = value.toMillimeters(3);
                BigDecimal divMM = div.toMillimeters(3);
                BigDecimal maxMM = max.toMillimeters(3);
                BigDecimal minMM = min.toMillimeters(3);

                cmd.checkState(valueMM.compareTo(minMM) >= 0, "selection falls below the minimum allowed");
                cmd.checkState(valueMM.compareTo(maxMM) <= 0, "selection falls above the maximum allowed");
                cmd.checkState(valueMM.remainder(divMM).intValue() == 0, "selection is not a multiple of the divisor specified");
                cmd.checkState(isLengthMeasure(prop), "part is not a length measure");

                resolved = true;
                break;
            }

            cmd.checkState(resolved, "no selection found for " + prop.getName());
        }
    }

    private boolean isLengthMeasure(PartProperty prop) {
        // verify that selection is of the correct type
        switch (prop.getType()) {

            case FLEX_LENGTH:
            case STRIP_LENGTH:
            case LEAD_LENGTH:
            case MALE_LEAD_LENGTH:
            case FEMALE_LEAD_LENGTH:
            case POWER_CORD_LENGTH:
                return true;
        }

        return false;
    }

    private void checkValueNotBlank(BaseQuoteCmd cmd, Part part, String value) {
        cmd.checkNotBlank(value, "selection is not set for part " + part.getPartID());
    }
}
