package part;

public enum PartPropertyType {

    //LINKABLE("Multiple units can be daisy-chained together"),
    VOLTAGE("Voltage rating for the circuit"),
    VOLTAGE_INPUT("Input voltage usually applies to power sources only"),

    AMPERAGE("Amperage rating for the circuit"),

    ADHESIVE("Mounting Adhesive"),

    LED_TYPE("LED type"), 
    LED_COLOR("LED color"), 
    LED_COLOR_TEMP("Tone or shade of white LED"),
    LED_BEAM_ANGLE("LED beam angle expressed in degrees"),

    LEAD_LENGTH("Lead wire length"),
    MALE_LEAD_LENGTH("Male connector lead wire length with"),
    FEMALE_LEAD_LENGTH("Female connector lead wire length with "),

    FLEX_LENGTH("LED Flex flexible strip length"),
    //FLEX_MALE_LEAD_LENGTH("LED flexible strip output lead wire length"),
    //FLEX_FEMALE_LEAD_LENGTH("LED flexible strip input lead wire length"),

    STRIP_LENGTH("LED rigid strip Length"),
    //STRIP_MALE_LEAD_LENGTH("LED rigid strip output lead wire length"),
    //STRIP_FEMALE_LEAD_LENGTH("LED rigid strip input lead wire length"),

    CLUSTER_SIZE("LED PCB cluster dimensions (2x2)"),
    CLUSTER_LED_COUNT("LED PCB cluster number of LEDs"),

    SWITCH("Circuit switch"),

    TAPE_TYPE(""),
    TAPE_LOCATIONS(""),

    HARNESS (""),

    POWER_RECHARGEABLE("Rechargeable capability"),
    POWER_CORD_LENGTH("Power cord length");

    String description;

    PartPropertyType(String description) {
        this.description = description;
    }
}