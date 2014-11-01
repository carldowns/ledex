package catalog;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;


public class Rule {

    @JsonProperty("type")
    String type;

    @JsonProperty ("properties")
    Map<String, String> properties = new HashMap<>();

    Rule(String type, String key, String value) {
        this.type = type;
        properties.put(key, value);
    }
    
    void addProperty (String key, String value) {
        properties.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;

        Rule rule = (Rule) o;

        if (!properties.equals(rule.properties)) return false;
        if (type != null ? !type.equals(rule.type) : rule.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + properties.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "type='" + type + '\'' +
                ", properties=" + properties +
                '}';
    }
}
