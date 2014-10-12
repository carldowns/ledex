package model;

import java.io.*;

import org.junit.Assert;
import org.junit.Test;
import rule.RuleEngine;
import util.GsonUtil;

public class AssemblyTest {

    /**
     * Key rules to implement:
     * 
     * voltage compatibility check voltage drop check plug compatibility check
     * amp sufficiency check Lifespan estimation check wiring / connectivity
     * check
     */

    /**
     * Verify that parts match the set voltage
     */

    /**
     * Verify that all of the parts do not draw more than the maximum amperage
     * supplied by the power source
     */

    /**
     * Verify that a set contains no conflicting rules
     */

    /**
     * Verify that a set contains a complete set of rules
     */

    /**
     * Verify that the connectors are all compatible
     */

    /**
     * Verify the the
     */

    @Test
    public void test() throws Exception {
        
        Assembly asm = new Assembly("ADI Jim Bean 3 Bottle Set");
        asm.add(getPart1());
        asm.add(getPart2());

        File file = File.createTempFile("assembly", ".json");
        file.deleteOnExit();

        GsonUtil.toJsonFile(asm, file.toURI());
        Assembly asmReadIn = GsonUtil.fromJson(file.toURI());
        
        RuleEngine e = new RuleEngine(asm);
        Assert.assertTrue(e.isValid());
        Assert.assertEquals(0, e.getIssues().size());

    }

    private Part getPart1() {
        Part p = new Part("AC Adapter", "POWER");
        
        p.addProperty (new PartProperty("VOLTAGE", "Voltage current", "12VDC"));
        p.addProperty (new PartProperty("AMPERAGE", "Amp output", "100mA"));
        p.addProperty (new PartProperty("SWITCH", "Inline On/Off switch", "yes"));
        
        p.addProperty (new PartProperty("POWER_CORD", "Cord length", new PartPropertyIncrement("24in","72in","1in")));
        
        PartMetric dims = new PartMetric("5g","2mm","10mm","10mm");
        dims.add(new PartMetricIncrement("CORD","1kg","5cm","6cm","1cm"));
        p.setMetrics(dims);
        
        PartCost cost100 = new PartCost (100, "$4.00");
        cost100.add (new PartCostIncrement ("POWER_CORD", "0.05 USD"));
        
        PartCost cost250 = new PartCost (250, "3.50 USD");
        cost250.add (new PartCostIncrement ("POWER_CORD", "0.04 USD"));

        PartCost cost500 = new PartCost (500, "3.25 USD");
        cost500.add (new PartCostIncrement ("POWER_CORD", "00.03 USD"));
        
        p.addCost(cost100).addCost(cost250).addCost(cost500);
        
        return p;
    }

    private Part getPart2() {
        Part p = new Part("Flex LED Light Strip", "LIGHT");
        
        p.addProperty (new PartProperty("LED_TYPE", "Type of LED", "3528"));
        p.addProperty (new PartProperty("LED_COLOR", "LED Color", "White"));
        p.addProperty (new PartProperty("LED_COLOR_TEMP", "Cool white", "6000k"));
        p.addProperty (new PartProperty("LED_BEAM_ANGLE", "LED beam angle", "180 degrees"));
        p.addProperty (new PartProperty("VOLTAGE", "Supported Voltage", "4.5VDC"));        
        p.addProperty (new PartProperty("CONNECTION_PLUG_TYPE", "Connection Plug Type", "J17"));        

        p.addProperty (new PartProperty("STRIP_LENGTH", "Strip Length", new PartPropertyIncrement("5mm","72mm","5mm")));
        p.addProperty (new PartProperty("STRIP_INPUT_LEAD_LENGTH", "Strip Input Lead Length", new PartPropertyIncrement("2in","24in","1in")));
        p.addProperty (new PartProperty("STRIP_OUTPUT_LEAD_LENGTH", "Strip Output Lead Length", new PartPropertyIncrement("2in","3in","1in")));
        
        PartCost cost100 = new PartCost (100, "4.55 USD");
        cost100.add (new PartCostIncrement ("STRIP_LENGTH", "Strip Length", "0.05 USD"));
        cost100.add (new PartCostIncrement ("STRIP_INPUT_LEAD_LENGTH", "Strip Input Lead Length", "0.01 USD"));
        cost100.add (new PartCostIncrement ("STRIP_OUTPUT_LEAD_LENGTH", "Strip Output Lead Length", "0.01 USD"));
        
        PartCost cost250 = new PartCost (250, "4.50 USD");
        cost250.add (new PartCostIncrement ("STRIP_LENGTH", "STRIP_LENGTH", "0.04 USD"));
        cost250.add (new PartCostIncrement ("STRIP_INPUT_LEAD_LENGTH", "Strip Input Lead Length", "0.01 USD"));
        cost250.add (new PartCostIncrement ("STRIP_OUTPUT_LEAD_LENGTH", "Strip Output Lead Length", "0.01 USD"));

        PartCost cost500 = new PartCost (500, "4.25 USD");
        cost500.add (new PartCostIncrement ("STRIP_LENGTH", "STRIP_LENGTH", "0.03 USD"));
        cost500.add (new PartCostIncrement ("STRIP_INPUT_LEAD_LENGTH", "Strip Input Lead Length", "0.01 USD"));
        cost500.add (new PartCostIncrement ("STRIP_OUTPUT_LEAD_LENGTH", "Strip Output Lead Length", "0.01 USD"));
        
        p.addCost(cost100).addCost(cost250).addCost(cost500);
       
        return p;
    }
}
