package org.fenixedu.treasury.domain.sibsonlinepaymentsgateway;

public class SibsBillingAddressBean {

    private String addressCountryCode;
    private String city;
    private String address;
    private String zipCode;
    
    public SibsBillingAddressBean() {
    }
    
    // *****************
    // GETTERS & SETTERS
    // *****************

    public String getAddressCountryCode() {
        return addressCountryCode;
    }

    public void setAddressCountryCode(String addressCountryCode) {
        this.addressCountryCode = addressCountryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    

    
    
}
