package catalog;


import mgr.CatalogMgr;
import org.junit.Test;
import part.Part;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static catalog.AssemblyEngine.CandidateProduct;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing of circuit routing interpreter
 */
public class RICircuitTest {

    /**
     * tests simple 3-part circuit
     * @throws Exception
     */
    @Test
    public void testCircuit1 () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyEngine mgr = new AssemblyEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test1.circuit.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        url = getClass().getResource("/catalog/test1.circuit.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertEquals(1, candidates.size());

        CandidateProduct cp1 = candidates.get(0);
        List<String> partIDs = cp1.getPartIDs();
        org.junit.Assert.assertTrue(partIDs.size() == 3);
        org.junit.Assert.assertTrue(partIDs.contains("LIGHT-01"));
        org.junit.Assert.assertTrue(partIDs.contains("ADAPT-01"));
        org.junit.Assert.assertTrue(partIDs.contains("PLUG-01"));
    }

    /**
     * Testing 4-part circuit
     * Testing route=ignore, route=optional
     * Includes LINKABLE LED Strips with
     * Includes AC Adapter with connections to ignore
     *
     * @throws Exception
     */
    @Test
    public void testCircuit2 () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyEngine mgr = new AssemblyEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test2.circuit.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        url = getClass().getResource("/catalog/test2.circuit.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertEquals(1, candidates.size());

        CandidateProduct cp1 = candidates.get(0);
        List<String> partIDs = cp1.getPartIDs();
        org.junit.Assert.assertTrue(partIDs.size() == 4);
        org.junit.Assert.assertTrue(partIDs.contains("LIGHT1"));
        org.junit.Assert.assertTrue(partIDs.contains("ADAPT1"));
        org.junit.Assert.assertTrue(partIDs.contains("WIRE1"));
        org.junit.Assert.assertTrue(partIDs.contains("PLUG1"));
    }

    /**
     * test that we can successfully assemble 2 correctly connected
     * products from a set of 6 part candidates.
     */
    @Test
    public void testCircuit3 () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyEngine mgr = new AssemblyEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test3.circuit.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        url = getClass().getResource("/catalog/test3.circuit.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertEquals(2, candidates.size());

        CandidateProduct cp1 = candidates.get(0);
        List<String> partIDs = cp1.getPartIDs();
        org.junit.Assert.assertTrue(partIDs.size() == 5);
        org.junit.Assert.assertTrue(partIDs.contains("HARNESS-01"));
        org.junit.Assert.assertTrue(partIDs.contains("ADAPT-01"));
        org.junit.Assert.assertTrue(partIDs.contains("LEAD-01"));
        org.junit.Assert.assertTrue(partIDs.contains("PLUG-01"));
        org.junit.Assert.assertTrue(partIDs.contains("LIGHT-01"));

        CandidateProduct cp2 = candidates.get(1);
        List<String> part2IDs = cp2.getPartIDs();
        org.junit.Assert.assertTrue(part2IDs.size() == 5);
        org.junit.Assert.assertTrue(part2IDs.contains("HARNESS-01"));
        org.junit.Assert.assertTrue(part2IDs.contains("ADAPT-01"));
        org.junit.Assert.assertTrue(part2IDs.contains("LEAD-01"));
        org.junit.Assert.assertTrue(part2IDs.contains("PLUG-01"));
        org.junit.Assert.assertTrue(part2IDs.contains("LIGHT-02"));
    }

    /**
     * test that an assembly with a part that has male/female connection
     * type not matched is not considered a valid product candidate
     */
    @Test
    public void testCircuit4 () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyEngine mgr = new AssemblyEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test4.circuit.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        url = getClass().getResource("/catalog/test4.circuit.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertEquals(0, candidates.size());
    }
}
