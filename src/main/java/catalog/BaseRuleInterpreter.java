package catalog;

import mgr.AssemblyMgr;

/**
 */
public abstract class BaseRuleInterpreter {

    protected AssemblyMgr.CandidateProblem reportProblem(AssemblyMgr.CandidatePart candidate, String message) {
        AssemblyMgr.CandidateProblem problem = new AssemblyMgr.CandidateProblem(new RuleViolation(message), candidate);
        return problem;
    }
}
