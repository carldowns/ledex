package rule;

import java.util.List;
import doc.Assembly;
import rule.RuleEngine.RuleIssue;


public interface RuleInterpreter {

    public List<RuleIssue> evaluate (Assembly asm);
}
