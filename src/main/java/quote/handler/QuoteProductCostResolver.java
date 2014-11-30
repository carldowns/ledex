package quote.handler;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import part.Part;
import part.PartCost;
import part.PartCostIncrement;
import quote.Quote;
import quote.cmd.BaseQuoteCmd;
import util.UnitConverter;
import util.UnitMath;

/**
 * calculates aggregate product costs
 * verify that none of the parts have an MOQ that is lower than the product quantity.
 * parts will have varying quantity price breaks defined
 * accumulate each part's incremental costs based on incremental selections.
 * be sure to handle quantities of parts greater than 1
 */
public class QuoteProductCostResolver implements QuoteHandlerInterface {

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

            // collect calculations of base cost and any incremental costs defined for that cost bracket.
            for (Quote.QuotePart qPart : qProduct.quotedParts) {
                calculateQuotedPartCost(cmd, lineItem, qPart);
            }

            // calculate the total cost for the product line item.
            for (Quote.QuotePart qPart : qProduct.quotedParts) {
                calculateLineItemTotalCost(cmd, lineItem, qPart);
            }
        }
    }

    private void calculateQuotedPartCost(BaseQuoteCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart) {

        PartCost partCost = selectBaseCostForPart(cmd, lineItem, qPart);
        addCostToPartQuotedCost(cmd, qPart, partCost.getBaseCost());
        addIncrementalCostsToPartQuotedCost(cmd, lineItem, qPart, partCost);
        addCostToLineItemQuotedCost(cmd, lineItem, qPart);
    }

    private PartCost selectBaseCostForPart(BaseQuoteCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart) {

        Part part = qPart.part;

        cmd.checkNotNull(part, "no part data is present for quoted part " + qPart);
        cmd.checkState(!part.getCosts().isEmpty(), "no cost data is present for part " + part);

        // find the cost bracket that most closely matches product quantity
        // use exact match part quantity price if available, otherwise next lowest range match.
        // if lowest declared quantity is higher than selected quantity, use it with potential MOQ violation flagged.

        int targetQuantity = Integer.parseInt(lineItem.quantity);
        PartCost lowestMatch = null;
        PartCost exactMatch = null;
        PartCost rangeMatch = null;

        for (PartCost cost : part.getCosts()) {
            int costQuantity = cost.getQty();

            if (costQuantity == targetQuantity) {
                exactMatch = cost;
                break;
            }

            if (lowestMatch == null || costQuantity < lowestMatch.getQty()) {
                lowestMatch = cost;
            }

            if (costQuantity < targetQuantity && (rangeMatch == null || costQuantity > rangeMatch.getQty())) {
                rangeMatch = cost;
            }
        }

        if (exactMatch != null) {
            Quote.QuoteNote note = new Quote.QuoteNote();
            note.type = "BASE-COST";
            note.value = exactMatch.getBaseCost();
            note.treatment = "base cost assigned using exact quantity match for " + part.getPartID();
            lineItem.calculations.add(note);
            return exactMatch;
        }

        if (rangeMatch != null) {
            Quote.QuoteNote note = new Quote.QuoteNote();
            note.type = "BASE-COST";
            note.value = rangeMatch.getBaseCost();
            note.treatment = "base cost assigned using range quantity match for " + part.getPartID();
            lineItem.calculations.add(note);
            return rangeMatch;
        }

        if (lowestMatch != null) {
            Quote.QuoteNote note = new Quote.QuoteNote();
            note.type = "BASE-COST";
            note.value = lowestMatch.getBaseCost();
            note.treatment = "MOQ warning: base cost assigned using lowest quantity match for " + part.getPartID();
            lineItem.calculations.add(note);
            return lowestMatch;
        }

        cmd.checkState(false, "unable to resolve base cost for part " + part);
        return null;
    }

    private void addIncrementalCostsToPartQuotedCost(BaseQuoteCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart, PartCost partCost) {

        // for all increments present on the part cost:
        // add cost for each incremental quantity selected

        for (PartCostIncrement inc : partCost.getIncrements()) {

            boolean resolved = false;
            for (Quote.QuoteSelection selection : qPart.selections) {

                if (!selection.type.equals(inc.getType()))
                    continue;

                // TODO: parameter-ize this: for now quotes are in USD, English measurements
                String unit = UnitMath.multiplyUnits(inc.getAddCost(), UnitConverter.UnitType.USD, selection.value, UnitConverter.UnitType.IN);
                addCostToPartQuotedCost(cmd, qPart, unit);

                resolved = true;
                break;
            }

            cmd.checkState(resolved, "unable to resolve incremental cost fo selection " + inc.getName());
        }
    }

    private void addCostToLineItemQuotedCost(BaseQuoteCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart) {

        if (lineItem.quotedCost == null) {
            lineItem.quotedCost = new Quote.QuoteCost();
            lineItem.quotedCost.value = qPart.quotedCost.value;
            return;
        }

        lineItem.quotedCost.value = UnitMath.addUnits(lineItem.quotedCost.value, qPart.quotedCost.value);
    }

    private void addCostToPartQuotedCost(BaseQuoteCmd cmd, Quote.QuotePart qPart, String cost) {

        if (qPart.quotedCost == null) {
            qPart.quotedCost = new Quote.QuoteCost();
            qPart.quotedCost.value = UnitMath.multiplyScalar(cost, qPart.quantity);
            return;
        }

        // most of the time part quantity will be = 1.
        // linkable parts can be specified in multiples hence the need for a part quantity
        String unit1 = UnitMath.multiplyScalar(cost, qPart.quantity);
        String unit2 = UnitMath.addUnits(qPart.quotedCost.value, unit1);
        qPart.quotedCost.value = unit2;
    }

    private void calculateLineItemTotalCost(BaseQuoteCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart) {

        if (lineItem.totalCost == null) {
            lineItem.totalCost = new Quote.QuoteCost();
            lineItem.totalCost.value = UnitMath.multiplyScalar(qPart.quotedCost.value, lineItem.quantity);
            return;
        }

        String unit1 = UnitMath.multiplyScalar(lineItem.totalCost.value, qPart.quantity);
        String unit2 = UnitMath.addUnits(lineItem.totalCost.value, unit1);
        lineItem.totalCost.value = unit2;
    }


}
