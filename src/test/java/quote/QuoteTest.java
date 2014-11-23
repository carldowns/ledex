package quote;

import app.ApplicationDBI;
import app.CatConfiguration;
import app.CatHealth;
import catalog.Assembly;
import catalog.AssemblyEngine;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.sun.xml.internal.xsom.impl.WildcardImpl;
import io.dropwizard.setup.Environment;
import mgr.CatalogMgr;
import mgr.SupplierMgr;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import part.Part;
import catalog.Product;
import catalog.dao.CatalogPart;
import part.dao.PartDoc;
import part.dao.PartSQL;
import task.PingTask;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
@RunWith(JukitoRunner.class)
public class QuoteTest {

//    public static class GuiceTestModule extends JukitoModule {
//        protected void configureTest() {
//            bind(CatalogMgr.class);
//            bind(QuotePartResolver.class);
//        }
//    };

    @Inject QuoteEngine engine;

//    @Before
//    public void setupMocks (CatalogMgr mgr) {
//        when(mgr.getPart(anyString())).thenReturn(new Part());
//    }
    @Before
    public void setupMocks (PartSQL partSQL) {
        when(partSQL.getCurrentPartDoc(anyString())).thenReturn(new PartDoc());
    }

    @Test
    public void dummy(){}

    //@Test
    public void testQuotePartResolver () throws Exception {

        String assemblyURI = "/quote/test1.quote.assembly.json";
        String partsURI    = "/quote/test1.quote.parts.json";

        CreateQuoteCmd cmd = new CreateQuoteCmd();
        cmd.getQuote();
        cmd.setCustomerID("1");
        cmd.setProjectName("testQuotePartResolver");
        int quantity = 100;

        // primary command interface
        for (Product product : getTestProducts (assemblyURI, partsURI)) {
            cmd.addLineItem(Integer.toString(quantity), product);
        }

        engine.evaluate(cmd);
    }

    private List<Product> getTestProducts (String assemblyURI, String partsURI)  throws Exception {

        List<Product> products = Lists.newArrayList();
        int counter = 1000;
        for (AssemblyEngine.CandidateProduct candidateProduct : getCandidateProducts(assemblyURI, partsURI)) {
            Product product = new Product();
            product.setProductID("PRODUCT-" + counter++);
            products.add(product);

            for (AssemblyEngine.CandidatePart candidatePart : candidateProduct.getCandidateParts().values()) {
                Part part = candidatePart.getPart();

                CatalogPart catalogPart = new CatalogPart();
                catalogPart.setProductID(product.getProductID());
                catalogPart.setAssemblyID(candidateProduct.getAssembly().getAssemblyID());
                catalogPart.setAssemblyDocID(candidateProduct.getAssembly().getAssemblyDocID());
                catalogPart.setPartID(part.getPartID());
                catalogPart.setPartDocID(part.getPartDocID());
                catalogPart.setLinkable(part.isLinkable());

                product.addPart(catalogPart);
            }
        }
        return products;
    }

    private List<AssemblyEngine.CandidateProduct> getCandidateProducts (String assemblyURI, String partsURI) throws Exception {
        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        AssemblyEngine mgr = new AssemblyEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource(partsURI);
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        url = getClass().getResource(assemblyURI);
        Assembly assembly = util.importAssembly(url.toURI());

        List<AssemblyEngine.CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        return candidates;

    }
}
