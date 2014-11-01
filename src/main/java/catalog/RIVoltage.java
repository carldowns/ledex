package catalog;

import com.google.common.collect.Maps;
import mgr.AssemblyMgr;
import mgr.CatalogMgr;
import part.Part;
import part.PartProperty;
import part.PartPropertyType;
import part.PartRec;
import util.UnitConverter;

import java.util.List;
import java.util.Map;


public class RIVoltage implements IRuleInterpreter {

    CatalogMgr mgr;
    /**
     * verifies that all electrical parts are operating within at the prescribed voltage
     */
    public AssemblyMgr.CandidateProblem evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {

        // get the rules associated with the 'power' function.
        // if there are none, there is no restriction, so return null (everything is allowed)
        // if there are rules, evaluate them.


        ///////////////
        // rule 1
        ///////////////

        // all (function:power) parts of must match voltage

//        for (AssemblyMgr.CandidatePart partCandidate : candidate.getCandidateParts().values()) {
//            PartRec part = partCandidate.getPart();
//            part.getFunctionType();
//        }

        ///////////////
        // rule 2
        ///////////////

        // all (function:power) parts must match an explicit voltage and current type

        Function f = assembly.getFunction(FunctionType.POWER);
        for (Rule r : f.rules ) {
            String targetVoltage = r.getProperty (PartPropertyType.VOLTAGE.name());
            if (targetVoltage != null) {
                return requireMatchVoltage (candidate, targetVoltage);
            }

        }

        return null;
    }

    private AssemblyMgr.CandidateProblem requireMatchVoltage (AssemblyMgr.CandidateProduct candidate, String voltage) {

        for (FunctionType type : candidate.getCandidateParts().keySet()) {
            if (FunctionType.POWER.equals (type)) {

                // fetch part of function:power
                AssemblyMgr.CandidatePart candidatePart = candidate.getCandidateParts().get(type);
                PartRec rec = candidatePart.getPart();
                Part part = mgr.getPart(rec.getPartId());

                // get part properties of type:voltage
                List<PartProperty> props = part.getPropertiesFor(PartPropertyType.VOLTAGE);
                for (PartProperty prop : props) {
                    UnitConverter.UnitTypeValue unit = new UnitConverter(prop.getValue()).getVoltageType();
                    if (unit.getValue().equals(voltage) == false) {
                        return reportProblem (candidatePart, "non-conforming " + voltage);
                    }
                }
            }
        }

        // conforms
        return null;
    }

    // TODO: move to an abstract base so all the interpreters can use it
    private AssemblyMgr.CandidateProblem reportProblem (AssemblyMgr.CandidatePart candidate, String message) {
        AssemblyMgr.CandidateProblem problem = new AssemblyMgr.CandidateProblem(new RuleViolation(message), candidate);
        return problem;
    }
}
