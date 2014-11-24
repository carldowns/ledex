package quote;

import catalog.Assembly;
import catalog.Product;
import catalog.dao.CatalogPart;
import mgr.CatalogMgr;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import part.Part;
import quote.cmd.CreateQuoteCmd;
import quote.handler.QuotePartResolver;
import util.AppRuntimeException;
import util.CmdRuntimeException;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 *
 */
public class QuotePartResolverTest {

    FileUtil util = new FileUtil();

    /**
     * verify that part resolver can find and attach Part object
     */
    @Test
    public void basicTest() throws Exception {

        final String assemblyURI = "/quote/test1.quote.assembly.json";
        final String partsURI = "/quote/test1.quote.parts.json";

        CatalogMgr catMgr = mock(CatalogMgr.class);
        doAnswer(new Answer<Part>() {
            @Override
            public Part answer(InvocationOnMock invocation) throws Exception {
                return getPart(partsURI, (String) invocation.getArguments()[0]);
            }
        }).when(catMgr).getPart(anyString());


        CreateQuoteCmd cmd = new CreateQuoteCmd();
        int quantity = 100;

        Product product = makeProduct(assemblyURI, partsURI);
        cmd.getQuote().addLineItem(Integer.toString(quantity), product);

        QuotePartResolver i = new QuotePartResolver(catMgr);
        i.evaluate(cmd);
        org.junit.Assert.assertTrue(cmd.isStarted());
    }

    /**
     * verify that part resolver reacts properly when it cannot find and attach Part
     */
    @Test
    public void testPartsNotFound() throws Exception {

        final String assemblyURI = "/quote/test1.quote.assembly.json";
        final String partsURI = "/quote/test1.quote.parts.json";

        CatalogMgr catMgr = mock(CatalogMgr.class);
        when(catMgr.getPart(anyString())).thenReturn(null); // can't find part

        CreateQuoteCmd cmd = new CreateQuoteCmd();
        cmd.getQuote();
        cmd.setCustomerID("1");
        cmd.setProjectName("test");
        int quantity = 100;

        Product product = makeProduct(assemblyURI, partsURI);
        cmd.getQuote().addLineItem(Integer.toString(quantity), product);

        try {
            QuotePartResolver i = new QuotePartResolver(catMgr);
            i.evaluate(cmd);
        }
        catch (CmdRuntimeException cre) {
            org.junit.Assert.assertTrue(cre.getMessage().contains("part"));
        }
        org.junit.Assert.assertTrue(cmd.isFailed());
    }

    ///////////////
    // Helpers
    ///////////////

    private Product makeProduct(String assemblyURI, String partsURI) throws Exception {

        Assembly asm = getAssembly(assemblyURI);
        List<Part> parts = getParts(partsURI);
        Product product = new Product();
        product.setProductID("PRODUCT-1");

        for (Part part : parts) {
            CatalogPart catalogPart = new CatalogPart();
            catalogPart.setProductID(product.getProductID());
            catalogPart.setAssemblyID(asm.getAssemblyID());
            catalogPart.setAssemblyDocID(asm.getAssemblyDocID());
            catalogPart.setPartID(part.getPartID());
            catalogPart.setPartDocID(part.getPartDocID());
            catalogPart.setLinkable(part.isLinkable());
            product.addPart(catalogPart);
        }

        return product;
    }

    private Assembly getAssembly(String assemblyURI) throws Exception {

        URL url = getClass().getResource(assemblyURI);
        Assembly assembly = util.importAssembly(url.toURI());
        return assembly;

    }

    private List<Part> getParts(String partsURI) throws Exception {

        URL url = getClass().getResource(partsURI);
        List<Part> parts = util.importParts(url.toURI());
        return parts;
    }

    private Part getPart(String partsURI, String partID) throws Exception {

        URL url = getClass().getResource(partsURI);
        List<Part> parts = util.importParts(url.toURI());
        for (Part part : parts) {
            if (part.getPartID().equals(partID)) {
                return part;
            }
        }
        throw new AppRuntimeException("part not found" + partID);
    }
}
