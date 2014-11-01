package catalog;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;


public class Function {

    @JsonProperty ("type")
    FunctionType type = FunctionType.UNKNOWN;

    @JsonProperty ("rules")
    List<Rule> rules = Lists.newArrayList();

    public Function() {}

    public Function(String functionTypeName) {
        type = FunctionType.valueOf(functionTypeName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Function)) return false;

        Function function = (Function) o;

        if (!rules.equals(function.rules)) return false;
        if (type != function.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + rules.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Function{" +
                "type=" + type +
                ", rules=" + rules +
                '}';
    }
}
