package com.example.smartwater.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Address implements Serializable {

    private static final long serialVersionUID = -7377543095355015517L;

    private UUID id;
    private String city;
    private String street;
    private int building;
    private int apartment;

    public Address(UUID id, String city, String street, int building, int apartment) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
    }

    public Address(String id, String city, String street, int building, int apartment) {
        this.id = UUID.fromString(id);
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
    }

    public Address(String city, String street, int building, int apartment) {
        this.id = UUID.randomUUID();
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public int getApartment() {
        return apartment;
    }

    public void setApartment(int apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return city + ", " + street + ", " + building + ", " + apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (building != address.building) return false;
        if (apartment != address.apartment) return false;
        if (!Objects.equals(id, address.id)) return false;
        if (!Objects.equals(city, address.city)) return false;
        return Objects.equals(street, address.street);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + building;
        result = 31 * result + apartment;
        return result;
    }
}
