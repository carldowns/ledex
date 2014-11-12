package catalog;

import com.google.common.collect.Lists;
import mgr.AssemblyMgr.CandidatePart;
import mgr.AssemblyMgr.CandidateProblem;
import mgr.AssemblyMgr.CandidateProduct;
import part.Part;
import part.PartConnection;

import java.util.Arrays;
import java.util.List;

/**
 * This uses recursion and does a brute force routing which works reliably
 * but is not going to handle production cases because it is O(n!)
 */
public class RICircuitRecursive implements RuleInterpreter {

    List<RoutePoint> points = Lists.newArrayList();
    int permutations;
    boolean debug = true;

    public CandidateProblem evaluate(Assembly assembly, CandidateProduct candidate) {
        CandidateProblem report = new CandidateProblem();
        for (Rule rule : assembly.getRules()) {
            switch (rule.getType()) {

                case CIRCUIT_COMPLETE:
                    report.addProblems(evaluateRoute(rule, candidate));
                    break;
            }
        }

        return report;
    }

    // get plug wire face positions, 2-pin (P,N), 3-pin (P,N,C) 4-pin (R,G,B,N)
    // LATER: get voltage for a connect point if present
    // LATER: deal with voltage drop, amplifiers for long circuits

    /**
     *
     * @param rule
     * @param candidate
     * @return
     */
    private CandidateProblem evaluateRoute(Rule rule, CandidateProduct candidate) {

        CandidateProblem report = new CandidateProblem();
        // generate an ordered list of route segments for each part.
        // iterate through all permutations of the list
        // for each permutation, if all adjacency rules all obeyed, return true.
        // else there is no workable connection, reject the candidate

        for (CandidatePart candidatePart : candidate.getCandidateParts().values()) {
            Part part = candidatePart.getPart();
            for (PartConnection pc : part.getConnections()) {
                if (pc.isConnectIgnorable())
                    continue;

                RoutePoint point = new RoutePoint(candidatePart, pc);
                points.add(point);
            }
        }

        RoutePoint[] buffer = new RoutePoint[points.size()];
        boolean valid = evalPermutations(buffer, 0);
        if (!valid) {
            RuleViolation problem = new RuleViolation("unable to make all required connections");
            report.addProblem(problem, null); // TODO FIXME
        }

        if (debug) {
            System.out.println();
            System.out.println(candidate.getPartIDs());
            System.out.println("route points:" + buffer.length);
            System.out.println("permutations:" + this.permutations);
            for (RoutePoint rp : buffer) {
                System.out.println(rp);
            }
        }

        return report;
    }

    /**
     * recursively build permutations looking for one that is valid
     * @param buffer
     * @param current
     * @return
     */
    private boolean evalPermutations(RoutePoint[] buffer, int current) {
        // exit condition
        if (current == buffer.length) {
            return isOrderValid(buffer);
        }

        for (int x = 0; x < points.size(); x++) {
            RoutePoint point = points.get(x);
            if (point.isInUse())
                continue;

            buffer[current] = point;
            point.setInUse();
            boolean result = evalPermutations(buffer, current + 1);
            point.clearInUse();

            // return now if we found a valid permutation
            if (result == true) {
                return true;
            }
        }
        return false;
    }

    private boolean isOrderValid(RoutePoint[] buffer) {

        permutations++;
        for (int i = 0; i < buffer.length; i++) {
            RoutePoint current = buffer[i];

            // TODO: enable this efficiency after the whole thing is debugged.
            if (current.isMatched())
                continue;

            // if current is {type}.male.required, right must be {type}.female.{required|optional}
            // if current is {type}.female.required, left must be {type}.male.{required|optional}
            // left {part id} cannot equal right {part id}

            if (current.connection.isConnectRequired()) {
                if (current.connection.isMale()) {
                    if (i < buffer.length - 1) {
                        RoutePoint right = buffer[i+1];
                        if (right.connection.isFemale()) {
                            if (right.connection.getType().equals(current.connection.getType())) {
                                if (right.connection.isConnectRequiredOrOptional()) {
                                    if (right.part != current.part) {
                                        current.setMatched();
                                        right.setMatched();
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    if (i > 0) {
                        RoutePoint left = buffer[i-1];
                        if (left.connection.isMale()) {
                            if (left.connection.getType().equals(current.connection.getType())) {
                                if (left.connection.isConnectRequiredOrOptional()) {
                                    if (left.part != current.part) {
                                        current.setMatched();
                                        left.setMatched();
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
                // required connection for this part not satisfied, order is invalid
                return false;
            }
        }
        return true;
    }

    private class RoutePoint {
        CandidatePart part;
        PartConnection connection;
        boolean inUse;
        boolean isMatched;// TODO: a bit sneaky make sure this state is consistent

        RoutePoint(CandidatePart part, PartConnection connection) {
            this.part = part;
            this.connection = connection;
        }

        boolean isInUse () {
            return inUse;
        }

        void setInUse () {
            this.inUse = true;
        }

        void clearInUse () {
            this.inUse = false;
            isMatched = false;
        }

        boolean isMatched () {
            return isMatched;
        }

        void setMatched () {
            this.isMatched = true;
        }

        @Override
        public String toString () {
            return part.getPart().getPartID() + " " + connection.toString();
        }
    }
}
