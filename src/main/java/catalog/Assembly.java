package catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Architectural Overview
 *
 * The catalog is defined as the collection of all possible (compatible) part combinations as defined by
 * Assembly rules.  An Assembly defines a set of Functions, each which act as a slot for 0..n Parts to
 * occupy.  Each Function slot is controlled by a set of Rules which determine what Parts may occupy that slot.
 *
 * Be applying all parts against all slots, we end up with a complete permutation of permissible combinations
 * what are then written as Products to the ProductPart tables.
 *
 * The ProductPart tables and auxiliary indices make it possible to move from part to product to part freely.
 */
public class Assembly {

    @JsonProperty("assemblyID")
    String assemblyID;

    @JsonProperty("name")
    String name;

    @JsonProperty("description")
    String description;

    @JsonIgnore
    String assemblyDocID;

    @JsonProperty ("functions")
    List<FunctionType> functions;

    @JsonProperty ("rules")
    List<Rule> rules = Lists.newArrayList();

    ///////////////////////
    // Identify Methods
    ///////////////////////

    public String getAssemblyID() {
        return assemblyID;
    }

    public void setAssemblyID(String assemblyID) {
        this.assemblyID = assemblyID;
    }

    public String getAssemblyDocID() {
        return assemblyDocID;
    }

    public void setAssemblyDocID(String assemblyDocID) {
        this.assemblyDocID = assemblyDocID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ///////////////////////
    // Function Methods
    ///////////////////////

    public List<FunctionType> getFunctionTypes() {
        return functions;
    }

    public void setFunctionTypes(List<FunctionType> functions) {
        this.functions = functions;
    }


    ///////////////////////
    // Rules Methods
    ///////////////////////

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    ///////////////////////
    // Overrides
    ///////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assembly)) return false;

        Assembly assembly = (Assembly) o;

        if (assemblyID != null ? !assemblyID.equals(assembly.assemblyID) : assembly.assemblyID != null) return false;
        if (functions != null ? !functions.equals(assembly.functions) : assembly.functions != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = assemblyID != null ? assemblyID.hashCode() : 0;
        result = 31 * result + (functions != null ? functions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Assembly{" +
                "assemblyID='" + assemblyID + '\'' +
                ", functions=" + functions +
                '}';
    }
}
