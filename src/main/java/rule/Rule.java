package rule;

import java.util.HashMap;
import java.util.Map;


public class Rule {
    String type;
    Map<String, String> properties = new HashMap<String,String>();
    
    Rule (String type) {
        this.type = type;
    }
    
    Rule (String type, String key, String value) {
        this.type = type;
        properties.put(key, value);
    }
    
    void addProperty (String key, String value) {
        properties.put(key, value);
    }
}
