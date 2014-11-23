package catalog;

import mgr.CatalogMgr;
import org.junit.Test;
import part.Part;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Assembly instructions with no rules meaning any combination of parts along functional lines is tested
 */
public class AssemblyPermutationTest {

    @Test
    public void test1Combination () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyEngine mgr = new AssemblyEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test1.permute.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        // no rules, any combination within functional limits allowed
        // 1 part per function, 4 functions, expecting 16 combinations

        url = getClass().getResource("/catalog/test1.permute.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<AssemblyEngine.CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertTrue(candidates.size() == 1);

    }

    @Test
    public void test2Combinations () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyEngine mgr = new AssemblyEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test2.permute.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        // no rules, any combination within functional limits allowed
        // 2 adapter options remainder one choice per function, expecting 2 combinations

        url = getClass().getResource("/catalog/test2.permute.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<AssemblyEngine.CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertTrue(candidates.size() == 2);

    }

    @Test
    public void test16Combinations () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyEngine mgr = new AssemblyEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test3.permute.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        // no rules, any combination within functional limits allowed
        // 2 parts per function, 4 functions, expecting 16 combinations

        url = getClass().getResource("/catalog/test3.permute.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<AssemblyEngine.CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertTrue(candidates.size() == 16);

    }
}
