package quote.handler;

import ch.qos.logback.classic.Logger;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.LoggerFactory;
import quote.cmd.BaseQuoteCmd;
import util.CmdRuntimeException;

import java.util.ArrayList;

/////////////
// Products
/////////////

// product items will refer to one or more catalog parts.
// retrieve and attach full-definition Parts based on each catalog part

// QuotePartResolver (DONE)

// some parts require incremental property choices such as LED strip length
// which will affect pricing.  verify that required incremental selections are specified

// QuoteIncrementResolver (DONE)
// QuoteSelectionResolver

// a quote may request a range of quantities for a given product (100, 250, 500 sets)
// clone / add alternate quote volumes to the command

// calculate part pricing based on closest quantity match, incremental values
// record details of how pricing was derived

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
    private static ArrayList<QuoteHandlerInterface> interpreters = Lists.newArrayList();
    // FIXME this list of interpreters needs to be ordered now that we are
    // FIXME using IoC to add all of the interpreters we don't know in what order they are added
    // FIXME either that or we introduce a dependency chain


    public void evaluate(BaseQuoteCmd cmd) throws Exception {

        try {
            for (QuoteHandlerInterface ri : interpreters) {
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
        interpreters.add(handler);
    }

    @Inject
    public void addHandler(QuoteIncrementResolver handler) {
        interpreters.add(handler);
    }
}
