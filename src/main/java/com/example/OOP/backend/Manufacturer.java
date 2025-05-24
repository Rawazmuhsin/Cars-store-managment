package com.example.OOP.backend;


import java.util.List;

/**
 * Manufacturer entity representing car manufacturers
 * Maps to the manufacturers table in the database
 */
public class Manufacturer {
    
    private int manufacturerId;
    private String name;
    private String country;
    private String website;
    private String contactInfo;
    
    // Related objects
    private List<CarModel> carModels;
    
    /**
     * Default constructor
     */
    public Manufacturer() {
    }
    
    /**
     * Constructor with basic details
     */
    public Manufacturer(String name, String country) {
        this.name = name;
        this.country = country;
    }
    
    /**
     * Full constructor
     */
    public Manufacturer(int manufacturerId, String name, String country, 
                       String website, String contactInfo) {
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.country = country;
        this.website = website;
        this.contactInfo = contactInfo;
    }
    
    // Getters and Setters
    
    public int getManufacturerId() {
        return manufacturerId;
    }
    
    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public List<CarModel> getCarModels() {
        return carModels;
    }
    
    public void setCarModels(List<CarModel> carModels) {
        this.carModels = carModels;
    }
    
    @Override
    public String toString() {
        return "Manufacturer{" +
                "manufacturerId=" + manufacturerId +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Manufacturer that = (Manufacturer) obj;
        return manufacturerId == that.manufacturerId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(manufacturerId);
    }
}
