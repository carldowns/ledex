package util;

import java.math.BigDecimal;
import org.eclipse.jetty.util.StringUtil;

/**
 */

public class UnitConverter {

    // ////////////////////////////////
    // currency constants
    // ////////////////////////////////

    static final String USDOLLAR = "USD";
    static final String USDOLLAR_SYMBOL = "$";
    static final String CNY_RENMINBI = "RMB";

    // ////////////////////////////////
    // weight constants
    // ////////////////////////////////

    static final String GRAM = "G";
    static final String KILOGRAM = "KG";
    static final String POUND = "LB";
    static final String POUNDS = "LBS";
    static final String OUNCE = "OZ";

    // ////////////////////////////////
    // length constants
    // ////////////////////////////////

    static final String METER = "M";
    static final String CENTIMETER = "CM";
    static final String MILLIMETER = "MM";
    static final String INCH = "IN";
    static final String INCH_SYMBOL = "\"";
    static final String FEET = "FT";
    static final String FEET_SYMBOL = "\'";

    // ////////////////////////////////
    // power constants
    // ////////////////////////////////

    static final String WATT = "W";
    static final String AMP = "A";
    static final String MILLIAMP = "MA";
    static final String VOLT_DC = "VDC";
    static final String VOLT_AC = "VAC";

    private String input;
    private UnitTypeValue utv;

    /**
     * Unit types possible
     * 
     * @author carl_downs
     */
    public enum UnitType {
        // currency
        USD, // dollars
        RMB, // renminbi

        // weights
        G, // grams
        KG, // kilograms
        LB, // pounds
        OZ, // ounces

        // measures
        M, // meters
        CM, // centimeters
        MM, // millimeters
        IN, // inches
        FT, // feet

        // power
        MA, // milli amps
        VDC, // Volts DC
        VAC, // Volts AC
    }

    /**
     * Used to hold the value separately from the value type enum.
     * @author carl_downs
     *
     */
    public static class UnitTypeValue {

        UnitType type;
        String value;

        private UnitTypeValue(UnitType type, String value) {
            this.type = type;
            this.value = value;
        }

        public void validate () {
            try {
                toDecimal();                
            }
            catch (NumberFormatException nfe) {
                throw new IllegalArgumentException ("value must be in decimal format: " + value);
            }
        }

        public UnitType getType() {
            return type;
        }

        public void setType(UnitType type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public BigDecimal toDecimal () {
            return new BigDecimal (value);
        }
        
        public String toString() {
            return value + " (" + type + ")";
        }
    }

    // //////////////////////
    // static helpers
    // //////////////////////

    public static String assertMonetaryType(String input) {
        if (StringUtil.isNotBlank(input))
            new UnitConverter(input).getCurrencyType().validate();

        return input;
    }

    public static String assertLengthType(String input) {
        if (StringUtil.isNotBlank(input))
            new UnitConverter(input).getLengthType().validate();
        
        return input;
    }

    public static String assertWeightType(String input) {
        if (StringUtil.isNotBlank(input))
            new UnitConverter(input).getWeightType().validate();
        
        return input;
    }

    public static String assertVoltageType(String input) {
        if (StringUtil.isNotBlank(input))
            new UnitConverter(input).getVoltageType().validate();

        return input;
    }

    // //////////////////////
    // constructors
    // //////////////////////

    public UnitConverter(String input) {
        this.input = input.toUpperCase();
    }

    // //////////////////////
    // public methods
    // //////////////////////

    /**
     * returns a 'measure' type for the intrinsic value.
     * 
     * @throws IllegalArgumentException
     *             if value not a measure type
     * @return
     */
    public UnitTypeValue getLengthType() {
        if (utv != null)
            return utv;

        int index = -1;

        index = input.indexOf(CENTIMETER);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.CM, s);
        }

