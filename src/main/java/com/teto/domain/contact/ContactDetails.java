package com.teto.domain.contact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.provenance.Provenance;

import java.util.Collection;
import java.util.HashSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactDetails  {
    private String companyName;
    private Collection<ContactName> contacts = new HashSet<>();
    private Collection<Address> addresses = new HashSet<>();
    private Collection<TelNumber> telNumbers = new HashSet<>();
    private String coordinates;
    private Collection<SocialMedia> socialMedias = new HashSet<>();
    private String countryName;
    private Provenance provenance;
    @JsonIgnore
    private boolean modified = false;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Collection<ContactName> getContacts() {
        return contacts;
    }

    public void setContacts(Collection<ContactName> contacts) {
        this.contacts = contacts;
    }

    public Collection<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<Address> addresses) {
        this.addresses = addresses;
    }

    public Collection<TelNumber> getTelNumbers() {
        return telNumbers;
    }

    public void setTelNumbers(Collection<TelNumber> telNumbers) {
        this.telNumbers = telNumbers;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Collection<SocialMedia> getSocialMedias() {
        return socialMedias;
    }

    public void setSocialMedias(Collection<SocialMedia> socialMedias) {
        this.socialMedias = socialMedias;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Provenance getProvenance() {
        return provenance;
    }

    public void setProvenance(Provenance provenance) {
        this.provenance = provenance;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
