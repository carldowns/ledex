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
    
    public Map<RuleViolation,AssemblyMgr.CandidatePart> evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        Map<RuleViolation,AssemblyMgr.CandidatePart> combined = Maps.newHashMap();

        for (IRuleInterpreter ri : interpreters) {
            Map<RuleViolation,AssemblyMgr.CandidatePart> violations = ri.evaluate(assembly, candidate);
            if (violations != null) {
                combined.putAll(violations);
            }
        }

        return combined;
    }
}
