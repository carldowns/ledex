package mgr;

import catalog.Assembly;
import catalog.Function;
import com.google.common.collect.Lists;
import org.junit.Test;
import part.PartRec;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by carl_downs on 10/29/14.
 */
public class AssemblyMgrTest {

    @Test
    public void test1Combination () {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyMgr mgr = new AssemblyMgr(catalogMgr);

        // exactly one part per function yields one complete product
        List<PartRec> parts = Lists.newArrayList();
        parts.add(new PartRec("part-100","S1","AC Adapter 100mAH","POWER"));
        parts.add(new PartRec("part-200","S1","LED 5050 Waterproof Light Strip","LIGHT"));
        parts.add(new PartRec("part-300","S1","Threaded Jack Plug","PLUG"));
        parts.add(new PartRec("part-400","S1","1 Amp Lead Wire","LEAD"));

        when(catalogMgr.getAllPartRecs()).thenReturn(parts.iterator());

        Assembly asm = new Assembly();

        List<Function> functions = Lists.newArrayList();
        functions.add(new Function ("POWER"));
        functions.add(new Function ("LIGHT"));
        functions.add(new Function ("PLUG"));
        functions.add(new Function ("LEAD"));
        asm.setFunctions(functions );
        asm.setAssemblyID("asm-100");

        List<AssemblyMgr.CandidateProduct> candidates = mgr.buildProductCandidates(asm);
        org.junit.Assert.assertTrue(candidates.size() == 1);

    }

    @Test
    public void test2Combinations () {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyMgr mgr = new AssemblyMgr(catalogMgr);

        // Two adapter choices yields 2 complete products
        List<PartRec> parts = Lists.newArrayList();
        parts.add(new PartRec("part-100","S1","AC Adapter 100mAH","POWER"));
        parts.add(new PartRec("part-101","S1","AC Adapter 500mAH","POWER"));
        parts.add(new PartRec("part-200","S1","LED 5050 Waterproof Light Strip","LIGHT"));
        parts.add(new PartRec("part-301","S1","Threaded Jack Plug","PLUG"));
        parts.add(new PartRec("part-403","S1","20 Amp Lead Wire","LEAD"));

        when(catalogMgr.getAllPartRecs()).thenReturn(parts.iterator());

        Assembly asm = new Assembly();

        List<Function> functions = Lists.newArrayList();
        functions.add(new Function ("POWER"));
        functions.add(new Function ("LIGHT"));
        functions.add(new Function ("PLUG"));
        functions.add(new Function ("LEAD"));
        asm.setFunctions(functions );
        asm.setAssemblyID("asm-100");

        List<AssemblyMgr.CandidateProduct> candidates = mgr.buildProductCandidates(asm);
        org.junit.Assert.assertTrue(candidates.size() == 2);

    }

    @Test
    public void test16Combinations () {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyMgr mgr = new AssemblyMgr(catalogMgr);

        List<PartRec> parts = Lists.newArrayList();
        parts.add(new PartRec("part-100","S1","AC Adapter 100mAH","POWER"));
        parts.add(new PartRec("part-101","S1","AC Adapter 500mAH","POWER"));

        parts.add(new PartRec("part-200","S1","LED 5050 Waterproof Light Strip","LIGHT"));
        parts.add(new PartRec("part-201","S1","LED 3528 Waterproof Light Strip","LIGHT"));

        parts.add(new PartRec("part-300","S1","Threaded Jack Plug","PLUG"));
        parts.add(new PartRec("part-301","S1","Threaded Jack Plug","PLUG"));

        parts.add(new PartRec("part-400","S1","1 Amp Lead Wire","LEAD"));
        parts.add(new PartRec("part-401","S1","2 Amp Lead Wire","LEAD"));

        when(catalogMgr.getAllPartRecs()).thenReturn(parts.iterator());

        Assembly asm = new Assembly();

        List<Function> functions = Lists.newArrayList();
        functions.add(new Function ("POWER"));
        functions.add(new Function ("LIGHT"));
        functions.add(new Function ("PLUG"));
        functions.add(new Function ("LEAD"));
        asm.setFunctions(functions );
        asm.setAssemblyID("asm-100");

        List<AssemblyMgr.CandidateProduct> candidates = mgr.buildProductCandidates(asm);
        org.junit.Assert.assertTrue(candidates.size() == 16);

    }

    @Test
    public void test160Combinations () {

        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyMgr mgr = new AssemblyMgr(catalogMgr);

        List<PartRec> parts = Lists.newArrayList();
        parts.add(new PartRec("part-100","S1","AC Adapter 100mAH","POWER"));
        parts.add(new PartRec("part-101","S1","AC Adapter 500mAH","POWER"));
        parts.add(new PartRec("part-102","S1","AC Adapter 1AH","POWER"));
        parts.add(new PartRec("part-103","S1","AC Adapter 2AH","POWER"));
        parts.add(new PartRec("part-104","S1","AC Adapter 5AH","POWER"));

        parts.add(new PartRec("part-200","S1","LED 5050 Waterproof Light Strip","LIGHT"));
        parts.add(new PartRec("part-201","S1","LED 3528 Waterproof Light Strip","LIGHT"));
        parts.add(new PartRec("part-202","S1","LED 3528 Light Strip","LIGHT"));
        parts.add(new PartRec("part-203","S1","LED 5050 Light Strip","LIGHT"));

        parts.add(new PartRec("part-300","S1","Threaded Jack Plug","PLUG"));
        parts.add(new PartRec("part-301","S1","Threaded Jack Plug","PLUG"));

        parts.add(new PartRec("part-400","S1","1 Amp Lead Wire","LEAD"));
        parts.add(new PartRec("part-401","S1","2 Amp Lead Wire","LEAD"));
        parts.add(new PartRec("part-402","S1","5 Amp Lead Wire","LEAD"));
        parts.add(new PartRec("part-403","S1","20 Amp Lead Wire","LEAD"));

        when(catalogMgr.getAllPartRecs()).thenReturn(parts.iterator());

        Assembly asm = new Assembly();

        List<Function> functions = Lists.newArrayList();
        functions.add(new Function ("POWER"));
        functions.add(new Function ("LIGHT"));
        functions.add(new Function ("PLUG"));
        functions.add(new Function ("LEAD"));
        asm.setFunctions(functions );
        asm.setAssemblyID("asm-100");

        List<AssemblyMgr.CandidateProduct> candidates = mgr.buildProductCandidates(asm);
        org.junit.Assert.assertTrue(candidates.size() == 160);
    }
}
