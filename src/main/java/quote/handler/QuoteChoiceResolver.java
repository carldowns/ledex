package quote.handler;


import ch.qos.logback.classic.Logger;
import com.google.common.collect.Sets;
import org.slf4j.LoggerFactory;
import part.Part;
import part.PartProperty;
import part.PartPropertyChoice;
import quote.Quote;
import quote.cmd.QuoteBaseCmd;

import java.util.Set;


/**
 * Handles the properties of a given Part that require an increment or choice which does NOT affect pricing or metrics.
 */
public class QuoteChoiceResolver implements QuoteHandlerInterface {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(QuoteChoiceResolver.class);

    @Override
    public void evaluate(QuoteBaseCmd cmd) {

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
                evaluateChoice(cmd, qPart);
            }
        }
    }

    private void evaluateChoice(QuoteBaseCmd cmd, Quote.QuotePart qPart) {

        Part part = qPart.part;
        cmd.checkNotNull(part, "part is not set");

        for (PartProperty prop : part.getProperties()) {
            if (prop.getChoices().isEmpty()) continue;

            boolean typesMatched = false;
            Set<Quote.QuoteSelection> matched = Sets.newHashSet();

            for (Quote.QuoteSelection selection : qPart.selections) {
                for (PartPropertyChoice choice : prop.getChoices()) {

                    // FIXME add in if we need to support type+name keys
                    //if (!selection.name.equals(prop.getName()))
                    //   continue;

                    if (!selection.type.equals(prop.getType()))
                        continue;

                    typesMatched = true;
                    if (!selection.value.equals(choice.getValue()))
                        continue;

                    matched.add(selection);
                }
            }

            // verify that there are not multiple selection matches for a given property
            if (matched.size() > 1)
                cmd.showFailed("multiple choice selections not supported for " + prop.getName());

            // verify that if a selection matched a type, a choice also matched
            if (matched.isEmpty() && typesMatched)
                cmd.showFailed("selection did not match any of the available choices for " + prop.getName());

            // verify that exactly ONE selection was made for the choice
            if (matched.size() == 0)
                cmd.showFailed("no choice selection found for " + prop.getName());
        }
    }
}
