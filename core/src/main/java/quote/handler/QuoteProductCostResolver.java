package quote.handler;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import part.*;
import quote.Quote;
import quote.cmd.QuoteBaseCmd;
import util.Unit;
import util.UnitMath;

import java.util.List;

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
    public void evaluate(QuoteBaseCmd cmd) {

        Quote quote = cmd.getQuote();

        for (Quote.LineItem lineItem : quote.items) {
            Quote.QuoteProduct qProduct = lineItem.quotedProduct;

            // this handler only applies for product line items
            if (qProduct == null) {
                continue;
            }

            // idempotent support - this handler can re-cost a previously evaluated quote
            clearCostsAndPricing(cmd, lineItem, qProduct);

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

    private void clearCostsAndPricing (QuoteBaseCmd cmd, Quote.LineItem lineItem, Quote.QuoteProduct qProduct) {
        for (Quote.QuotePart qPart : qProduct.quotedParts) {
            qPart.quotedCost = null;
        }

        lineItem.quotedPrice = null;
        lineItem.quotedCost = null;
        lineItem.totalCost = null;
        lineItem.totalPrice = null;
    }

    private void calculateQuotedPartCost(QuoteBaseCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart) {

        PartCost partCost = selectBaseCostForPart(cmd, lineItem, qPart);
        addCostToPartQuotedCost(cmd, qPart, partCost.getBaseCost());
        addIncrementalCostsToPartQuotedCost(cmd, lineItem, qPart, partCost);
        addCostToLineItemQuotedCost(cmd, lineItem, qPart);
    }

    private PartCost selectBaseCostForPart(QuoteBaseCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart) {

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

    private void addIncrementalCostsToPartQuotedCost(QuoteBaseCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart, PartCost partCost) {

        // for all increments present on the part cost:
        // add cost for each incremental quantity selected

        for (PartCostIncrement costInc : partCost.getIncrements()) {

            boolean resolved = false;
            for (Quote.QuoteSelection selection : qPart.selections) {

                if (!selection.type.equals(costInc.getType()))
                    continue;

                // get property increment declaration.
                // verify that there is only one part declared of this type

                List<PartProperty> parts = qPart.part.getPropertiesOfType(costInc.getType());
                cmd.checkState(parts.size() == 1, "requires exactly 1 part property specified for type " + costInc.getType());

                PartProperty pProp = parts.get(0);
                PartPropertyIncrement propInc = pProp.getIncrement();

                Unit incDiv = new Unit(propInc.getIncDiv());
                Unit incMin = new Unit(propInc.getIncMin());
                Unit incMax = new Unit(propInc.getIncMax());
                Unit select = new Unit(selection.value);

                // verify property increments and the selection are all of the same unit type

                cmd.checkState(incDiv.isSameType (incMin), "incremental property units type mismatch: " + pProp.toString());
                cmd.checkState(incDiv.isSameType (incMax), "incremental property units type mismatch: " + pProp.toString());
                cmd.checkState(incDiv.isSameType (select), "incremental property units type mismatch: " + pProp.toString());

                // verify that this selection is valid (within min/max incremental limits and evenly divisible).

                cmd.checkState(select.compareTo(incMin) >= 0, "selection is below minimum " + pProp.toString());
                cmd.checkState(select.compareTo(incMax) <= 0, "selection is above maximum " + pProp.toString());
                cmd.checkState(select.modulo(incDiv).intValue() == 0, "selection is not a multiple " + pProp.toString());

                // get the cost multiplier by dividing by divisor

                Unit multiplier = new Unit(select.divideBy(incDiv));
                String unit = UnitMath.multiplyUnits(new Unit(costInc.getAddCost()), multiplier);
                addCostToPartQuotedCost(cmd, qPart, unit);

                resolved = true;
                break;
            }

            cmd.checkState(resolved, "unable to resolve incremental cost fo selection " + costInc.getName());
        }
    }

    private void addCostToLineItemQuotedCost(QuoteBaseCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart) {

        if (lineItem.quotedCost == null) {
            lineItem.quotedCost = new Quote.QuoteCost();
            lineItem.quotedCost.value = qPart.quotedCost.value;
            return;
        }

        lineItem.quotedCost.value = UnitMath.addUnits(lineItem.quotedCost.value, qPart.quotedCost.value);
    }

    private void addCostToPartQuotedCost(QuoteBaseCmd cmd, Quote.QuotePart qPart, String cost) {

        if (qPart.quotedCost == null) {
            qPart.quotedCost = new Quote.QuoteCost();
            qPart.quotedCost.value = UnitMath.multiplyInteger(cost, qPart.quantity);
            return;
        }

        // most of the time part quantity will be = 1.
        // linkable parts can be specified in multiples hence the need for a part quantity
        String calc1 = UnitMath.multiplyInteger(cost, qPart.quantity);
        String calc2 = UnitMath.addUnits(qPart.quotedCost.value, calc1);
        qPart.quotedCost.value = calc2;
    }

    private void calculateLineItemTotalCost(QuoteBaseCmd cmd, Quote.LineItem lineItem, Quote.QuotePart qPart) {

        if (lineItem.totalCost == null) {
            lineItem.totalCost = new Quote.QuoteCost();
            lineItem.totalCost.value = UnitMath.multiplyInteger(qPart.quotedCost.value, lineItem.quantity);
            return;
        }

        String calc1 = UnitMath.multiplyInteger(qPart.quotedCost.value, lineItem.quantity);
        String calc2 = UnitMath.addUnits(lineItem.totalCost.value, calc1);
        lineItem.totalCost.value = calc2;
    }

}
