package catalog;

import com.google.common.base.Preconditions;
import mgr.AssemblyMgr.CandidatePart;
import mgr.AssemblyMgr.CandidateProblem;
import mgr.AssemblyMgr.CandidateProduct;
import mgr.CatalogMgr;
import part.Part;
import part.PartProperty;
import part.PartPropertyType;
import part.PartRec;
import util.UnitConverter;

import java.util.List;


public class RIPower extends BaseRuleInterpreter implements RuleInterpreter {

    // FIXME @Inject
    CatalogMgr mgr;

    /**
     * evaluate voltage rules
     */
    public CandidateProblem evaluate(Assembly assembly, CandidateProduct candidate) {
        for (Rule rule : assembly.getRules()) {
            switch (rule.getType()) {
                case AMPERAGE_COMPATIBLE:
                    return evaluateAmperageCompatible(rule, candidate);
                case VOLTAGE_COMPATIBLE:
                    return evaluateVoltageCompatible(rule, candidate);
                case VOLTAGE_SPECIFIC:
                    return evaluateVoltageSpecific(rule, candidate);
            }
        }

        // confirms to all rules
        return null;
    }

    private CandidateProblem evaluateAmperageCompatible(Rule rule, CandidateProduct candidate) {

        // add up the amp load of all non power parts
        // check to be sure power part(s) have enough amps to cover the load

        int cumulativeLoadAmps = 0;
        int powerSourceAmps = 0;

        for (CandidatePart candidatePart : candidate.getCandidateParts().values()) {
            PartRec rec = candidatePart.getPart();
            Part part = mgr.getPart(rec.getPartId());
            List<PartProperty> props = part.getPropertiesOfType(PartPropertyType.AMPERAGE);
            for (PartProperty prop : props) {
                UnitConverter.UnitTypeValue unit = new UnitConverter(prop.getValue()).getVoltageType();

                switch (rec.getFunctionType()) {
                    case POWER:
                        Preconditions.checkArgument(powerSourceAmps == 0); // TODO: guard against two power functions for now
                        powerSourceAmps = Integer.valueOf(unit.getValue());
                        break;

                    case LIGHT:
                    case CONTROLLER:
                    case SWITCH:
                    case SENSOR:
                    case LEAD:
                        cumulativeLoadAmps += Integer.valueOf(unit.getValue());
                        break;
                }
            }

            if (cumulativeLoadAmps > powerSourceAmps) {
                return reportProblem(candidatePart,
                        " cumulative amp load of " + cumulativeLoadAmps +
                                " greater than amp source of " + powerSourceAmps);
            }
        }

        // conforms to rule
        return null;
    }

    private CandidateProblem evaluateVoltageCompatible(Rule rule, CandidateProduct candidate) {

        String targetVoltage = null; // unassigned
        for (CandidatePart candidatePart : candidate.getCandidateParts().values()) {
            PartRec rec = candidatePart.getPart();
            if (!isVoltageRelevant(rec.getFunctionType())) continue;

            Part part = mgr.getPart(rec.getPartId());
            List<PartProperty> props = part.getPropertiesOfType(PartPropertyType.VOLTAGE);
            for (PartProperty prop : props) {
                UnitConverter.UnitTypeValue unit = new UnitConverter(prop.getValue()).getVoltageType();

                // assign voltage of first participating candidate
                if (targetVoltage == null) {
                    targetVoltage = unit.getValue();
                } else if (unit.getValue().equals(targetVoltage) == false) {
                    return reportProblem(candidatePart, " not voltage compatible " + targetVoltage);
                }
            }
        }

        // conforms to rule
        return null;
    }

    private CandidateProblem evaluateVoltageSpecific(Rule rule, CandidateProduct candidate) {

        String targetVoltage = rule.getProperty(PartPropertyType.VOLTAGE.name());
        for (CandidatePart candidatePart : candidate.getCandidateParts().values()) {
            PartRec rec = candidatePart.getPart();
            if (!isVoltageRelevant(rec.getFunctionType())) continue;

            Part part = mgr.getPart(rec.getPartId());
            List<PartProperty> props = part.getPropertiesOfType(PartPropertyType.VOLTAGE);

            for (PartProperty prop : props) {
                UnitConverter.UnitTypeValue unit = new UnitConverter(prop.getValue()).getVoltageType();

                // check to see if we have a voltage match
                if (unit.getValue().equals(targetVoltage) == false) {
                    return reportProblem(candidatePart, " does not match voltage requirement of " + targetVoltage);
                }
            }
        }

        // conforms to rule
        return null;
    }

    private boolean isVoltageRelevant(FunctionType functionType) {
        switch (functionType) {
            case POWER:
            case LIGHT:
            case CONTROLLER:
            case SWITCH:
            case SENSOR:
                return true;
        }
        return false;
    }

    private boolean isAmperageRelevant(FunctionType functionType) {
        switch (functionType) {
            case POWER:
            case LIGHT:
            case CONTROLLER:
            case SWITCH:
            case SENSOR:
            case LEAD:
                return true;
        }
        return false;
    }
}
