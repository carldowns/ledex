package part;

public enum PartPropertyType {

    LINKABLE("Multiple units can be daisy-chained together"),
    VOLTAGE("Voltage rating for the circuit"),
    AMPERAGE("Amperage rating for the circuit"),
    LED_TYPE("LED type"), 
    LED_COLOR("LED color"), 
    LED_COLOR_TEMP("Tone or shade of white LED"), 
    LED_BEAM_ANGLE("LED beam angle expressed in degrees"),
    POWER_RECHARGEABLE("Rechargeable capability"),
    FLEX_INPUT_LEAD_LENGTH("LED flexible strip input lead wire length"), 
    FLEX_OUTPUT_LEAD_LENGTH("LED flexible strip output lead wire length"), 
    FLEX_LENGTH("LED Flex flexible strip length"),
    STRIP_LENGTH("LED rigid strip Length"),
    STRIP_INPUT_LEAD_LENGTH("LED rigid strip input lead wire length"), 
    STRIP_OUTPUT_LEAD_LENGTH("LED rigid strip output lead wire length"),
    CONNECTION_PLUG_TYPE("Connector plug type"),
    CLUSTER_DIM("LED PCB cluster dimensions"), 
    CLUSTER_LED_COUNT("LED PCB cluster number of LEDs"),
    SWITCH ("Circuit switch"),
    POWER_CORD ("Power cord");
    
    String description;

    PartPropertyType(String description) {
        this.description = description;
    }
}