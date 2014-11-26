package quote.handler;

import ch.qos.logback.classic.Logger;
import com.google.inject.Inject;
import org.slf4j.LoggerFactory;
import quote.cmd.BaseQuoteCmd;
import util.CmdRuntimeException;

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

// QuoteChoiceResolver

// a quote may request a range of quantities for a given product (100, 250, 500 sets)
// clone / add alternate quote volumes to the command

// QuoteQuantityResolver

// calculate metrics based on baseline and incremental values
// record details of how the metrics were derived

// QuoteMetricsResolver

// concatenate the complete agg Label and description for each part based on features, increments and selections.

// QuoteAggregateLabelResolver
// QuoteAggregateDescriptionResolver

/////////////
// Services
/////////////

// calculate logistics based on service level selected
// record details of how the metrics were derived
// calculate handling / project management service fees if selected

// QuoteServiceResolver

/////////////////////
// SLA
/////////////////////

// calculate the lead time estimated for each phase of the product build
// phases possible include prototype, sample, pre-pro visual, pre-pro actual, production, ship, in-transit
// Includes warranty information

// QuoteAgreementResolver
// QuoteWarrantyResolver

//////////////////
// Pricing
//////////////////

// calculate part pricing based on closest quantity match, incremental values
// record details of how pricing was derived

// QuotePriceResolver;

//////////////////
// Verification
//////////////////

// verify that at least one line item is present with one service or product and a quantity for each
// verify that of present, each product contains compatible parts
// verify that part quantities are correct taking linkable property into account

// QuoteVerifier

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

    public void evaluate(BaseQuoteCmd cmd) throws Exception {

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
}
