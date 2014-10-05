package doc;

import java.util.ArrayList;
import java.util.List;
import rule.Rule;



public class Function {
    
    String name;
    List<Rule> rules = new ArrayList<Rule>();
    
    Function (String fcnName) {
        this.name = fcnName;
    }
    
    Function add (Rule r) {
        rules.add(r);
        return this;
    }
    
    public static enum FunctionName {
        POWER,
        LIGHT,
        CONTROLLER,
        HARNESS,
        SWITCH,
        SENSOR,
        PLUG,
        LEAD,
        CASE,
        CELL,
        CUSTOM,
    }
}
