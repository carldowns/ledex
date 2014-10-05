package doc;

import java.util.ArrayList;
import java.util.List;


public class Assembly {

    String name;
    
    // name of the set definition that the assembly conforms to
//    Set set;
    
    // quantity --> parts
    List<Part> parts = new ArrayList<Part>();
       
    public Assembly (String asmName) {
        this.name = asmName;
    }

//    public Assembly (Set set, String asmName) {
//        this.set = set;
//        this.name = asmName;
//    }
    
    public Assembly add(Part part) {
        parts.add(part);        
        return this;
    }
    
    public List<Part> getParts () {
        return parts;
    }
}
