package catalog;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Function {

    @JsonProperty ("type")
    FunctionType type = FunctionType.UNKNOWN;

    public Function() {}

    public Function(String functionTypeName) {
        type = FunctionType.valueOf(functionTypeName);
    }

    public FunctionType getType() {
        return type;
    }

    public void setType(FunctionType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Function)) return false;

        Function function = (Function) o;

        if (type != function.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Function{" +
                "type=" + type +
                '}';
    }
}
