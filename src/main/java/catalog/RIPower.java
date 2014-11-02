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

    /**
     * evaluate power rules if any
     */
    public CandidateProblem evaluate(Assembly assembly, CandidateProduct candidate) {
        CandidateProblem report = new CandidateProblem();
        for (Rule rule : assembly.getRules()) {
            switch (rule.getType()) {

                case AMPERAGE_COMPATIBLE:
                    report.addProblems(evaluateAmperageCompatible(rule, candidate));
                    break;

                case VOLTAGE_COMPATIBLE:
                    report.addProblems(evaluateVoltageCompatible(rule, candidate));
                    break;

                case VOLTAGE_SPECIFIC:
                    report.addProblems(evaluateVoltageSpecific(rule, candidate));
                    break;
            }
        }

        return report;
    }

    private CandidateProblem evaluateAmperageCompatible(Rule rule, CandidateProduct candidate) {

        // get power parts source amperage
        // add up the amperage load of all non power parts
        // check to be sure power part(s) have enough amps to cover the load

        int cumulativeLoadMA = 0;
        int powerSourceMA = 0;

        for (CandidatePart candidatePart : candidate.getCandidateParts().values()) {

            Part part = candidatePart.getPart();
            if (!isAmperageRelevant(part.getFunctionType())) continue;

            List<PartProperty> props = part.getPropertiesOfType(PartPropertyType.AMPERAGE);
            for (PartProperty prop : props) {
                UnitConverter.UnitTypeValue unit = new UnitConverter(prop.getValue()).getAmperageType();

                switch (part.getFunctionType()) {
                    case POWER:
                        // FIXME: guard against two power parts for now
                        Preconditions.checkArgument(powerSourceMA == 0, "only 1 part per POWER function is supported");
                        powerSourceMA = Integer.valueOf(unit.toMilliAmps());
                        break;

                    case LIGHT:
                    case CONTROLLER:
                    case SWITCH:
                    case SENSOR:
                    case LEAD:
                        cumulativeLoadMA += Integer.valueOf(unit.toMilliAmps());
                        break;
                }
            }

        }

        if (cumulativeLoadMA > powerSourceMA) {
            return reportProblem(null,
                    " circuit milli-amp load of " + cumulativeLoadMA +
                            " greater than milli-amp source of " + powerSourceMA);
        }

        // conforms to rule
        return null;
    }

    private CandidateProblem evaluateVoltageCompatible(Rule rule, CandidateProduct candidate) {

        String targetVoltage = null; // unassigned
        for (CandidatePart candidatePart : candidate.getCandidateParts().values()) {

            Part part = candidatePart.getPart();
            if (!isVoltageRelevant(part.getFunctionType())) continue;

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
        Preconditions.checkNotNull(targetVoltage, "target voltage not specified");
        UnitConverter.UnitTypeValue targetUnit = new UnitConverter(targetVoltage).getVoltageType();

        for (CandidatePart candidatePart : candidate.getCandidateParts().values()) {

            Part part = candidatePart.getPart();
            if (!isVoltageRelevant(part.getFunctionType())) continue;

            List<PartProperty> props = part.getPropertiesOfType(PartPropertyType.VOLTAGE);

            for (PartProperty prop : props) {
                UnitConverter.UnitTypeValue unit = new UnitConverter(prop.getValue()).getVoltageType();

                // check to see if we have a voltage match
                if (unit.getValue().equals(targetUnit.getValue()) == false) {
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
