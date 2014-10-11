package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class Assembly {

    @JsonProperty("name")
    String name;

    @JsonProperty ("parts")
    List<Part> parts = new ArrayList<Part>();

    /**
     * Jackson
     */
    public Assembly () {
    }

    public Assembly (String asmName) {
        this.name = asmName;
    }
    
    public Assembly add(Part part) {
        parts.add(part);        
        return this;
    }
    
    public List<Part> getParts () {
        return parts;
    }

    @Override
    public String toString () {
        return "Assembly:" + name;
    }
}
