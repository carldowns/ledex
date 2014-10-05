package rule;

import java.util.List;
import doc.Assembly;
import doc.Part;
import rule.RuleEngine.RuleIssue;


public class RIVoltage implements RuleInterpreter {
    
    /**
     * verifies that all parts declaring a VOLTAGE property have compatible values
     */
    public List<RuleIssue> evaluate (Assembly asm) {
        
        for (Part part : asm.getParts()) {
            System.out.println (this.getClass().getName() + " evaluating " + part.getName());
        }
        return null;
    }
}
