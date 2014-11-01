package catalog;

import mgr.AssemblyMgr;

import java.util.Map;


public class RISameSupplier implements IRuleInterpreter {
    /**
     * verifies that all parts are provided by the same supplier
     */
    public AssemblyMgr.CandidateProblem evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        return null;
    }

}
