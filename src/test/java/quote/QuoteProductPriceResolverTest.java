package quote;

import catalog.Assembly;
import catalog.Product;
import catalog.dao.CatalogPart;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import quote.handler.QuoteProductPriceResolver;
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
public class QuoteProductPriceResolverTest {

    FileUtil util = new FileUtil();
    //private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * verify that selections have been made for all incremental properties
     * present in all parts of all products included in the quote.
     */
    @Test
    public void testOnePartFixedMargin() throws Exception {

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
        quote.projectName = "testBasic1";
        quote.customerID = "C-1500";
        Product product = makeProduct(assemblyURI, partsURI);
        Quote.LineItem lineItem = quote.addLineItem(Integer.toString(quantity), product);

        Quote.QuotePart quotePart = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart.setSelection(PartPropertyType.STRIP_LENGTH, "12in");
        quotePart.quantity = "1"; // IMPORTANT - number of parts of this type in the product

        CreateQuoteCmd cmd = new CreateQuoteCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteChoiceResolver i2 = new QuoteChoiceResolver();
        QuoteProductCostResolver i3 = new QuoteProductCostResolver();
        QuoteProductPriceResolver i4 = new QuoteProductPriceResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);
        i4.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());

        Assert.assertEquals("line item quoted cost", "17.00 USD", lineItem.quotedCost.value);
        Assert.assertEquals("line item total cost", "1700.00 USD", lineItem.totalCost.value);

        Assert.assertEquals("line item quoted price", "21.25 USD", lineItem.quotedPrice.value);
        Assert.assertEquals("line item total price", "2125.00 USD", lineItem.totalPrice.value);

    }

    /**
     * verify multiple part quantities
     * @throws Exception
     */
    @Test
    public void testOnePartMultiQuantityFixedMargin() throws Exception {

        final String assemblyURI = "/quote/test4.quote.assembly.json";
        final String partsURI = "/quote/test4.quote.parts.json";

        CatalogMgr catMgr = mock(CatalogMgr.class);
        doAnswer(new Answer<Part>() {
            @Override
            public Part answer(InvocationOnMock invocation) throws Exception {
                return getPart(partsURI, (String) invocation.getArguments()[0]);
            }
        }).when(catMgr).getPart(anyString());

        int quantity = 350;

        Quote quote = new Quote();
        quote.projectName = "testBasic2";
        quote.customerID = "C-1500";
        Product product = makeProduct(assemblyURI, partsURI);
        Quote.LineItem lineItem = quote.addLineItem(Integer.toString(quantity), product);

        Quote.QuotePart quotePart = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart.setSelection(PartPropertyType.STRIP_LENGTH, "12in");
        quotePart.quantity = "5"; // IMPORTANT - number of parts of this type in the product

        CreateQuoteCmd cmd = new CreateQuoteCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteChoiceResolver i2 = new QuoteChoiceResolver();
        QuoteProductCostResolver i3 = new QuoteProductCostResolver();
        QuoteProductPriceResolver i4 = new QuoteProductPriceResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);
        i4.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());

        Assert.assertEquals("line item quoted cost", "50.00 USD", lineItem.quotedCost.value);
        Assert.assertEquals("line item total cost", "17500.00 USD", lineItem.totalCost.value);

        Assert.assertEquals("line item quoted price", "62.50 USD", lineItem.quotedPrice.value);
        Assert.assertEquals("line item total price", "21875.00 USD", lineItem.totalPrice.value);
    }


    @Test
    public void testThreePartsFiveIncrementsFixedMargin() throws Exception {

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

        Quote.QuotePart quotePart1 = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart1.setSelection(PartPropertyType.STRIP_LENGTH, "100cm");
        quotePart1.setSelection(PartPropertyType.FEMALE_LEAD_LENGTH, "12in");

        Quote.QuotePart quotePart2 = lineItem.quotedProduct.getPart("ADAPT-01");
        quotePart2.setSelection(PartPropertyType.POWER_CORD_LENGTH, "24in");

        Quote.QuotePart quotePart3 = lineItem.quotedProduct.getPart("PLUG-01");
        quotePart3.setSelection(PartPropertyType.LEAD_LENGTH, "100cm");

        CreateQuoteCmd cmd = new CreateQuoteCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteChoiceResolver i2 = new QuoteChoiceResolver();
        QuoteProductCostResolver i3 = new QuoteProductCostResolver();
        QuoteProductPriceResolver i4 = new QuoteProductPriceResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);
        i4.evaluate(cmd);

        Assert.assertEquals("quotedCost", "24.20 USD", quotePart1.quotedCost.value);
        Assert.assertEquals("quotedCost", "7.40 USD", quotePart2.quotedCost.value);
        Assert.assertEquals("quotedCost", "4.90 USD", quotePart3.quotedCost.value);

        Assert.assertEquals("line item quoted cost", "36.50 USD", lineItem.quotedCost.value);
        Assert.assertEquals("line item total cost", "3650.00 USD", lineItem.totalCost.value);

        Assert.assertEquals("line item quoted price", "45.63 USD", lineItem.quotedPrice.value); // rounded up from 45.625
        Assert.assertEquals("line item total price", "4562.50 USD", lineItem.totalPrice.value);
    }

    @Test
    public void testThreePartsFiveIncrementsFixedMarginIdempotent() throws Exception {

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

        Quote.QuotePart quotePart1 = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart1.setSelection(PartPropertyType.STRIP_LENGTH, "100cm");
        quotePart1.setSelection(PartPropertyType.FEMALE_LEAD_LENGTH, "12in");

        Quote.QuotePart quotePart2 = lineItem.quotedProduct.getPart("ADAPT-01");
        quotePart2.setSelection(PartPropertyType.POWER_CORD_LENGTH, "24in");

        Quote.QuotePart quotePart3 = lineItem.quotedProduct.getPart("PLUG-01");
        quotePart3.setSelection(PartPropertyType.LEAD_LENGTH, "100cm");

        CreateQuoteCmd cmd = new CreateQuoteCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteChoiceResolver i2 = new QuoteChoiceResolver();
        QuoteProductCostResolver i3 = new QuoteProductCostResolver();
        QuoteProductPriceResolver i4 = new QuoteProductPriceResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);
        i4.evaluate(cmd);

        // idempotent support -- rerun all handlers again -- must be in correct order however

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);
        i4.evaluate(cmd);

        Assert.assertEquals("quotedCost", "24.20 USD", quotePart1.quotedCost.value);
        Assert.assertEquals("quotedCost", "7.40 USD", quotePart2.quotedCost.value);
        Assert.assertEquals("quotedCost", "4.90 USD", quotePart3.quotedCost.value);

        Assert.assertEquals("line item quoted cost", "36.50 USD", lineItem.quotedCost.value);
        Assert.assertEquals("line item total cost", "3650.00 USD", lineItem.totalCost.value);

        Assert.assertEquals("line item quoted price", "45.63 USD", lineItem.quotedPrice.value); // rounded up from 45.625
        Assert.assertEquals("line item total price", "4562.50 USD", lineItem.totalPrice.value);
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

    private void printCalculations (Quote quote) {
        System.out.println(quote.projectName);
        for (Quote.LineItem item : quote.items) {
            for (Quote.QuoteNote note : item.calculations) {
                System.out.println(note.toString());
            }
        }
    }
}
