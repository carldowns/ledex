package quote;

import catalog.Assembly;
import catalog.Product;
import catalog.dao.CatalogPart;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import mgr.CatalogMgr;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import part.Part;
import part.PartPropertyType;
import quote.cmd.CreateQuoteCmd;
import quote.handler.QuoteChoiceResolver;
import quote.handler.QuotePartResolver;
import quote.handler.QuoteProductCostResolver;
import util.AppRuntimeException;
import util.CmdRuntimeException;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 *
 */
public class QuoteProductCostResolverTest {

    FileUtil util = new FileUtil();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * verify that selections have been made for all incremental properties
     * present in all parts of all products included in the quote.
     */
    @Test
    public void testBasicFunction() throws Exception {

        final String assemblyURI = "/quote/test4.quote.assembly.json";
        final String partsURI = "/quote/test4.quote.parts.json";

        CatalogMgr catMgr = mock(CatalogMgr.class);
        doAnswer(new Answer<Part>() {
            @Override
            public Part answer(InvocationOnMock invocation) throws Exception {
                return getPart(partsURI, (String) invocation.getArguments()[0]);
            }
        }).when(catMgr).getPart(anyString());

        int quantity = 100;

        Quote quote = new Quote();
        quote.projectName = "Test Project";
        quote.customerID = "C-1500";
        Product product = makeProduct(assemblyURI, partsURI);
        Quote.LineItem lineItem = quote.addLineItem(Integer.toString(quantity), product);

        Quote.QuotePart quotePart = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart.setSelection(PartPropertyType.STRIP_LENGTH, "36in");
        quotePart.quantity = "2"; // IMPORTANT - number of parts of this type in the product

        CreateQuoteCmd cmd = new CreateQuoteCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteChoiceResolver i2 = new QuoteChoiceResolver();
        QuoteProductCostResolver i3 = new QuoteProductCostResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());

//        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
//        writer.writeValue(System.out, cmd.getQuote());
    }

    //@Test
    public void testDoubleIncrements() throws Exception {

        final String assemblyURI = "/quote/test5.quote.assembly.json";
        final String partsURI = "/quote/test5.quote.parts.json";

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

        Quote.QuotePart quotePart = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart.setSelection(PartPropertyType.ADHESIVE, "Tb10mm");
        quotePart.setSelection(PartPropertyType.LED_COLOR_TEMP, "6000k");

        CreateQuoteCmd cmd = new CreateQuoteCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteChoiceResolver i2 = new QuoteChoiceResolver();
        QuoteProductCostResolver i3 = new QuoteProductCostResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());
        //xSystem.out.println(cmd.getQuote());

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(System.out, cmd.getQuote());
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
