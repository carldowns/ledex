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
    public void testBasic1() throws Exception {

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

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());
        //        {
        //            "qty": 100,
        //                "baseCost": "5.00 USD",
        //                "increments": [
        //            {
        //                "type": "STRIP_LENGTH",
        //                    "name": "Strip Length",
        //                    "addCost": "1.00 USD"
        //            }
        //            ]
        //        },
        // $5.00 + $12.00 = $17.00 * 1 = $17.00
        // $17.00 * 100 = $1700.00
        Assert.assertEquals("line item quoted cost", "17.00 USD", lineItem.quotedCost.value);
        Assert.assertEquals("line item total cost", "1700.00 USD", lineItem.totalCost.value);
        Assert.assertEquals("calculations", 1, lineItem.calculations.size());
        Assert.assertEquals("calc type", lineItem.calculations.get(0).type, "BASE-COST");
        Assert.assertTrue("exact treatment", lineItem.calculations.get(0).treatment.contains("exact"));
        //printCalculations (quote);
    }

    @Test
    public void testBasic2() throws Exception {

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

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());
        //        {
        //            "qty": 250,
        //                "baseCost": "4.00 USD",
        //                "increments": [
        //            {
        //                "type": "STRIP_LENGTH",
        //                    "name": "STRIP_LENGTH",
        //                    "addCost": "0.50 USD"
        //            }
        //            ]
        //        },
        // $4.00 + $6.00 = $10.00 * 5 = $50.00
        // $50.00 * 350 = $17500.00
        Assert.assertEquals("line item quoted cost incorrect", "50.00 USD", lineItem.quotedCost.value);
        Assert.assertEquals("line item total cost incorrect", "17500.00 USD", lineItem.totalCost.value);
        Assert.assertTrue("exact treatment", lineItem.calculations.get(0).treatment.contains("range"));
        //printCalculations (quote);
    }

    @Test
    public void testBasic3() throws Exception {

        final String assemblyURI = "/quote/test4.quote.assembly.json";
        final String partsURI = "/quote/test4.quote.parts.json";

        CatalogMgr catMgr = mock(CatalogMgr.class);
        doAnswer(new Answer<Part>() {
            @Override
            public Part answer(InvocationOnMock invocation) throws Exception {
                return getPart(partsURI, (String) invocation.getArguments()[0]);
            }
        }).when(catMgr).getPart(anyString());

        int quantity = 1000;

        Quote quote = new Quote();
        quote.projectName = "testBasic3";
        quote.customerID = "C-1500";
        Product product = makeProduct(assemblyURI, partsURI);
        Quote.LineItem lineItem = quote.addLineItem(Integer.toString(quantity), product);

        Quote.QuotePart quotePart = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart.setSelection(PartPropertyType.STRIP_LENGTH, "24in");
        quotePart.quantity = "2"; // IMPORTANT - number of parts of this type in the product

        CreateQuoteCmd cmd = new CreateQuoteCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteChoiceResolver i2 = new QuoteChoiceResolver();
        QuoteProductCostResolver i3 = new QuoteProductCostResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());

        //        {
        //            "qty": 500,
        //                "baseCost": "3.00 USD",
        //                "increments": [
        //            {
        //                "type": "STRIP_LENGTH",
        //                    "name": "STRIP_LENGTH",
        //                    "addCost": "0.25 USD"
        //            }
        //            ]
        //        }
        // $3.00 + $6.00 = $9.00 * 2 = $18.00
        // $18.00 * 1000 = $18000.00
        Assert.assertEquals("line item quoted cost incorrect", "18.00 USD", lineItem.quotedCost.value);
        Assert.assertEquals("line item total cost incorrect", "18000.00 USD", lineItem.totalCost.value);
        Assert.assertTrue("exact treatment", lineItem.calculations.get(0).treatment.contains("range"));
        //printCalculations (quote);
    }

    @Test
    public void testBasic4() throws Exception {

        final String assemblyURI = "/quote/test4.quote.assembly.json";
        final String partsURI = "/quote/test4.quote.parts.json";

        CatalogMgr catMgr = mock(CatalogMgr.class);
        doAnswer(new Answer<Part>() {
            @Override
            public Part answer(InvocationOnMock invocation) throws Exception {
                return getPart(partsURI, (String) invocation.getArguments()[0]);
            }
        }).when(catMgr).getPart(anyString());

        int quantity = 1;

        Quote quote = new Quote();
        quote.projectName = "testBasic4";
        quote.customerID = "C-1500";
        Product product = makeProduct(assemblyURI, partsURI);
        Quote.LineItem lineItem = quote.addLineItem(Integer.toString(quantity), product);

        Quote.QuotePart quotePart = lineItem.quotedProduct.getPart("LIGHT-01");
        quotePart.setSelection(PartPropertyType.STRIP_LENGTH, "400cm");
        quotePart.quantity = "1"; // IMPORTANT - number of parts of this type in the product

        CreateQuoteCmd cmd = new CreateQuoteCmd(quote);

        QuotePartResolver i1 = new QuotePartResolver(catMgr);
        QuoteChoiceResolver i2 = new QuoteChoiceResolver();
        QuoteProductCostResolver i3 = new QuoteProductCostResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());
        //        {
        //            "qty": 100,
        //                "baseCost": "5.00 USD",
        //                "increments": [
        //            {
        //                "type": "STRIP_LENGTH",
        //                    "name": "Strip Length",
        //                    "addCost": "1.00 USD"
        //            }
        //            ]
        //        },
        // 400cm ~ 157in
        // $5.00 + $157.00 = $162.00 * 1 = $162.00
        // $162.00 * 1 = $162.00
        Assert.assertEquals("line item quoted cost", "162.00 USD", lineItem.quotedCost.value);
        Assert.assertEquals("line item total cost", "162.00 USD", lineItem.totalCost.value);
        Assert.assertEquals("calculations", 1, lineItem.calculations.size());
        Assert.assertEquals("calc type", lineItem.calculations.get(0).type, "BASE-COST");
        Assert.assertTrue("exact treatment", lineItem.calculations.get(0).treatment.contains("MOQ warning"));
        //printCalculations (quote);
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

//        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
//        writer.writeValue(System.out, cmd.getQuote());
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
