package quote.handler;

import ch.qos.logback.classic.Logger;
import com.google.inject.Inject;
import org.slf4j.LoggerFactory;
import quote.cmd.QuoteBaseCmd;
import cmd.CmdRuntimeException;

import java.util.SortedMap;
import java.util.TreeMap;

/////////////
// Products
/////////////

// product items will refer to one or more catalog parts.
// retrieve and attach full-definition Parts based on each catalog part
// QuotePartResolver (DONE)

// some parts require incremental property selection such as LED strip length
// which will affect pricing.  verify that required incremental selections are specified
// QuoteIncrementResolver (DONE)

// some parts require choice property selection such as tape front/back, etc.
// choices do NOT affect pricing.  verify that required choice selections are specified
// QuoteChoiceResolver (DONE)

////////////////////
// Product Cost
////////////////////

// accumulate each part's fixed, incremental costs based on incremental selections.
// add it all up showing a cost number plus some means of showing the calculation?
// QuoteProductCostResolver (DONE)

////////////////////
// Product Pricing
////////////////////

// calculate part pricing based on closest quantity match, incremental costs
// record details of how pricing was derived
// QuoteProductPriceResolver; (DONE)

// TODO add in support for per-customer pricing strategies -- all we have now is 25% margin across the board

////////////////////
// Part Identifier
////////////////////

// concatenate the complete agg Label and description for each part based on features, increments and selections.
// QuotePartLabelResolver (DONE)
// QuotePartDescriptionResolver (DONE)

//////////////////////
// Logistics Service
//////////////////////

// calculate metrics based on baseline and incremental metrics
// record details of how the metrics were derived
// QuoteMetricsResolver

// select logistics service level
// QuoteLogisticsServiceResolver

// calculate logistics based on service level selected
// QuoteAirFreightResolver
// QuoteUSCustomsImportFeesResolver

// extra credit
// QuoteOceanFreightResolver
// QuoteLTLResolver
// QuoteRailResolver
// QuoteOvernightResolver
// QuoteTmpStorageResolver

// calculate the lead time estimated for each phase of the product build
// phases possible include prototype, sample, pre-pro visual, pre-pro actual, production, ship, in-transit
// QuoteLeadTimeEstimator

////////////////////////
// Management Service
////////////////////////

// calculate handling / project management service fees if selected
// QuoteManagementServiceResolver

/////////////////////
// SLA
/////////////////////

// QuoteWarrantyResolver

/////////////////////////
// Multiple Quantities
/////////////////////////

// a quote may request a range of quantities for a given product (100, 250, 500 sets)
// clone / add alternate quote volumes to the command
// QuoteMultiQuantityResolver

//////////////////
// Verification
//////////////////

// verify that at least one line item is present with one service or product and a quantity for each
// QuoteVerifier

// verify that of present, each product contains compatible parts
// verify that part quantities are correct taking linkable property into account
// QuotePartConnectionResolver


///////////////
// Report
///////////////

// reporting is very important.  The quote contains ALL of the pertinent data.  Neither supplier nor
// customer should see the entire set of data.  Instead, we filter the output based on who they are (role)
// QuoteReporter

///////////////
// Notify
///////////////

// notification occurs after the quote report is ready.
// Reports can be sent via email as a PDF.
// Reports can be sent to both supplier and customer.
// Reports can be sent to competitors of customers or multiple email addresses
// Consolidated reports for multiple quantity levels can be generated
// within a customer (project manager versus buying manager)
// QuoteNotifier

///////////////
// Persist
///////////////

// QuoteWriter


public class QuoteEngine {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(QuoteEngine.class);
    private static SortedMap<Integer,QuoteHandlerInterface> interpreters = new TreeMap<>();

    public void evaluate(QuoteBaseCmd cmd) throws Exception {

        try {
            for (QuoteHandlerInterface ri : interpreters.values()) {
                ri.evaluate(cmd);
            }
        }

        catch (CmdRuntimeException cre) {
            throw cre;
        }

        catch (Exception e) {
            cmd.showFailed(e.getMessage());
            throw e;
        }
    }


    @Inject
    public void addHandler(QuotePartResolver handler) {
        interpreters.put(0,handler);
    }

    @Inject
    public void addHandler(QuoteIncrementResolver handler) {
        interpreters.put(1,handler);
    }

    @Inject
    public void addHandler(QuoteChoiceResolver handler) {
        interpreters.put(2,handler);
    }

    @Inject
    public void addHandler(QuoteProductCostResolver handler) {
        interpreters.put(3,handler);
    }

    @Inject
    public void addHandler(QuoteProductPriceResolver handler) {
        interpreters.put(4,handler);
    }

    @Inject
    public void addHandler(QuoteSkuLabelResolver handler) {
        interpreters.put(5,handler);
    }
}
