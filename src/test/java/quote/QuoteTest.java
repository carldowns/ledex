package quote;

import catalog.Assembly;
import catalog.CatalogEngine;
import com.google.common.collect.Lists;
import mgr.CatalogMgr;
import org.junit.Test;
import part.Part;
import catalog.Product;
import catalog.dao.CatalogPart;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class QuoteTest {

    @Test (expected=NullPointerException.class) // FIXME
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

        // run specific evaluator
        QuotePartResolver e = new QuotePartResolver();
        e.evaluate(cmd);
    }

    private List<Product> getTestProducts (String assemblyURI, String partsURI)  throws Exception {

        List<Product> products = Lists.newArrayList();
        int counter = 1000;
        for (CatalogEngine.CandidateProduct candidateProduct : getCandidateProducts(assemblyURI, partsURI)) {
            Product product = new Product();
            product.setProductID("PRODUCT-" + counter++);
            products.add(product);

            for (CatalogEngine.CandidatePart candidatePart : candidateProduct.getCandidateParts().values()) {
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

    private List<CatalogEngine.CandidateProduct> getCandidateProducts (String assemblyURI, String partsURI) throws Exception {
        CatalogMgr catalogMgr = mock(CatalogMgr.class);
        CatalogEngine mgr = new CatalogEngine(catalogMgr);
        FileUtil util = new FileUtil();

        URL url = getClass().getResource(partsURI);
        List<Part> parts = util.importParts(url.toURI());
        when(catalogMgr.getAllParts()).thenReturn(parts);

        url = getClass().getResource(assemblyURI);
        Assembly assembly = util.importAssembly(url.toURI());

        List<CatalogEngine.CandidateProduct> candidates = mgr.assembleProductCandidates(assembly);
        return candidates;

    }
}
