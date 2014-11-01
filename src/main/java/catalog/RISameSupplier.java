package catalog;

import mgr.AssemblyMgr;

import java.util.Map;


public class RISameSupplier implements IRuleInterpreter {
    /**
     * verifies that all parts are provided by the same supplier
     */
    public Map<RuleViolation, AssemblyMgr.CandidatePart> evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        return null;
    }

}
