package com.gbroche.model;

public class Customer {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String address1;
    private final String address2;
    private final String city;
    private final String state;
    private final int zip;
    private final String country;
    private final int region;

    public Customer(int id, String firstName, String lastName, String address1, String address2, String city, String state, int zip, String country, int region) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getFullAddress() {
        return address2 != null ? address1 + " - " + address2 : address1;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public int getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public int getRegion() {
        return region;
    }

}
