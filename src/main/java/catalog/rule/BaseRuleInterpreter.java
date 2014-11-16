package catalog.rule;

import catalog.CatalogEngine;

/**
 */
public abstract class BaseRuleInterpreter {

    protected CatalogEngine.CandidateProblem reportProblem(CatalogEngine.CandidatePart candidate, String message) {
        CatalogEngine.CandidateProblem problem = new CatalogEngine.CandidateProblem(new RuleViolation(message), candidate);
        return problem;
    }
}
