package catalog;

import mgr.AssemblyMgr;

import java.util.Map;


public class RIVoltage implements IRuleInterpreter {
    /**
     * verifies that all electrical parts are operating within at the prescribed voltage
     */
    public Map<RuleViolation, AssemblyMgr.CandidatePart> evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        return null;
    }

}
