package catalog.rule;

import catalog.Assembly;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import catalog.AssemblyEngine.CandidatePart;
import catalog.AssemblyEngine.CandidateProblem;
import catalog.AssemblyEngine.CandidateProduct;
import part.Part;
import part.PartConnection;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This strategy organizes connections by types, attempts to connect them
 * greedily.  If it fails to find a match, it rotates connections in relation to
 * each other like a circular queue to see if we can find positional matches.
 * This may seem odd but we have discovered through a lot of trial and error
 * (mostly error) that position of connections in relation to each other really matters.
 */

public class RICircuit implements RuleInterpreter {

    // Connection Type --> List of ConnectionEntry objects
    Map<String, LinkedList<ConnectionEntry>> map = Maps.newHashMap();

    public CandidateProblem evaluate(Assembly assembly, CandidateProduct candidate) {
        CandidateProblem report = new CandidateProblem();
        for (Rule rule : assembly.getRules()) {
            switch (rule.getType()) {

                case CIRCUIT_COMPLETE:
                    report.addProblems(evaluateConnections(rule, candidate));
                    break;
            }
        }

        return report;
    }

    private CandidateProblem evaluateConnections(Rule rule, CandidateProduct candidate) {
        CandidateProblem report = new CandidateProblem();

        for (CandidatePart candidatePart : candidate.getCandidateParts().values()) {
            Part part = candidatePart.getPart();
            for (PartConnection pc : part.getConnections()) {

                if (pc.isConnectIgnorable())
                    continue;

                LinkedList<ConnectionEntry> connectionList = map.get(pc.getType());
                if (connectionList == null) {
                    connectionList = Lists.newLinkedList();
                    map.put(pc.getType(), connectionList);
                }

                connectionList.add(new ConnectionEntry(part, pc));
            }
        }

        for (String type : map.keySet()) {
            LinkedList<ConnectionEntry> connections = map.get(type);
            int rotations = connections.size();
            int unconnected = 0;
            while (--rotations > 0) {
                unconnected = evaluateConnectionsOfType(connections);
                if (unconnected == 0)
                    break;

                // if we did not get a 100% match for required connections
                // rotate the connections and try again
                cleanAndRotate (connections);
            }

            if (unconnected > 0) {
                RuleViolation rv = new RuleViolation("not able to map all required connections of type " + type);
                report.addProblem(rv, null);
                break;
            }
        }


        return report;
    }

    private void cleanAndRotate (LinkedList<ConnectionEntry> connections) {
        ConnectionEntry target = connections.removeFirst();
        connections.addLast(target);

        for (ConnectionEntry connection : connections) {
            connection.isConnected = false;
        }
    }

    private int evaluateConnectionsOfType(LinkedList<ConnectionEntry> connections) {
        if (evaluationPass(connections, true) == 0)
            return 0;

        return evaluationPass(connections, false);
    }

    private int evaluationPass(List<ConnectionEntry> connections, boolean skipOptional) {

        int unconnectedCount = 0;
        for (ConnectionEntry current : connections) {
            unconnectedCount += pair(current, connections, skipOptional) ? 0 : 1;
        }

        return unconnectedCount;
    }

    private boolean pair(ConnectionEntry current, List<ConnectionEntry> connections, boolean skipOptional) {

        if (current.isConnected) {
            return true;
        }

        // if this connection is optional, skip it always. If another part on
        // a later iteration has a required connection that this optional
        // connection can meet, it will be used at that time

        if (current.partConnection.isConnectOptional()) {
            return true;
        }

        for (ConnectionEntry other : connections) {

            // do not pair part connections to themselves
            if (current.part.getPartID().equals(other.part.getPartID())) {
                continue;
            }

            // other entry is already connected, skip
            if (other.isConnected) {
                continue;
            }

            if (other.partConnection.isConnectOptional() && skipOptional) {
                continue;
            }

            // verify types match
            Preconditions.checkArgument(current.partConnection.getType().equals(other.partConnection.getType()));

            if (current.partConnection.isFemale() && other.partConnection.isMale()) {
                current.isConnected = other.isConnected = true;
                return true;
            }

            if (current.partConnection.isMale() && other.partConnection.isFemale()) {
                current.isConnected = other.isConnected = true;
                return true;
            }
        }
        return false;
    }

    private class ConnectionEntry {
        Part part;
        PartConnection partConnection;
        boolean isConnected = false;

        ConnectionEntry(Part part, PartConnection partConnection) {
            this.part = part;
            this.partConnection = partConnection;
        }
    }
}
