package doc;

import java.util.ArrayList;
import java.util.List;


public class Set {

    String name;
    List<Function> functions = new ArrayList<Function>();
    
    Set (String setName) {      
        this.name = setName;
    }
    
    void add(Function f) {
        functions.add(f);
    }
}
