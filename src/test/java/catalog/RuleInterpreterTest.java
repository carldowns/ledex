package catalog;

import com.google.common.collect.Lists;
import mgr.AssemblyMgr;
import mgr.CatalogMgr;
import org.junit.Test;
import part.PartRec;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 */
public class RuleInterpreterTest {

    @Test
    public void testRIPower () {
//        CatalogMgr catalogMgr = mock(CatalogMgr.class);
//        AssemblyMgr mgr = new AssemblyMgr(catalogMgr);
//
//        // exactly one part per function yields one complete product
//        List<PartRec> parts = Lists.newArrayList();
//        parts.add(new PartRec("part-100","S1","AC Adapter 100mAH","POWER"));
//        parts.add(new PartRec("part-200","S1","LED 5050 Waterproof Light Strip","LIGHT"));
//        parts.add(new PartRec("part-300","S1","Threaded Jack Plug","PLUG"));
//        parts.add(new PartRec("part-400","S1","1 Amp Lead Wire","LEAD"));
//
//        when(catalogMgr.getAllPartRecs()).thenReturn(parts.iterator());
//
//        Assembly asm = new Assembly();
//
//        List<Function> functions = Lists.newArrayList();
//        functions.add(new Function ("POWER"));
//        functions.add(new Function ("LIGHT"));
//        functions.add(new Function ("PLUG"));
//        functions.add(new Function ("LEAD"));
//        asm.setFunctions(functions );
//        asm.setAssemblyID("asm-100");
//
//        List<AssemblyMgr.CandidateProduct> candidates = mgr.buildProductCandidates(asm);
//        org.junit.Assert.assertTrue(candidates.size() == 1);

    }
}
