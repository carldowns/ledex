package catalog;


import mgr.AssemblyMgr;
import static mgr.AssemblyMgr.CandidateProduct;
import mgr.CatalogMgr;
import org.junit.Test;
import part.Part;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing of
 */
public class RIPowerTest {

    @Test
    public void testPower1 () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyMgr mgr = new AssemblyMgr(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test1.power.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        // testing rules
        // "type": "VOLTAGE_COMPATIBLE"
        // "type": "AMPERAGE_COMPATIBLE"

        url = getClass().getResource("/catalog/test1.power.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<AssemblyMgr.CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertEquals(2, candidates.size());

        // 4 parts
        // 4.5 volt and 12 volt mix of adapter, light function types
        // should come up with 2 valid product candidates

        CandidateProduct cp1 = candidates.get(0);
        List<String> partIDs = cp1.getPartIDs();
        org.junit.Assert.assertTrue(partIDs.size() == 2);
        org.junit.Assert.assertTrue(partIDs.contains("LIGHT-00-010-12"));
        org.junit.Assert.assertTrue(partIDs.contains("ADAPT-04-021-12"));

        CandidateProduct cp2 = candidates.get(1);
        partIDs = cp2.getPartIDs();
        org.junit.Assert.assertTrue(partIDs.size() == 2);
        org.junit.Assert.assertTrue(partIDs.contains("LIGHT-00-009-4.5"));
        org.junit.Assert.assertTrue(partIDs.contains("ADAPT-04-022-4.5"));
    }

    @Test
    public void testPower2 () throws Exception {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyMgr mgr = new AssemblyMgr(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource("/catalog/test2.power.parts.json");
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        // testing rules
        // "type": "VOLTAGE_COMPATIBLE"
        // "type": "AMPERAGE_COMPATIBLE"

        url = getClass().getResource("/catalog/test2.power.assembly.json");
        Assembly assembly = util.importAssembly(url.toURI());

        List<AssemblyMgr.CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        org.junit.Assert.assertEquals(2, candidates.size());

        CandidateProduct cp1 = candidates.get(0);
        List<String> partIDs = cp1.getPartIDs();
        org.junit.Assert.assertTrue(partIDs.size() == 2);
        org.junit.Assert.assertTrue(partIDs.contains("LIGHT-12V-500mA"));
        org.junit.Assert.assertTrue(partIDs.contains("ADAPT-12V-2A"));

        CandidateProduct cp2 = candidates.get(1);
        partIDs = cp2.getPartIDs();
        org.junit.Assert.assertTrue(partIDs.size() == 2);
        org.junit.Assert.assertTrue(partIDs.contains("LIGHT-12V-1A"));
        org.junit.Assert.assertTrue(partIDs.contains("ADAPT-12V-2A"));
    }
}
