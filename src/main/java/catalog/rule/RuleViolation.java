package catalog.rule;

/**
 * Created by carl_downs on 10/25/14.
 */
public class RuleViolation {
    String msg;

    public RuleViolation (String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}