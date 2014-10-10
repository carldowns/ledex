package model;


public class PartProperty {

    PartPropertyType type;
    String name;
    String value;
    String skuLabel;
    String description;

    /**
     * Measurement constraint Typical measures are length, size, etc. measures
     * usually impact both price and dimensional weight as the increase
     */
    PartIncrement increment;

    public PartProperty(String type, String name, String value) {
        this.type = PartPropertyType.valueOf(type);
        this.name = name;
        this.value = value;
    }

    public PartProperty(PartPropertyType type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public PartProperty(String type, String name, PartIncrement increment) {
        this.type = PartPropertyType.valueOf(type);
        this.name = name;
        this.increment = increment;
    }
}
