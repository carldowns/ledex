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
        A, // amps
        MA, // milli amps
        VDC, // Volts DC
        VAC, // Volts AC
    }

    /**
     * Used to hold the value separately from the value type enum.
     *
     * @author carl_downs
     */
    public static class UnitTypeValue {

        UnitType type;
        String value;

        public UnitTypeValue(UnitType type, String value) {
            this.type = type;
            this.value = value;
        }

        public void validate() {
            try {
                toDecimal();
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("value must be in decimal format: " + value);
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

        public BigDecimal toDecimal() {
            return new BigDecimal(value);
        }

        @Override
        public String toString() {
            return toTypeValueString();
        }

        public String toTypeValueString() {
            return value + " " + type;
        }
    }

    // //////////////////////
    // static helpers
    // //////////////////////

    public static String assertMonetaryType(String input) {
        if (StringUtil.isNotBlank(input))
            new UnitConverter(input).getCurrencyType(true).validate();

        return input;
    }

    public static String assertLengthType(String input) {
        if (StringUtil.isNotBlank(input))
            new UnitConverter(input).getLengthType(true).validate();

        return input;
    }

    public static String assertWeightType(String input) {
        if (StringUtil.isNotBlank(input))
            new UnitConverter(input).getWeightType(true).validate();

        return input;
    }

    public static String assertVoltageType(String input) {
        if (StringUtil.isNotBlank(input))
            new UnitConverter(input).getVoltageType(true).validate();

        return input;
    }

    // //////////////////////
    // constructors
    // //////////////////////

    public UnitConverter(String input) {
        this.input = input.toUpperCase();
    }

    public UnitConverter(UnitTypeValue utv) {
        this.utv = utv;
        this.input = utv.toTypeValueString();
    }

    // //////////////////////
    // public methods
    // //////////////////////

    public UnitConverter convertTo(UnitType unitType, int scale) {
        switch (unitType) {

            // currency
            case USD:
                return new UnitConverter(new UnitTypeValue(unitType, toUSD()));
            case RMB:
                return new UnitConverter(new UnitTypeValue(unitType, toRMB()));

            // weights
            case G:
                return new UnitConverter(new UnitTypeValue(unitType, toGramsStr(scale)));
            case KG:
                return new UnitConverter(new UnitTypeValue(unitType, toKilosStr(scale)));
            case LB:
                return new UnitConverter(new UnitTypeValue(unitType, toPoundsStr(scale)));
            case OZ:
                return new UnitConverter(new UnitTypeValue(unitType, toOuncesStr(scale)));

            // measures
            case M:
                return new UnitConverter(new UnitTypeValue(unitType, toMetersStr(scale)));
            case CM:
                return new UnitConverter(new UnitTypeValue(unitType, toCentimetersStr(scale)));
            case MM:
                return new UnitConverter(new UnitTypeValue(unitType, toMillimetersStr(scale)));
            case IN:
                return new UnitConverter(new UnitTypeValue(unitType, toInchesStr(scale)));
            case FT:
                return new UnitConverter(new UnitTypeValue(unitType, toFeetStr(scale)));

            // power
            case A:
                return new UnitConverter(new UnitTypeValue(unitType, toAmpsStr()));
            case MA:
                return new UnitConverter(new UnitTypeValue(unitType, toMilliAmpsStr()));
            case VDC:
            case VAC:
            default:
                throw new AppRuntimeException("conversion not implemented for " + unitType);
        }
    }

    public UnitType getUnitType () {
        return getUnitTypeValue(true).getType();
    }

    public UnitTypeValue getUnitTypeValue(boolean required) {
        if (utv != null)
            return utv;

        UnitTypeValue type = null;

        type = getCurrencyType(false);
        if (type != null) return type;

        type = getLengthType(false);
        if (type != null) return type;

        type = getWeightType(false);
        if (type != null) return type;

        type = getVoltageType(false);
        if (type != null) return type;

        type = getAmperageType(false);
        if (type != null) return type;

        if (!required) return null;
        throw new IllegalArgumentException("unknown unit type value: " + input);
    }

    /**
     * returns a 'measure' type for the intrinsic value.
     *
     * @return
     * @throws IllegalArgumentException if value not a measure type
     */
    public UnitTypeValue getLengthType(boolean required) {
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

        if (!required) return null;
        throw new IllegalArgumentException("unknown measurement type: " + input);
    }

    public UnitTypeValue getVoltageType(boolean required) {
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

        if (!required) return null;
        throw new IllegalArgumentException("unknown voltage type: " + input);
    }

    public UnitTypeValue getAmperageType(boolean required) {
        if (utv != null)
            return utv;

        int index = -1;

        index = input.indexOf(MILLIAMP);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.MA, s);
        }

        index = input.indexOf(AMP);
        if (index != -1) {
            String s = input.substring(0, index).trim();
            return utv = new UnitTypeValue(UnitType.A, s);
        }

        if (!required) return null;
        throw new IllegalArgumentException("unknown amperage type: " + input);
    }

    /**
     * returns a 'currency' type for the intrinsic value.
     *
     * @return
     * @throws IllegalArgumentException if value not a measure type
     */
    public UnitTypeValue getCurrencyType(boolean required) {
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

        if (!required) return null;
        throw new IllegalArgumentException("unknown currency type: " + input);
    }

    /**
     * returns a 'weight' type for the intrinsic value.
     *
     * @return
     * @throws IllegalArgumentException if value not a measure type
     */
    public UnitTypeValue getWeightType(boolean required) {
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

        if (!required) return null;
        throw new IllegalArgumentException("unknown weight type: " + input);
    }

    // ////////////////////////////////
    // currency conversions
    // ////////////////////////////////

    public String toUSD() {
        UnitTypeValue utv = getCurrencyType(true);
        if (utv.type == UnitType.USD)
            return utv.value;

        throw new UnsupportedOperationException(input);
    }

    public String toRMB() {
        UnitTypeValue utv = getCurrencyType(true);
        if (utv.type == UnitType.RMB)
            return utv.value;

        throw new UnsupportedOperationException(input);
    }

    // ////////////////////////////////
    // weight conversions
    // ////////////////////////////////

    public String toPoundsStr(int scale) {
        return toPounds(scale).toString();
    }

    public BigDecimal toPounds(int scale) {
        UnitTypeValue utv = getWeightType(true);

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

    public String toKilosStr(int scale) {
        return toKilos(scale).toString();
    }

    public BigDecimal toKilos(int scale) {
        UnitTypeValue utv = getWeightType(true);

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

    public String toOuncesStr(int scale) {
        return toOunces(scale).toString();
    }

    public BigDecimal toOunces(int scale) {
        UnitTypeValue utv = getWeightType(true);

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

    public String toGramsStr(int scale) {
        return toGrams(scale).toString();
    }

    public BigDecimal toGrams(int scale) {
        UnitTypeValue utv = getWeightType(true);

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

    public String toMetersStr(int scale) {
        return toMeters(scale).toString();
    }

    public BigDecimal toMeters(int scale) {
        UnitTypeValue utv = getLengthType(true);

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

    public String toCentimetersStr(int scale) {
        return toCentimeters(scale).toString();
    }

    public BigDecimal toCentimeters(int scale) {
        UnitTypeValue utv = getLengthType(true);

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

    public String toMillimetersStr(int scale) {
        return toMillimeters(scale).toString();
    }

    public BigDecimal toMillimeters(int scale) {
        UnitTypeValue utv = getLengthType(true);

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

    public String toFeetStr(int scale) {
        return toFeet(scale).toString();
    }

    public BigDecimal toFeet(int scale) {
        UnitTypeValue utv = getLengthType(true);

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

    public String toInchesStr(int scale) {
        return toInches(scale).toString();
    }

    public BigDecimal toInches(int scale) {
        UnitTypeValue utv = getLengthType(true);

        if (utv.type == UnitType.M)
            return convert("39.3701", scale);

        if (utv.type == UnitType.CM)
            return convert("0.393701", scale);

        if (utv.type == UnitType.MM)
            return convert("0.00393701", scale);

        if (utv.type == UnitType.FT)
            return convert("12", scale);

        if (utv.type == UnitType.IN)
            return convert("1", scale);

        throw new UnsupportedOperationException(input);
    }

    //////////////////////
    // Power conversions
    //////////////////////

    public String toMilliAmpsStr() {
        return toMilliAmps().toString();
    }

    public BigDecimal toMilliAmps() {
        UnitTypeValue utv = getAmperageType(true);

        if (utv.type == UnitType.MA)
            return convert("1", 0);

        if (utv.type == UnitType.A)
            /// 1000 mA per Amp
            return convert("1000", 0);

        throw new UnsupportedOperationException(input);
    }

    public String toAmpsStr() {
        return toAmps().toString();
    }

    public BigDecimal toAmps() {
        UnitTypeValue utv = getAmperageType(true);

        if (utv.type == UnitType.MA)
            /// 1000 mA per Amp
            return convert(".001", 0);

        if (utv.type == UnitType.A)
            return convert("1", 0);

        throw new UnsupportedOperationException(input);
    }
    /**
     * converts the unit value and multiplier to BigDecimals, multiplies them,
     * and returns a string with the precision given.
     *
     * @param multiplier
     * @param scale
     * @return
     */
    private BigDecimal convert(String multiplier, int scale) {
        BigDecimal v1 = new BigDecimal(utv.value);
        BigDecimal v2 = new BigDecimal(multiplier);
        BigDecimal v3 = v1.multiply(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);

        return v3;
    }

}
