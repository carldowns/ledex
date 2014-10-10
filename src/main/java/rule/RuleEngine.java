package rule;

import java.util.ArrayList;
import java.util.List;
import model.Assembly;


public class RuleEngine {

    Assembly asm;
    ArrayList<RuleIssue> issues = new ArrayList<RuleIssue>();
    ArrayList<RuleInterpreter> interps = new ArrayList<RuleInterpreter>();
    
    {
        interps.add(new RIAmperage());
        interps.add(new RIVoltage());
        interps.add(new RIFunctionCompliance());
        interps.add(new RIPartCompatibility());
        interps.add(new RIConnectorCompatibility());
    }
    
    public RuleEngine (Assembly asm) {
        this.asm = asm;
        
        // applies all of the registered rule interpreters to the Assembly
        for (RuleInterpreter ri : interps) {
            List<RuleIssue> subissues = ri.evaluate(asm);
            if (subissues != null) {
                issues.addAll(subissues);
            }
        }
    }
    
    /**
     * 
     * @return
     */
    public boolean isValid () {
        return issues.size() == 0;
    }
    
    public List<RuleIssue> getIssues () {
        return issues;
    }
    
    public static class RuleIssue {
        String msg;
    }
}
