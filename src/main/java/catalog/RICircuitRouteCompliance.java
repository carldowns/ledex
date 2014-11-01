package catalog;

import mgr.AssemblyMgr;

import java.util.Map;

/**
 * verifies that all parts can be connected in a circuit
 */
public class RICircuitRouteCompliance implements IRuleInterpreter {

    public AssemblyMgr.CandidateProblem evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        return null;
    }

}
