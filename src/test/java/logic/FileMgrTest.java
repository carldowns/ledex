package logic;

import model.Assembly;
import model.Part;
import org.junit.Test;

import java.net.URL;
import java.util.List;

/**
 * Created by carl_downs on 10/10/14.
 */
public class FileMgrTest {

    @Test
    public void readAssemblyFile () throws Exception{

        URL url = getClass().getResource("/data/assembly.json");
        System.out.println ("URL " + url);

        FileMgr mgr = new FileMgr();
        Assembly asm = mgr.importAssembly(url.toURI());
        System.out.println ("assembly " + asm);

    }

    @Test
    public void readAssembliesFile () throws Exception{

        URL url = getClass().getResource("/data/assemblies.json");
        System.out.println ("URL " + url);

        FileMgr mgr = new FileMgr();
        List<Assembly> asms = mgr.importAssemblies(url.toURI());
        System.out.println ("assemblies " + asms);

    }

    @Test
    public void readPartFile () throws Exception{

        URL url = getClass().getResource("/data/part.json");
        System.out.println ("URL " + url);

        FileMgr mgr = new FileMgr();
        Part part = mgr.importPart(url.toURI());
        System.out.println ("part " + part.toString());
    }

    @Test
    public void readPartsFile () throws Exception{

        URL url = getClass().getResource("/data/parts.json");
        System.out.println ("URL " + url);

        FileMgr mgr = new FileMgr();
        List<Part> parts = mgr.importParts(url.toURI());
        System.out.println ("parts " + parts.toString());
    }
}
