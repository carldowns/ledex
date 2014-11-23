package catalog.rule;

import catalog.AssemblyEngine;

/**
 */
public abstract class BaseRuleInterpreter {

    protected AssemblyEngine.CandidateProblem reportProblem(AssemblyEngine.CandidatePart candidate, String message) {
        AssemblyEngine.CandidateProblem problem = new AssemblyEngine.CandidateProblem(new RuleViolation(message), candidate);
        return problem;
    }
}
