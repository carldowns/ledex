package supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * REST Representation for a Supplier
 * @author carl_downs
 *
 */
@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public class Supplier {

    @JsonProperty ("supplierID")
    private String supplierID;

    @JsonProperty ("name")
    private String name;

    @JsonProperty ("phone")
    private List<Phone> phone;

    @JsonProperty ("address")
    private List<Address> address;

    @JsonProperty ("internet")
    private List<Internet> internet;

    @JsonProperty ("contact")
    private List<Contact> contact;

    public Supplier() {
    }

    public Supplier(String id, String name) {
        this.supplierID = id;
        this.name = name;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String id)
    {
        this.supplierID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    //////////////////////////
    // Json Inner Classes
    //////////////////////////

    @JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    static class Contact {

        @JsonProperty ("name")
        private String name;

        @JsonProperty ("title")
        private String title;

        @JsonProperty ("phone")
        private List<Phone> phone;

        @JsonProperty ("address")
        private List<Address> address;

        @JsonProperty ("internet")
        private List<Internet> internet;
    }

    @JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    static class Phone {
        static enum PhoneType {office,office_direct,office_mail,cell,home}

        @JsonProperty ("type")
        PhoneType type;

        @JsonProperty ("countryCode")
        private String countryCode;

        @JsonProperty ("number")
        private String phoneNumber;
    }

    @JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    static class Address {
        static enum AddressType {office,office_field,factory,home}

        @JsonProperty ("type")
        AddressType type;

        @JsonProperty ("street")
        private String street;

        @JsonProperty ("city")
        private String city;

        @JsonProperty ("state")
        private String state;

        @JsonProperty ("code")
        private String code;

        @JsonProperty ("region")
        private String region;

        @JsonProperty ("province")
        private String province;

        @JsonProperty ("country")
        private String country;
    }

    @JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    static class Internet {
        static enum InternetType {web,web_mobile,email,email_work,email_company,email_personal,skype,chat,we_chat,hip_chat}

        @JsonProperty ("type")
        InternetType type;

        @JsonProperty ("address")
        private String address;
    }

}