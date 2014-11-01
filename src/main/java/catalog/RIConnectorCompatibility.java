package catalog;

import mgr.AssemblyMgr;

import java.util.Map;


public class RIConnectorCompatibility implements IRuleInterpreter {
    /**
     * verifies that all electrical parts are plug / connector compatible
     */
    public Map<RuleViolation, AssemblyMgr.CandidatePart> evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        return null;
    }

}
