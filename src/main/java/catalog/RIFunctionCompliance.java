package catalog;

import mgr.AssemblyMgr;

import java.util.Map;


public class RIFunctionCompliance implements IRuleInterpreter {
    public Map<RuleViolation, AssemblyMgr.CandidatePart> evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        return null;
    }

}
