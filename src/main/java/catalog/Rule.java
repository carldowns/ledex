package catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;


public class Rule {

    @JsonProperty("type")
    private final RuleType type;

    @Nullable
    @JsonProperty("properties")
    private final Map<String, String> properties = Maps.newHashMap();

    ///////////////////
    // Construct
    ///////////////////


    Rule(String type) {
        this.type = RuleType.valueOf(type);
    }

    ///////////////////
    // Helpers
    ///////////////////

    public RuleType getType() {
        return type;
    }

    void addProperty(String key, String value) {
        properties.put(key, value);
    }

    String getProperty(String key) {
        return properties.get(key);
    }

    @Nullable
    public Map<String, String> getProperties() {
        return properties;
    }

    ///////////////////
    // Overrides
    ///////////////////

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
                (properties != null ? ", properties=" + properties : "") +
                '}';
    }
}
