package com.example.OOP.backend;

import java.util.ArrayList;

import java.util.List;

/**
 * CarModel entity representing car models in the system
 * Maps to the car_models table in the database
 */
public class CarModel {
    
    private int modelId;
    private int manufacturerId;
    private String modelName;
    private Integer yearIntroduced;
    private CarCategory category;
    
    // Related objects
    private Manufacturer manufacturer;
    private List<Car> cars;
    private Specification specification;
    
    /**
     * Car category enumeration
     */
    public enum CarCategory {
        SEDAN("sedan"),
        SUV("suv"),
        TRUCK("truck"),
        SPORTS("sports"),
        HATCHBACK("hatchback"),
        VAN("van"),
        LUXURY("luxury"),
        ELECTRIC("electric");
        
        private final String value;
        
        CarCategory(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static CarCategory fromString(String value) {
            for (CarCategory category : CarCategory.values()) {
                if (category.value.equalsIgnoreCase(value)) {
                    return category;
                }
            }
            return SEDAN; // default
        }
        
        @Override
        public String toString() {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }
    }
    
    /**
     * Default constructor
     */
    public CarModel() {
    }
    
    /**
     * Constructor with basic details
     */
    public CarModel(int manufacturerId, String modelName, Integer yearIntroduced, CarCategory category) {
        this.manufacturerId = manufacturerId;
        this.modelName = modelName;
        this.yearIntroduced = yearIntroduced;
        this.category = category;
    }
    
    /**
     * Full constructor
     */
    public CarModel(int modelId, int manufacturerId, String modelName, 
                   Integer yearIntroduced, CarCategory category) {
        this.modelId = modelId;
        this.manufacturerId = manufacturerId;
        this.modelName = modelName;
        this.yearIntroduced = yearIntroduced;
        this.category = category;
    }
    
    // Getters and Setters
    
    public int getModelId() {
        return modelId;
    }
    
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
    
    public int getManufacturerId() {
        return manufacturerId;
    }
    
    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
    
    public String getModelName() {
        return modelName;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public Integer getYearIntroduced() {
        return yearIntroduced;
    }
    
    public void setYearIntroduced(Integer yearIntroduced) {
        this.yearIntroduced = yearIntroduced;
    }
    
    public CarCategory getCategory() {
        return category;
    }
    
    public void setCategory(CarCategory category) {
        this.category = category;
    }
    
    public Manufacturer getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public List<Car> getCars() {
        return cars;
    }
    
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
    
    public Specification getSpecification() {
        return specification;
    }
    
    public void setSpecification(Specification specification) {
        this.specification = specification;
    }
    
    // Utility methods
    
    /**
     * Get full model name including manufacturer
     */
    public String getFullName() {
        if (manufacturer != null) {
            return manufacturer.getName() + " " + modelName;
        }
        return modelName;
    }
    
    /**
     * Get category display name
     */
    public String getCategoryDisplayName() {
        return category != null ? category.toString() : "Unknown";
    }
    
    @Override
    public String toString() {
        return "CarModel{" +
                "modelId=" + modelId +
                ", fullName='" + getFullName() + '\'' +
                ", category=" + category +
                ", yearIntroduced=" + yearIntroduced +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CarModel carModel = (CarModel) obj;
        return modelId == carModel.modelId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(modelId);
    }
}