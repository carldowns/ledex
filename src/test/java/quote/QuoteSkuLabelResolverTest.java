package quote;

import catalog.Assembly;
import catalog.Product;
import catalog.dao.CatalogPart;
import mgr.CatalogMgr;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import part.Part;
import part.PartPropertyType;
import quote.cmd.CreateQuoteCmd;
import quote.handler.*;
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
public class QuoteSkuLabelResolverTest {

    FileUtil util = new FileUtil();

    /**
     */
    @Test
    public void testOnePart() throws Exception {

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
        QuoteSkuLabelResolver i5 = new QuoteSkuLabelResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);
        i4.evaluate(cmd);
        i5.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());
        Assert.assertEquals("FLEX-12VDC-5050-UVA-12in", lineItem.quotedProduct.getPart("LIGHT-01").cfgLabel);
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("Flex LED Light Strip"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("Supported Voltage 12VDC"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("LED Type 5050"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("LED Color UVA"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("Strip Length 12in"));
    }


    @Test
    public void testThreePartsLinkable() throws Exception {

        final String assemblyURI = "/quote/test6.quote.assembly.json";
        final String partsURI = "/quote/test6.quote.parts.json";

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
        QuoteSkuLabelResolver i5 = new QuoteSkuLabelResolver();

        i1.evaluate(cmd);
        i2.evaluate(cmd);
        i3.evaluate(cmd);
        i4.evaluate(cmd);
        i5.evaluate(cmd);

        Assert.assertTrue(cmd.isStarted());
        Assert.assertEquals("FLEX-3528-White-6000k-180d-4.5VDC-StripLength-100cm-12inL-Lk", lineItem.quotedProduct.getPart("LIGHT-01").cfgLabel);
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("Flex LED Light Strip"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("Strip Input Lead Length 12in"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("LED beam angle 180 degrees"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("LIGHT-01").cfgDescription.contains("Linkable"));

        Assert.assertEquals("ADAPT-12VDC-1A-S-CordLength-24in", lineItem.quotedProduct.getPart("ADAPT-01").cfgLabel);
        Assert.assertTrue(lineItem.quotedProduct.getPart("ADAPT-01").cfgDescription.contains("Voltage current 12VDC; Amp output 1A"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("ADAPT-01").cfgDescription.contains("Inline On/Off switch; Cord Length 24in"));

        Assert.assertEquals("PLUG-ThreadDepth-8mm-LeadWireLength-100cm", lineItem.quotedProduct.getPart("PLUG-01").cfgLabel);
        Assert.assertTrue(lineItem.quotedProduct.getPart("PLUG-01").cfgDescription.contains("Threaded Jack Plug"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("PLUG-01").cfgDescription.contains("Thread Depth 8mm"));
        Assert.assertTrue(lineItem.quotedProduct.getPart("PLUG-01").cfgDescription.contains("Lead Wire Length 100cm"));
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
