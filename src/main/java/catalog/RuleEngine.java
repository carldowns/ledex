package catalog;

import com.google.common.collect.Maps;
import mgr.AssemblyMgr.*;

import java.util.ArrayList;
import java.util.Map;


public class RuleEngine {

    ArrayList<IRuleInterpreter> interpreters = new ArrayList<IRuleInterpreter>();

    // static initializer
    {
        interpreters.add(new RIAmperage());
        interpreters.add(new RIVoltage());
        interpreters.add(new RIFunctionCompliance());
        interpreters.add(new RIPartCompatibility());
        interpreters.add(new RIConnectorCompatibility());
    }
    
    public CandidateProblem evaluate (Assembly assembly, CandidateProduct candidate) {
        CandidateProblem report = new CandidateProblem();

        for (IRuleInterpreter ri : interpreters) {
            CandidateProblem problem = ri.evaluate(assembly, candidate);
            if (problem != null) {
                report.addProblems(problem);
            }
        }

        return report;
    }
}
