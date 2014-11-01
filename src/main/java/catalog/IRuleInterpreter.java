package catalog;


import mgr.AssemblyMgr.*;

import java.util.Map;

/**
 * implementations of this interface verify that a given ProductCandidate
 */
public interface IRuleInterpreter {

    public CandidateProblem evaluate (Assembly assembly, CandidateProduct candidate);
}
