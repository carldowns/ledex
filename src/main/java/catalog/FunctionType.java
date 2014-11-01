package catalog;

/**
*/
public enum FunctionType {
    POWER,
    AC_POWER, // ? do we define multiple parts in this way, or rely instead on a cardinality
    DC_POWER, // ?do we define multiple parts in this way, or rely instead on a cardinality
    LIGHT,
    CONTROLLER,
    HARNESS,
    SWITCH,
    SENSOR,
    PLUG,
    LEAD,
    CASE,
    CELL,
    CUSTOM,
    UNKNOWN
}

// possible cardinality options

//POWER_1
//POWER_0_TO_1
//POWER_0_TO_N
//POWER_1_TO_N

// or we set it as a cardinality property specifically on the Function

