package util;

import catalog.Assembly;
import part.Part;
import org.junit.Test;
import supplier.Supplier;

import java.net.URL;
import java.util.List;

/**
 * Ok
 * Created by carl_downs on 10/10/14.
 */
public class FileUtilTest {

    @Test
    public void readAssembliesFile () throws Exception{

        URL url = getClass().getResource("/util/assemblies.json");
        System.out.println ("URL " + url);

        FileUtil mgr = new FileUtil();
        List<Assembly> asms = mgr.importAssemblies(url.toURI());
        System.out.println ("assemblies " + asms);

    }

    @Test
    public void readPart1File () throws Exception{

        URL url = getClass().getResource("/util/part1.json");
        System.out.println ("URL " + url);

        FileUtil mgr = new FileUtil();
        Part part = mgr.importPart(url.toURI());
        System.out.println ("part " + part.toString());
    }

    @Test
    public void readPart2File () throws Exception{

        URL url = getClass().getResource("/util/part2.json");
        System.out.println ("URL " + url);

        FileUtil mgr = new FileUtil();
        List<Part> parts = mgr.importParts(url.toURI());
        System.out.println ("parts " + parts.toString());
    }

    @Test
    public void readSupplierFile () throws Exception{

        URL url = getClass().getResource("/util/suppliers.json");
        System.out.println ("URL " + url);

        FileUtil mgr = new FileUtil();
        List<Supplier> parts = mgr.importSuppliers(url.toURI());
        System.out.println ("suppliers " + parts.toString());
    }
}
