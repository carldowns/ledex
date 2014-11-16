package catalog.rule;

import catalog.Assembly;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Lists;
import catalog.CatalogEngine.*;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class RuleEngine {

    private static ArrayList<Class<? extends RuleInterpreter>> interpreters = Lists.newArrayList();
     private static final Logger logger = (Logger) LoggerFactory.getLogger(RuleEngine.class);

    // static initializer
    {
        try {
            interpreters.add(RIPower.class);
            interpreters.add(RICircuit.class);
        }
        catch (Exception e) {
            logger.error("unable to initialize interpreters", e);
        }
    }

    public CandidateProblem evaluate (Assembly assembly, CandidateProduct candidate) throws Exception {
        CandidateProblem report = new CandidateProblem();

        for (Class<? extends RuleInterpreter> ri : interpreters) {
            RuleInterpreter interpreter = ri.newInstance();
            CandidateProblem subReport = interpreter.evaluate(assembly, candidate);

            // if we encounter a problem, no sense in continuing to
            // evaluate against other interpreters.

            if (subReport != null && subReport.hasProblems()) {
                report.addProblems(subReport);
                break;
            }
        }

        return report;
    }
}
