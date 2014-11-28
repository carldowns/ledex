package util;

import java.math.BigDecimal;

/**
 * utility class for performing math operations on 'unit' strings.
 *
 * @see util.UnitConverter
 * @see util.UnitConverter.UnitTypeValue
 * @see util.UnitConverter.UnitType
 */

public class UnitMath {

    /**
     * unit2 is converted to unit1's type before addition is performed
     */
    public static String addUnits(String unit1, String unit2) {

        UnitConverter uc1 = new UnitConverter(unit1);
        UnitConverter uc2 = new UnitConverter(unit2).convertTo(uc1.getUnitType(), 0);

        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);
        UnitConverter.UnitTypeValue utv2 = uc2.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().add(utv2.toDecimal());
        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(utv2.getType(), result.toString());
        return output.toTypeValueString();
    }

    /**
     * both unit values are converted to return type before addition is performed.
     */
    public static String addUnits(String unit1, String unit2, UnitConverter.UnitType returnType, int scale) {

        UnitConverter uc1 = new UnitConverter(unit1).convertTo(returnType, scale);
        UnitConverter uc2 = new UnitConverter(unit2).convertTo(returnType, scale);

        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);
        UnitConverter.UnitTypeValue utv2 = uc2.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().add(utv2.toDecimal());
        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(returnType, result.toString());
        return output.toTypeValueString();
    }

    /**
     * unit type is preserved
     */
    public static String multiplyScalar(String unit, String multiplier) {
        return multiplyScalar(unit, Integer.parseInt(multiplier));
    }

    /**
     * unit type is preserved
     */
    public static String multiplyScalar(String unit, int multiplier) {

        UnitConverter uc1 = new UnitConverter(unit);
        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().multiply(new BigDecimal(multiplier));
        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(utv1.getType(), result.toString());
        return output.toTypeValueString();
    }

    /**
     * unit type is preserved
     */
    public static String multiplyScalar(String unit, String multiplier, UnitConverter.UnitType returnType, int scale) {
        return multiplyScalar(unit, Integer.parseInt(multiplier), returnType, scale);
    }

    /**
     * unit is converted to return type before multiplication is performed
     */
    public static String multiplyScalar(String unit, int multiplier, UnitConverter.UnitType returnType, int scale) {

        UnitConverter uc1 = new UnitConverter(unit).convertTo(returnType, scale);
        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().multiply(new BigDecimal(multiplier));
        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(returnType, result.toString());
        return output.toTypeValueString();
    }

    /**
     * unit1 and unit2 are both converted to return type before multiplication is performed
     */
    public static String multiplyUnits(String unit1, String unit2, UnitConverter.UnitType returnType, int scale) {

        UnitConverter uc1 = new UnitConverter(unit1).convertTo(returnType, scale);
        UnitConverter uc2 = new UnitConverter(unit2).convertTo(returnType, scale);

        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);
        UnitConverter.UnitTypeValue utv2 = uc2.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().multiply(utv2.toDecimal());
        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(returnType, result.toString());
        return output.toTypeValueString();
    }

    /**
     * no type conversion before multiplication is performed.  Type of unit1 parameter is preserved.
     */
    public static String multiplyUnits(String unit1, String unit2) {

        UnitConverter uc1 = new UnitConverter(unit1);
        UnitConverter uc2 = new UnitConverter(unit2);

        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);
        UnitConverter.UnitTypeValue utv2 = uc2.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().multiply(utv2.toDecimal());
        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(utv1.getType(), result.toString());
        return output.toTypeValueString();
    }
}