package catalog;


import catalog.CatalogEngine.*;

/**
 * implementations of this interface verify that a given ProductCandidate
 */
public interface RuleInterpreter {

    public CandidateProblem evaluate (Assembly assembly, CandidateProduct candidate);
}
