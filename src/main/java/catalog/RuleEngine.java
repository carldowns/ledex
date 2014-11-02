package catalog;

import mgr.AssemblyMgr.*;

import java.util.ArrayList;


public class RuleEngine {

    ArrayList<RuleInterpreter> interpreters = new ArrayList<RuleInterpreter>();

    // static initializer
    {
        interpreters.add(new RIPower());
        interpreters.add(new RICircuit());
        interpreters.add(new RIConnector());
    }
    
    public CandidateProblem evaluate (Assembly assembly, CandidateProduct candidate) {
        CandidateProblem report = new CandidateProblem();

        for (RuleInterpreter ri : interpreters) {
            CandidateProblem subReport = ri.evaluate(assembly, candidate);

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
