package rule;

import java.util.List;
import doc.Assembly;
import doc.Part;
import rule.RuleEngine.RuleIssue;


public class RIAmperage implements RuleInterpreter {
    
    /**
     * verifies that if there are multiple power sources, each will provide the same AMP output.
     * verifies that all parts consuming Amps will together fall below the threshold of AMP output provided by the power source
     */
    public List<RuleIssue> evaluate (Assembly asm) {
        for (Part part : asm.getParts()) {
            System.out.println (this.getClass().getName() + " evaluating " + part.getName());
        }
        return null;
    }
}
