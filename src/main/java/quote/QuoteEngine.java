package quote;

import ch.qos.logback.classic.Logger;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class QuoteEngine {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(QuoteEngine.class);
    private static ArrayList<QuoteHandler> interpreters = Lists.newArrayList();

//    private static ArrayList<Class<? extends QuoteHandler>> interpreters = Lists.newArrayList();
//    // static initializer
//    {
//        try {
//            // when a quote is created, it will consist of one or more product and/or service line items.
//
//            /////////////
//            // Products
//            /////////////
//
//            // product items will refer to one or more catalog parts.
//            // retrieve and attach full-definition Parts based on each catalog part
//
//            interpreters.add(QuotePartResolver.class);
//
//            // some parts require incremental property choices such as LED strip length
//            // which will affect pricing.  verify that required incremental selections are specified
//
//            interpreters.add(QuoteIncrementResolver.class);
//
//            // a quote may request a range of quantities for a given product (100, 250, 500 sets)
//            // clone / add alternate quote volumes to the command
//
//            // calculate part pricing based on closest quantity match, incremental values
//            // record details of how pricing was derived
//
//            interpreters.add(QuoteQuantityResolver.class);
//
//            // calculate metrics based on baseline and incremental values
//            // record details of how the metrics were derived
//
//            interpreters.add(QuoteMetricsResolver.class);
//
//            /////////////
//            // Services
//            /////////////
//
//            // calculate logistics based on service level selected
//            // record details of how the metrics were derived
//            // calculate handling / project management service fees if selected
//
//            interpreters.add(QuoteServiceResolver.class);
//
//            /////////////////////
//            // SLA
//            /////////////////////
//
//            // calculate the lead time estimated for each phase of the product build
//            // phases possible include prototype, sample, pre-pro visual, pre-pro actual, production, ship, in-transit
//            // Includes warranty information
//
//            interpreters.add(QuoteAgreementResolver.class);
//            interpreters.add(QuoteWarrantyResolver.class);
//
//            //////////////////
//            // Pricing
//            //////////////////
//
//            interpreters.add(QuotePriceResolver.class);
//
//            //////////////////
//            // Verification
//            //////////////////
//
//            // verify that at least one line item is present with one service or product and a quantity for each
//            // verify that of present, each product contains compatible parts
//            // verify that part quantities are correct taking linkable property into account
//
//            interpreters.add(QuoteVerifier.class);
//
//            ///////////////
//            // Report
//            ///////////////
//
//            // reporting is very important.  The quote contains ALL of the pertinent data.  Neither supplier nor
//            // customer should see the entire set of data.  Instead, we filter the output based on who they are (role)
//
//            interpreters.add(QuoteReporter.class);
//
//            ///////////////
//            // Notify
//            ///////////////
//
//            // notification occurs after the quote report is ready.
//            // Reports can be sent via email as a PDF.
//            // Reports can be sent to both supplier and customer.
//            // Reports can be sent to competitors of customers or multiple email addresses
//            // Consolidated reports for multiple quantity levels can be generated
//            // within a customer (project manager versus buying manager)
//
//            interpreters.add(QuoteNotifier.class);
//
//            ///////////////
//            // Persist
//            ///////////////
//
//            interpreters.add(QuoteWriter.class);
//
//        }
//        catch (Exception e) {
//            logger.error("unable to initialize quote handlers", e);
//        }
//    }
//
//    public void evaluate(BaseQuoteCmd cmd) throws Exception {
//
//        for (Class<? extends QuoteHandler> ri : interpreters) {
//            QuoteHandler interpreter = ri.newInstance();
//            interpreter.evaluate(cmd);
//        }
//    }

    public void evaluate(BaseQuoteCmd cmd) throws Exception {

        for (QuoteHandler ri : interpreters) {
            ri.evaluate(cmd);
        }
    }

    @Inject
    public void addHandler (QuotePartResolver handler) {
        interpreters.add(handler);
    }
}
