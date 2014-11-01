package catalog;

import com.google.common.collect.Maps;
import mgr.AssemblyMgr;

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
    
    public AssemblyMgr.CandidateProblem evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        AssemblyMgr.CandidateProblem report = null;

        for (IRuleInterpreter ri : interpreters) {
            AssemblyMgr.CandidateProblem problem = ri.evaluate(assembly, candidate);
            if (problem != null) {
                if (report == null) {
                    report = new AssemblyMgr.CandidateProblem();
                }
                report.addProblems(problem);
            }
        }

        return report;
    }
}
