package quote;

import ch.qos.logback.classic.Logger;
import cmd.BaseCmd;
import com.google.common.collect.Lists;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class QuoteEngine {

    private static ArrayList<Class<? extends QuoteHandler>> interpreters = Lists.newArrayList();
    private static final Logger logger = (Logger) LoggerFactory.getLogger(QuoteEngine.class);

    // static initializer
    {
        try {
            interpreters.add(QuotePricingHandler.class);
            interpreters.add(QuoteMetricsHandler.class);
            interpreters.add(QuoteLogisticsHandler.class);
            interpreters.add(QuoteQuantityHandler.class);
            interpreters.add(QuoteVerificationHandler.class);
            interpreters.add(QuoteReportHandler.class);
            interpreters.add(QuoteNotificationsHandler.class);
        }
        catch (Exception e) {
            logger.error("unable to initialize quote handlers", e);
        }
    }

    public void evaluate(BaseCmd cmd) throws Exception {

        for (Class<? extends QuoteHandler> ri : interpreters) {
            QuoteHandler interpreter = ri.newInstance();
            interpreter.evaluate(cmd);
        }
    }
}
