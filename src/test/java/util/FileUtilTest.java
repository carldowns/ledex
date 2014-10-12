package util;

import model.Assembly;
import model.Part;
import org.junit.Test;
import util.FileUtil;

import java.net.URL;
import java.util.List;

/**
 * Ok
 * Created by carl_downs on 10/10/14.
 */
public class FileUtilTest {

    @Test
    public void readAssemblyFile () throws Exception{

        URL url = getClass().getResource("/data/assembly.json");
        System.out.println ("URL " + url);

        FileUtil mgr = new FileUtil();
        Assembly asm = mgr.importAssembly(url.toURI());
        System.out.println ("assembly " + asm);

    }

    @Test
    public void readAssembliesFile () throws Exception{

        URL url = getClass().getResource("/data/assemblies.json");
        System.out.println ("URL " + url);

        FileUtil mgr = new FileUtil();
        List<Assembly> asms = mgr.importAssemblies(url.toURI());
        System.out.println ("assemblies " + asms);

    }

    @Test
    public void readPartFile () throws Exception{

        URL url = getClass().getResource("/data/part.json");
        System.out.println ("URL " + url);

        FileUtil mgr = new FileUtil();
        Part part = mgr.importPart(url.toURI());
        System.out.println ("part " + part.toString());
    }

    @Test
    public void readPartsFile () throws Exception{

        URL url = getClass().getResource("/data/parts.json");
        System.out.println ("URL " + url);

        FileUtil mgr = new FileUtil();
        List<Part> parts = mgr.importParts(url.toURI());
        System.out.println ("parts " + parts.toString());
    }
}
