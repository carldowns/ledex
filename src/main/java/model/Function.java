package model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import rule.Rule;



public class Function {

    @JsonProperty ("name")
    String name;

    @JsonProperty ("rules")
    List<Rule> rules = new ArrayList<Rule>();

    /**
     * default constructor for Jackson
     */

    public Function() {}

    Function (String fcnName) {
        this.name = fcnName;
    }
    
    Function add (Rule r) {
        rules.add(r);
        return this;
    }

}
