package quote;

import catalog.Assembly;
import catalog.Product;
import catalog.dao.CatalogPart;
import mgr.CatalogMgr;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import part.Part;
import part.PartPropertyType;
import quote.cmd.QuoteCreateCmd;
import quote.handler.QuoteIncrementResolver;
import quote.handler.QuotePartResolver;
import util.AppRuntimeException;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 *
 */
public class QuoteIncrementResolverTest {

    FileUtil util = new FileUtil();

    /**
     * verify that selections have been made for all incremental properties
     * present in all parts of all products included in the quote.
     */
    @Test
    public void basicTest() throws Exception {

        final String assemblyURI = "/quote/test2.quote.assembly.json";
        final String partsURI = "/quote/test2.quote.parts.json";

        CatalogMgr catMgr = mock(CatalogMgr.class);
        doAnswer(new Answer<Part>() {
            @Override
            public Part answer(InvocationOnMock invocation) throws Exception {
                return getPart(partsURI, (String) invocation.getArguments()[0]);
            }
        }).when(catMgr).getPart(anyString());

        int quantity = 100;

        Quote quote = new Quote();
        Product product = makeProduct(assemblyURI, partsURI);
        Quote.LineItem lineItem = quote.addLineItem(Integer.toString(quantity), product);

        Quote.QuotePart quotePart1 = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart1.setSelection (PartPropertyType.FLEX_LENGTH, "36in");

        Quote.QuotePart quotePart2 = lineItem.quotedProduct.getPart("ADAPT-01");
        quotePart2.setSelection (PartPropertyType.POWER_CORD_LENGTH, "6ft");

        Quote.QuotePart quotePart3 = lineItem.quotedProduct.getPart("PLUG-01");
        quotePart3.setSelection (PartPropertyType.LEAD_LENGTH, "10cm");

        QuoteCreateCmd cmd = new QuoteCreateCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteIncrementResolver i2 = new QuoteIncrementResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);

        org.junit.Assert.assertTrue(cmd.isStarted());
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
        return util.importAssembly(url.toURI());
    }

    private List<Part> getParts(String partsURI) throws Exception {

        URL url = getClass().getResource(partsURI);
        return util.importParts(url.toURI());
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