        index = input.indexOf(MILLIMETER);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.MM, s);
        }

        index = input.indexOf(METER);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.M, s);
        }

        index = input.indexOf(INCH);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.IN, s);
        }

        index = input.indexOf(INCH_SYMBOL);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.IN, s);
        }

        index = input.indexOf(FEET);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.FT, s);
        }

        index = input.indexOf(FEET_SYMBOL);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.FT, s);
        }

        throw new IllegalArgumentException("unknown measurement type: " + input);
    }

    public UnitTypeValue getVoltageType() {
        if (utv != null)
            return utv;

        int index = -1;

        index = input.indexOf(VOLT_DC);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.VDC, s);
        }

        index = input.indexOf(VOLT_AC);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.VAC, s);
        }

        throw new IllegalArgumentException("unknown voltage type: " + input);
    }

        /**
         * returns a 'currency' type for the intrinsic value.
         *
         * @throws IllegalArgumentException
         *             if value not a measure type
         * @return
         */
    public UnitTypeValue getCurrencyType() {
        if (utv != null)
            return utv;

        int index = -1;

        index = input.indexOf(USDOLLAR);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.USD, s);
        }

        index = input.indexOf(USDOLLAR_SYMBOL);
        if (index != -1) {
            String s = input.substring(index + 1).trim();
            return utv = new UnitTypeValue(UnitType.USD, s);
        }

        index = input.indexOf(CNY_RENMINBI);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.RMB, s);
        }

        throw new IllegalArgumentException("unknown currency type: " + input);
    }

    /**
     * returns a 'weight' type for the intrinsic value.
     * 
     * @throws IllegalArgumentException
     *             if value not a measure type
     * @return
     */
    public UnitTypeValue getWeightType() {
        if (utv != null)
            return utv;

        int index = -1;

        index = input.indexOf(KILOGRAM);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.KG, s);
        }

        index = input.indexOf(GRAM);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.G, s);
        }

        index = input.indexOf(POUND);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.LB, s);
        }

        index = input.indexOf(POUNDS);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.LB, s);
        }

        index = input.indexOf(OUNCE);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.OZ, s);
        }

        throw new IllegalArgumentException("unknown weight type: " + input);
    }

    // ////////////////////////////////
    // currency conversions
    // ////////////////////////////////

    public String toUSD() {
        UnitTypeValue utv = getCurrencyType();
        if (utv.type == UnitType.USD)
            return utv.value;

        throw new UnsupportedOperationException(input);
    }

    public String toRMB() {
        UnitTypeValue utv = getCurrencyType();
        if (utv.type == UnitType.RMB)
            return utv.value;

        throw new UnsupportedOperationException(input);
    }

    // ////////////////////////////////
    // weight conversions
    // ////////////////////////////////

    public String toPounds(int scale) {
        UnitTypeValue utv = getWeightType();

        if (utv.type == UnitType.LB)
            return convert("1", scale);

        if (utv.type == UnitType.KG)
            return convert("2.20462", scale);

        if (utv.type == UnitType.G)
            return convert("0.00220462", scale);

        if (utv.type == UnitType.OZ)
            return convert("0.0625", scale);

        throw new UnsupportedOperationException(input);
    }

    public String toKilos(int scale) {
        UnitTypeValue utv = getWeightType();

        if (utv.type == UnitType.LB)
            return convert("0.453592", scale);

        if (utv.type == UnitType.KG)
            return convert("1", scale);

        if (utv.type == UnitType.G)
            return convert("0.001", scale);

        if (utv.type == UnitType.OZ)
            return convert("0.0283495", scale);

        throw new UnsupportedOperationException(input);
    }

    public String toOunces(int scale) {
        UnitTypeValue utv = getWeightType();

        if (utv.type == UnitType.LB)
            return convert("16", scale);

        if (utv.type == UnitType.KG)
            return convert("0.453592", scale);

        if (utv.type == UnitType.G)
            return convert("453.592", scale);

        if (utv.type == UnitType.OZ)
            return convert("1", scale);

        throw new UnsupportedOperationException(input);
    }

    public String toGrams(int scale) {
        UnitTypeValue utv = getWeightType();

        if (utv.type == UnitType.LB)
            return convert("453.592", scale);

        if (utv.type == UnitType.KG)
            return convert("1000", scale);

        if (utv.type == UnitType.G)
            return convert("1", scale);

        if (utv.type == UnitType.OZ)
            return convert("28.3495", scale);

        throw new UnsupportedOperationException(input);
    }

    // //////////////////////////////////
    // measure conversions
    // //////////////////////////////////

    public String toMeters(int scale) {
        UnitTypeValue utv = getLengthType();

        if (utv.type == UnitType.M)
            return convert("1", scale);

        if (utv.type == UnitType.CM)
            return convert("100", scale);

        if (utv.type == UnitType.MM)
            return convert("1000", scale);

        if (utv.type == UnitType.FT)
            return convert("0.3048", scale);

        if (utv.type == UnitType.IN)
            return convert("0.0254", scale);

        throw new UnsupportedOperationException(input);
    }

    public String toCentimeters(int scale) {
        UnitTypeValue utv = getLengthType();

        if (utv.type == UnitType.M)
            return convert("100", scale);

        if (utv.type == UnitType.CM)
            return convert("1", scale);

        if (utv.type == UnitType.MM)
            return convert("10", scale);

        if (utv.type == UnitType.FT)
            return convert("304.8", scale);

        if (utv.type == UnitType.IN)
            return convert("25.4", scale);

        throw new UnsupportedOperationException(input);
    }

    public String toMillimeters(int scale) {
        UnitTypeValue utv = getLengthType();

        if (utv.type == UnitType.M)
            return convert("1000", scale);

        if (utv.type == UnitType.CM)
            return convert("10", scale);

        if (utv.type == UnitType.MM)
            return convert("1", scale);

        if (utv.type == UnitType.FT)
            return convert("304.8", scale);

        if (utv.type == UnitType.IN)
            return convert("25.4", scale);

        throw new UnsupportedOperationException(input);
    }

    public String toFeet(int scale) {
        UnitTypeValue utv = getLengthType();

        if (utv.type == UnitType.M)

            return convert("3.28084", scale);

        if (utv.type == UnitType.CM)

            return convert("0.0328084", scale);
        if (utv.type == UnitType.MM)

            return convert("0.00328084", scale);
        if (utv.type == UnitType.FT)
            return convert("1", scale);

        if (utv.type == UnitType.IN)
            return convert("0.0833333", scale);

        throw new UnsupportedOperationException(input);
    }

    public String toInches(int scale) {
        UnitTypeValue utv = getLengthType();

        if (utv.type == UnitType.M)
            return convert("(39.3701", scale);

        if (utv.type == UnitType.CM)
            return convert("(0.393701", scale);

        if (utv.type == UnitType.MM)
            return convert("(0.00393701", scale);

        if (utv.type == UnitType.FT)
            return convert("(12", scale);

        if (utv.type == UnitType.IN)
            return utv.value;

        throw new UnsupportedOperationException(input);
    }

    //////////////////////
    // Power conversions
    //////////////////////

    /**
     * converts the unit value and multiplier to BigDecimals, multiplies them,
     * and returns a string with the precision given.
     * 
     * @param multiplier
     * @param scale
     * @return
     */
    private String convert(String multiplier, int scale) {
        BigDecimal v1 = new BigDecimal(utv.value);
        BigDecimal v2 = new BigDecimal(multiplier);
        BigDecimal v3 = v1.multiply(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);

        return v3.toString();
    }

}
