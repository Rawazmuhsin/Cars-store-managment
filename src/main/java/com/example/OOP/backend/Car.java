package com.example.OOP.backend;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Car entity model representing a car in the inventory
 * Maps to the cars table in the database
 */
public class Car {
    
    // Primary key
    private int carId;
    
    // Foreign keys
    private int modelId;
    private int addedBy;
    
    // Car details
    private String vin;
    private String color;
    private int manufactureYear;
    private int mileage;
    private BigDecimal price;
    private BigDecimal cost;
    private CarStatus status;
    private Timestamp dateAdded;
    private String location;
    private String imageUrl;
    private String notes;
    
    // Related objects (for joins)
    private CarModel carModel;
    private Staff addedByStaff;
    
    /**
     * Car status enumeration
     */
    public enum CarStatus {
        AVAILABLE("available"),
        SOLD("sold"),
        RESERVED("reserved"),
        MAINTENANCE("maintenance");
        
        private final String value;
        
        CarStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static CarStatus fromString(String value) {
            if (value == null) return AVAILABLE;
            for (CarStatus status : CarStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            return AVAILABLE; // default
        }
    }
    
    /**
     * Default constructor
     */
    public Car() {
        this.status = CarStatus.AVAILABLE;
        this.mileage = 0;
        this.dateAdded = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Constructor with basic details
     */
    public Car(int modelId, String vin, String color, int manufactureYear, 
               BigDecimal price, BigDecimal cost, int addedBy) {
        this();
        this.modelId = modelId;
        this.vin = vin;
        this.color = color;
        this.manufactureYear = manufactureYear;
        this.price = price;
        this.cost = cost;
        this.addedBy = addedBy;
    }
    
    /**
     * Full constructor
     */
    public Car(int carId, int modelId, String vin, String color, int manufactureYear,
               int mileage, BigDecimal price, BigDecimal cost, CarStatus status,
               Timestamp dateAdded, String location, String imageUrl, int addedBy, String notes) {
        this.carId = carId;
        this.modelId = modelId;
        this.vin = vin;
        this.color = color;
        this.manufactureYear = manufactureYear;
        this.mileage = mileage;
        this.price = price;
        this.cost = cost;
        this.status = status;
        this.dateAdded = dateAdded;
        this.location = location;
        this.imageUrl = imageUrl;
        this.addedBy = addedBy;
        this.notes = notes;
    }
    
    // Getters and Setters
    
    public int getCarId() {
        return carId;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public int getModelId() {
        return modelId;
    }
    
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
    
    public String getVin() {
        return vin;
    }
    
    public void setVin(String vin) {
        this.vin = vin;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public int getManufactureYear() {
        return manufactureYear;
    }
    
    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }
    
    public int getMileage() {
        return mileage;
    }
    
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getCost() {
        return cost;
    }
    
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    
    public CarStatus getStatus() {
        return status;
    }
    
    public void setStatus(CarStatus status) {
        this.status = status;
    }
    
    public Timestamp getDateAdded() {
        return dateAdded;
    }
    
    public void setDateAdded(Timestamp dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getAddedBy() {
        return addedBy;
    }
    
    public void setAddedBy(int addedBy) {
        this.addedBy = addedBy;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public CarModel getCarModel() {
        return carModel;
    }
    
    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }
    
    public Staff getAddedByStaff() {
        return addedByStaff;
    }
    
    public void setAddedByStaff(Staff addedByStaff) {
        this.addedByStaff = addedByStaff;
    }
    
    // Utility methods
    
    /**
     * Get profit from this car (price - cost)
     */
    public BigDecimal getProfit() {
        if (price != null && cost != null) {
            return price.subtract(cost);
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Get profit margin percentage
     */
    public double getProfitMargin() {
        if (price != null && cost != null && price.compareTo(BigDecimal.ZERO) > 0) {
            return getProfit().divide(price, 4, java.math.RoundingMode.HALF_UP)
                   .multiply(new BigDecimal("100")).doubleValue();
        }
        return 0.0;
    }
    
    /**
     * Check if car is available for sale
     */
    public boolean isAvailable() {
        return status == CarStatus.AVAILABLE;
    }
    
    /**
     * Check if car has been sold
     */
    public boolean isSold() {
        return status == CarStatus.SOLD;
    }
    
    /**
     * Get display name for the car (Manufacturer Model Year)
     */
    public String getDisplayName() {
        if (carModel != null && carModel.getManufacturer() != null) {
            return carModel.getManufacturer().getName() + " " + 
                   carModel.getModelName() + " " + manufactureYear;
        }
        return "Car #" + carId;
    }
    
    /**
     * Get formatted price string
     */
    public String getFormattedPrice() {
        if (price != null) {
            return String.format("$%,.2f", price);
        }
        return "$0.00";
    }
    
    /**
     * Get formatted cost string
     */
    public String getFormattedCost() {
        if (cost != null) {
            return String.format("$%,.2f", cost);
        }
        return "$0.00";
    }
    
    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", vin='" + vin + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", color='" + color + '\'' +
                ", price=" + getFormattedPrice() +
                ", status=" + status +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Car car = (Car) obj;
        return carId == car.carId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(carId);
    }
}