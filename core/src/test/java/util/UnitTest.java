package util;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import util.Unit.UnitType;
import util.Unit.UnitTypeValue;

public class UnitTest {

    // ///////////////////
    // Currency
    // ///////////////////

    @Test
    public void validateUSD() {
        Unit uc = new Unit("  300.05 USD        ");
        UnitTypeValue utv = uc.getCurrencyType(true);

        Assert.assertEquals(utv.getValue(), "300.05");
        Assert.assertTrue(utv.getType() == UnitType.USD);

        // uc.toRMB();
        uc.toUSD();
    }

    @Test
    public void validateUSDSymbol() {
        Unit uc = new Unit("$300");
        UnitTypeValue utv = uc.getCurrencyType(true);

        Assert.assertEquals(utv.getValue(), "300");
        Assert.assertTrue(utv.getType() == UnitType.USD);

        // uc.toRMB();
        uc.toUSD();
    }

    @Test(expected = IllegalArgumentException.class)
    public void badCurrency() {
        Unit uc = new Unit("$500,");
        uc.getCurrencyType(true).validate();
    }

    // ///////////////////
    // Length
    // ///////////////////

    @Test
    public void validateMeters() {
        Unit uc = new Unit("7.5m");
        UnitTypeValue utv = uc.getLengthType(true);

        Assert.assertEquals(utv.getValue(), "7.5");
        Assert.assertTrue(utv.getType() == UnitType.M);

        utv.validate();
    }

    @Test
    public void validateCm() {
        Unit uc = new Unit("57.5cm");
        UnitTypeValue utv = uc.getLengthType(true);

        Assert.assertEquals(utv.getValue(), "57.5");
        Assert.assertTrue(utv.getType() == UnitType.CM);

        utv.validate();
    }

    @Test
    public void validateMm() {
        Unit uc = new Unit("120mm");
        UnitTypeValue utv = uc.getLengthType(true);

        Assert.assertEquals(utv.getValue(), "120");
        Assert.assertTrue(utv.getType() == UnitType.MM);

        utv.validate();
    }

    @Test
    public void validateInches() {
        Unit uc = new Unit("12in");
        UnitTypeValue utv = uc.getLengthType(true);

        Assert.assertEquals(utv.getValue(), "12");
        Assert.assertTrue(utv.getType() == UnitType.IN);

        utv.validate();
    }

    @Test
    public void validateInchSymbol() {
        Unit uc = new Unit("12\"");
        UnitTypeValue utv = uc.getLengthType(true);

        Assert.assertEquals(utv.getValue(), "12");
        Assert.assertTrue(utv.getType() == UnitType.IN);

        utv.validate();
    }

    @Test
    public void validateFeet() {
        Unit uc = new Unit("12\'");
        UnitTypeValue utv = uc.getLengthType(true);

        Assert.assertEquals(utv.getValue(), "12");
        Assert.assertTrue(utv.getType() == UnitType.FT);

        utv.validate();
    }

    @Test
    public void validateFeetSymbol() {
        Unit uc = new Unit("12ft");
        UnitTypeValue utv = uc.getLengthType(true);

        Assert.assertEquals(utv.getValue(), "12");
        Assert.assertTrue(utv.getType() == UnitType.FT);

        utv.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void badLength() {
        Unit uc = new Unit("fifteen inches");
        uc.getLengthType(true).validate();
    }

    // ///////////////////
    // Weight
    // ///////////////////

    @Test
    public void validateLbs() {
        Unit uc = new Unit("12 lbs");
        UnitTypeValue utv = uc.getWeightType(true);

        Assert.assertEquals(utv.getValue(), "12");
        Assert.assertTrue(utv.getType() == UnitType.LB);

        utv.validate();
    }

    @Test
    public void validateKg() {
        Unit uc = new Unit("12.6kg");
        UnitTypeValue utv = uc.getWeightType(true);

        Assert.assertEquals(utv.getValue(), "12.6");
        Assert.assertTrue(utv.getType() == UnitType.KG);

        utv.validate();
        Assert.assertEquals(new BigDecimal("12.6"), utv.toDecimal());
    }

    @Test
    public void validateGrams() {
        Unit uc = new Unit("400 g");
        UnitTypeValue utv = uc.getWeightType(true);

        Assert.assertEquals(utv.getValue(), "400");
        Assert.assertTrue(utv.getType() == UnitType.G);

        utv.validate();
        Assert.assertEquals(new BigDecimal("400"), utv.toDecimal());
    }

    @Test
    public void convertLbsToGrams() {
        Unit uc = new Unit("12 lbs");

        // 12lbs = 4553.104 grams
        Assert.assertEquals(uc.toGramsStr(0), "5443");
        Assert.assertEquals(uc.toGramsStr(2), "5443.10");
        Assert.assertEquals(uc.toGramsStr(3), "5443.104");
    }

    @Test
    public void convertLbsToOz() {
        Unit uc = new Unit("12 lbs");

        // 12lbs = 192 oz
        Assert.assertEquals(uc.toOuncesStr(0), "192");
    }

    @Test
    public void convertLbsToKg() {
        Unit uc = new Unit("12 lbs");

        // 12lbs = 5.4431 KG
        Assert.assertEquals(uc.toKilosStr(0), "5");
        Assert.assertEquals(uc.toKilosStr(2), "5.44");
    }

    @Test
    public void convertOzToGrams() {
        Unit uc = new Unit("1 oz");

        // 1 oz = 28.3495 grams
        Assert.assertEquals("28", uc.toGramsStr(0));
        Assert.assertEquals("28.3495", uc.toGramsStr(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void badWeight() {
        Unit uc = new Unit("1 1/2 oz");
        uc.getWeightType(true).validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongTypeRequest() {
        Unit uc = new Unit("1000 lbs");
        uc.getCurrencyType(true);
    }

}
