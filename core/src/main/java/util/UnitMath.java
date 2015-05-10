package util;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.math.BigDecimal;

/**
 * utility class for performing math operations on 'unit' strings.
 *
 * @see Unit
 * @see Unit.UnitTypeValue
 * @see Unit.UnitType
 */

public class UnitMath {

    /**
     * unit strings must be of the same UnitType to be added.
     */
    public static String addUnits(String unitStr1, String unitStr2) {

        Unit unit1 = new Unit(unitStr1);
        Unit unit2 = new Unit(unitStr2);

        Unit.UnitTypeValue utv1 = unit1.getUnitTypeValue(true);
        Unit.UnitTypeValue utv2 = unit2.getUnitTypeValue(true);

        Preconditions.checkState(utv1.isSameType(utv2), Joiner.on(" ").join("UnitTypes differ",utv1, utv2));

        BigDecimal result = utv1.toDecimal().add(utv2.toDecimal());
        Unit.UnitTypeValue output = new Unit.UnitTypeValue(utv2.getType(), result.toString());
        return output.toTypeValueString();
    }

//    /**
//     * both unit values are converted to return type before addition is performed.
//     */
//    public static String addUnits(String unit1, String unit2, UnitConverter.UnitType returnType, int scale) {
//
//        UnitConverter uc1 = new UnitConverter(unit1).convertTo(returnType, scale);
//        UnitConverter uc2 = new UnitConverter(unit2).convertTo(returnType, scale);
//
//        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);
//        UnitConverter.UnitTypeValue utv2 = uc2.getUnitTypeValue(true);
//
//        BigDecimal result = utv1.toDecimal().add(utv2.toDecimal());
//        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(returnType, result.toString());
//        return output.toTypeValueString();
//    }

    /**
     * unit type is preserved
     */
    public static String multiplyInteger(String unitStr, String multiplier) {
        return multiplyInteger(unitStr, Integer.parseInt(multiplier));
    }

    /**
     * unit type is preserved
     */
    public static String multiplyInteger(String unitStr, int multiplier) {

        Unit uc1 = new Unit(unitStr);
        Unit.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().multiply(new BigDecimal(multiplier));
        Unit.UnitTypeValue output = new Unit.UnitTypeValue(utv1.getType(), result.toString());
        return output.toTypeValueString();
    }

    /**
     * unit type is preserved.
     * scale is the number of digits you want to the right of the decimal point.
     * scaling by BigDecimal.ROUND_HALF_UP is used.
     */
    public static String multiplyBigDecimal(String unitStr, BigDecimal multiplier, int scale) {

        Unit uc1 = new Unit(unitStr);
        Unit.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().multiply(multiplier).setScale(scale, BigDecimal.ROUND_HALF_UP);
        Unit.UnitTypeValue output = new Unit.UnitTypeValue(utv1.getType(), result.toString());
        return output.toTypeValueString();
    }

//    /**
//     * unit type is preserved
//     */
//    public static String multiplyInteger(String unit, String multiplier, UnitConverter.UnitType returnType, int scale) {
//        return multiplyInteger(unit, Integer.parseInt(multiplier), returnType, scale);
//    }
//
//    /**
//     * unit is converted to return type before multiplication is performed
//     */
//    public static String multiplyInteger(String unit, int multiplier, UnitConverter.UnitType returnType, int scale) {
//
//        UnitConverter uc1 = new UnitConverter(unit).convertTo(returnType, scale);
//        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);
//
//        BigDecimal result = utv1.toDecimal().multiply(new BigDecimal(multiplier));
//        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(returnType, result.toString());
//        return output.toTypeValueString();
//    }

//    /**
//     * unit1 return type is preserved
//     */
//    public static String multiplyUnits(String unit1, String unit2) {
//        return multiplyUnits (new Unit(unit1), new Unit(unit2));
//    }

    /**
     * return type matches unit1 return type
     */
    public static String multiplyUnits(Unit unit1, Unit unit2) {

        Unit.UnitTypeValue utv1 = unit1.getUnitTypeValue(true);
        Unit.UnitTypeValue utv2 = unit2.getUnitTypeValue(true);

        BigDecimal result = utv1.toDecimal().multiply(utv2.toDecimal());
        Unit.UnitTypeValue output = new Unit.UnitTypeValue(unit1.getUnitType(), result.toString());
        return output.toTypeValueString();
    }

//    /**
//     * unit1 and unit2 are both converted to return type before multiplication is performed
//     */
//    public static String multiplyUnits(String unit1, String unit2, UnitConverter.UnitType returnType, int scale) {
//
//        UnitConverter uc1 = new UnitConverter(unit1).convertTo(returnType, scale);
//        UnitConverter uc2 = new UnitConverter(unit2).convertTo(returnType, scale);
//
//        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);
//        UnitConverter.UnitTypeValue utv2 = uc2.getUnitTypeValue(true);
//
//        BigDecimal result = utv1.toDecimal().multiply(utv2.toDecimal());
//        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(returnType, result.toString());
//        return output.toTypeValueString();
//    }
//
//    /**
//     * unit1 and unit2 are both converted to respective types before multiplication is performed.
//     * return type is unit1Type.
//     */
//    public static String multiplyUnits(String unit1, UnitConverter.UnitType unit1Type, String unit2, UnitConverter.UnitType unit2Type) {
//
//        UnitConverter uc1 = new UnitConverter(unit1).convertTo(unit1Type, 0);
//        UnitConverter uc2 = new UnitConverter(unit2).convertTo(unit2Type, 0);
//
//        UnitConverter.UnitTypeValue utv1 = uc1.getUnitTypeValue(true);
//        UnitConverter.UnitTypeValue utv2 = uc2.getUnitTypeValue(true);
//
//        BigDecimal result = utv1.toDecimal().multiply(utv2.toDecimal());
//        UnitConverter.UnitTypeValue output = new UnitConverter.UnitTypeValue(utv1.getType(), result.toString());
//        return output.toTypeValueString();
//    }

}