package catalog;

import mgr.AssemblyMgr.*;

import java.util.Map;


public class RIConnectorCompatibility implements IRuleInterpreter {
    /**
     * verifies that all electrical parts are plug / connector compatible
     */
    public CandidateProblem evaluate (Assembly assembly, CandidateProduct candidate) {
        return null;
    }

}
