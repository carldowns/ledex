package catalog.rule;


import catalog.Assembly;
import catalog.AssemblyEngine.*;

/**
 * implementations of this interface verify that a given ProductCandidate
 */
public interface RuleInterpreter {

    public CandidateProblem evaluate (Assembly assembly, CandidateProduct candidate);
}
