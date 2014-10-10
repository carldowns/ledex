package model;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import model.UnitConverter.UnitType;
import model.UnitConverter.UnitTypeValue;

public class UnitConverterTest {

    // ///////////////////
    // Currency
    // ///////////////////

    @Test
    public void validateUSD() {
        UnitConverter uc = new UnitConverter("  300.05 USD        ");
        UnitTypeValue utv = uc.getCurrencyType();

        Assert.assertEquals(utv.value, "300.05");
        Assert.assertTrue(utv.type == UnitType.USD);

        // uc.toRMB();
        uc.toUSD();
    }

    @Test
    public void validateUSDSymbol() {
        UnitConverter uc = new UnitConverter("$300");
        UnitTypeValue utv = uc.getCurrencyType();

        Assert.assertEquals(utv.value, "300");
        Assert.assertTrue(utv.type == UnitType.USD);

        // uc.toRMB();
        uc.toUSD();
    }

    @Test(expected = IllegalArgumentException.class)
    public void badCurrency() {
        UnitConverter uc = new UnitConverter("$500,");
        uc.getCurrencyType().validate();
    }

    // ///////////////////
    // Length
    // ///////////////////

    @Test
    public void validateMeters() {
        UnitConverter uc = new UnitConverter("7.5m");
        UnitTypeValue utv = uc.getLengthType();

        Assert.assertEquals(utv.value, "7.5");
        Assert.assertTrue(utv.type == UnitType.M);

        utv.validate();
    }

    @Test
    public void validateCm() {
        UnitConverter uc = new UnitConverter("57.5cm");
        UnitTypeValue utv = uc.getLengthType();

        Assert.assertEquals(utv.value, "57.5");
        Assert.assertTrue(utv.type == UnitType.CM);

        utv.validate();
    }

    @Test
    public void validateMm() {
        UnitConverter uc = new UnitConverter("120mm");
        UnitTypeValue utv = uc.getLengthType();

        Assert.assertEquals(utv.value, "120");
        Assert.assertTrue(utv.type == UnitType.MM);

        utv.validate();
    }

    @Test
    public void validateInches() {
        UnitConverter uc = new UnitConverter("12in");
        UnitTypeValue utv = uc.getLengthType();

        Assert.assertEquals(utv.value, "12");
        Assert.assertTrue(utv.type == UnitType.IN);

        utv.validate();
    }

    @Test
    public void validateInchSymbol() {
        UnitConverter uc = new UnitConverter("12\"");
        UnitTypeValue utv = uc.getLengthType();

        Assert.assertEquals(utv.value, "12");
        Assert.assertTrue(utv.type == UnitType.IN);

        utv.validate();
    }

    @Test
    public void validateFeet() {
        UnitConverter uc = new UnitConverter("12\'");
        UnitTypeValue utv = uc.getLengthType();

        Assert.assertEquals(utv.value, "12");
        Assert.assertTrue(utv.type == UnitType.FT);

        utv.validate();
    }

    @Test
    public void validateFeetSymbol() {
        UnitConverter uc = new UnitConverter("12ft");
        UnitTypeValue utv = uc.getLengthType();

        Assert.assertEquals(utv.value, "12");
        Assert.assertTrue(utv.type == UnitType.FT);

        utv.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void badLength() {
        UnitConverter uc = new UnitConverter("fifteen inches");
        uc.getLengthType().validate();
    }

    // ///////////////////
    // Weight
    // ///////////////////

    @Test
    public void validateLbs() {
        UnitConverter uc = new UnitConverter("12 lbs");
        UnitTypeValue utv = uc.getWeightType();

        Assert.assertEquals(utv.value, "12");
        Assert.assertTrue(utv.type == UnitType.LB);

        utv.validate();
    }

    @Test
    public void validateKg() {
        UnitConverter uc = new UnitConverter("12.6kg");
        UnitTypeValue utv = uc.getWeightType();

        Assert.assertEquals(utv.value, "12.6");
        Assert.assertTrue(utv.type == UnitType.KG);

        utv.validate();
        Assert.assertEquals(new BigDecimal("12.6"), utv.toDecimal());
    }

    @Test
    public void validateGrams() {
        UnitConverter uc = new UnitConverter("400 g");
        UnitTypeValue utv = uc.getWeightType();

        Assert.assertEquals(utv.value, "400");
        Assert.assertTrue(utv.type == UnitType.G);

        utv.validate();
        Assert.assertEquals(new BigDecimal("400"), utv.toDecimal());
    }

    @Test
    public void convertLbsToGrams() {
        UnitConverter uc = new UnitConverter("12 lbs");

        // 12lbs = 4553.104 grams
        Assert.assertEquals(uc.toGrams(0), "5443");
        Assert.assertEquals(uc.toGrams(2), "5443.10");
        Assert.assertEquals(uc.toGrams(3), "5443.104");
    }

    @Test
    public void convertLbsToOz() {
        UnitConverter uc = new UnitConverter("12 lbs");

        // 12lbs = 192 oz
        Assert.assertEquals(uc.toOunces(0), "192");
    }

    @Test
    public void convertLbsToKg() {
        UnitConverter uc = new UnitConverter("12 lbs");

        // 12lbs = 5.4431 KG
        Assert.assertEquals(uc.toKilos(0), "5");
        Assert.assertEquals(uc.toKilos(2), "5.44");
    }

    @Test
    public void convertOzToGrams() {
        UnitConverter uc = new UnitConverter("1 oz");

        // 1 oz = 28.3495 grams
        Assert.assertEquals("28", uc.toGrams(0));
        Assert.assertEquals("28.3495", uc.toGrams(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void badWeight() {
        UnitConverter uc = new UnitConverter("1 1/2 oz");
        uc.getWeightType().validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongTypeRequest() {
        UnitConverter uc = new UnitConverter("1000 lbs");
        uc.getCurrencyType();
    }

}
