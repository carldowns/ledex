package part;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

/**
 *
 */
public class PartConnection {

    @JsonProperty("type")
    String type;

    @JsonProperty("gender")
    String gender;

    @Nullable
    private Boolean isMaleCached = null;

    @JsonProperty("pinOut")
    String pinOut;

    @JsonProperty("connect")
    PartConnectionMode connect = PartConnectionMode.required;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isFemale () {
        return !isMale();
    }

    public boolean isMale () {
        if (isMaleCached == null) {
            isMaleCached = "male".equals(gender);
        }
        return isMaleCached;
    }

    public boolean isSameGender (PartConnection other) {
        return other == null ? false : this.getGender().equals(other.getGender());
    }

    public String getPinOut() {
        return pinOut;
    }

    public void setPinOut(String pinOut) {
        this.pinOut = pinOut;
    }

    public PartConnectionMode getConnectMode () {
        return connect;
    }

    public boolean isConnectRequired () {
        return connect ==  PartConnectionMode.required;
    }

    public boolean isConnectOptional () {
        return connect ==  PartConnectionMode.optional;
    }

    public boolean isConnectRequiredOrOptional () {
        return connect == PartConnectionMode.required || connect == PartConnectionMode.optional;
    }

    public boolean isConnectIgnorable () {
        return connect == PartConnectionMode.ignore;
    }


    @Override
    public String toString() {
        return "{" +
                "type='" + type +
                ", gender='" + gender +
                ", connect=" + connect +
                //", internalID=" + internalID +
                '}';
    }
}
