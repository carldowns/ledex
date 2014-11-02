package catalog;

import mgr.AssemblyMgr.*;

/**
 * verifies that all parts can be connected in a circuit
 */
public class RICircuit implements RuleInterpreter {

    public CandidateProblem evaluate(Assembly assembly, CandidateProduct candidate) {
        CandidateProblem report = new CandidateProblem();
        for (Rule rule : assembly.getRules()) {
            switch (rule.getType()) {

                case CIRCUIT_COMPLETE:
                    report.addProblems(evaluateCircuit(rule, candidate));
                    break;
            }
        }

        return report;
    }

    private CandidateProblem evaluateCircuit(Rule rule, CandidateProduct candidate) {

        // assume: we have a straight line or harness assembly
        // assume: plug lists wire positions 0,,N giving power, ground, ctrl

        // for each electrical part:

        // get connect points (0..N)
        // get plug type of each connect point
        // get function(s) the connect point is designed to connect with

        // Plug Types
        // best to describe plug types in the Assembly, with rules there, Parts just reference the plug type
        // get plug wire face positions, 2-pin (P,N), 3-pin (P,N,C) 4-pin (R,G,B,N)
        // get plug male or female gender


        // LATER: get voltage override for a connect point if present
        // LATER: deal with voltage drop, amplifiers for long circuits

        return null;
    }
}
