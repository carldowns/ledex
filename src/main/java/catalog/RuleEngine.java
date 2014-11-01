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
        interpreters.add(new RISupplier());
    }
    
    public CandidateProblem evaluate (Assembly assembly, CandidateProduct candidate) {
        CandidateProblem report = new CandidateProblem();

        for (RuleInterpreter ri : interpreters) {
            CandidateProblem problem = ri.evaluate(assembly, candidate);
            if (problem != null) {
                report.addProblems(problem);
            }
        }

        return report;
    }
}
