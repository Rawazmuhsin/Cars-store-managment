package com.example.OOP.backend;

/**
 * Specification entity representing car specifications in the system
 * Maps to the specifications table in the database
 */
public class Specification {
    
    private int specId;
    private int modelId;
    private String engineType;
    private String transmission;
    private String fuelType;
    private Integer horsepower;
    private Integer seats;
    private Double fuelEconomy; // MPG
    private String features;
    private String driveType;
    private Double engineSize; // in liters
    private Integer cylinders;
    private String safetyRating;
    private String warrantyInfo;
    
    // Related objects
    private CarModel carModel;
    
    /**
     * Default constructor
     */
    public Specification() {
    }
    
    /**
     * Constructor with basic details
     */
    public Specification(int modelId, String engineType, String transmission, 
                        String fuelType, Integer horsepower, Integer seats) {
        this.modelId = modelId;
        this.engineType = engineType;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.horsepower = horsepower;
        this.seats = seats;
    }
    
    /**
     * Full constructor
     */
    public Specification(int specId, int modelId, String engineType, String transmission,
                        String fuelType, Integer horsepower, Integer seats, Double fuelEconomy,
                        String features, String driveType, Double engineSize, Integer cylinders,
                        String safetyRating, String warrantyInfo) {
        this.specId = specId;
        this.modelId = modelId;
        this.engineType = engineType;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.horsepower = horsepower;
        this.seats = seats;
        this.fuelEconomy = fuelEconomy;
        this.features = features;
        this.driveType = driveType;
        this.engineSize = engineSize;
        this.cylinders = cylinders;
        this.safetyRating = safetyRating;
        this.warrantyInfo = warrantyInfo;
    }
    
    // Getters and Setters
    
    public int getSpecId() {
        return specId;
    }
    
    public void setSpecId(int specId) {
        this.specId = specId;
    }
    
    public int getModelId() {
        return modelId;
    }
    
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
    
    public String getEngineType() {
        return engineType;
    }
    
    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }
    
    public String getTransmission() {
        return transmission;
    }
    
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    
    public String getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    
    public Integer getHorsepower() {
        return horsepower;
    }
    
    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }
    
    public Integer getSeats() {
        return seats;
    }
    
    public void setSeats(Integer seats) {
        this.seats = seats;
    }
    
    public Double getFuelEconomy() {
        return fuelEconomy;
    }
    
    public void setFuelEconomy(Double fuelEconomy) {
        this.fuelEconomy = fuelEconomy;
    }
    
    public String getFeatures() {
        return features;
    }
    
    public void setFeatures(String features) {
        this.features = features;
    }
    
    public String getDriveType() {
        return driveType;
    }
    
    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }
    
    public Double getEngineSize() {
        return engineSize;
    }
    
    public void setEngineSize(Double engineSize) {
        this.engineSize = engineSize;
    }
    
    public Integer getCylinders() {
        return cylinders;
    }
    
    public void setCylinders(Integer cylinders) {
        this.cylinders = cylinders;
    }
    
    public String getSafetyRating() {
        return safetyRating;
    }
    
    public void setSafetyRating(String safetyRating) {
        this.safetyRating = safetyRating;
    }
    
    public String getWarrantyInfo() {
        return warrantyInfo;
    }
    
    public void setWarrantyInfo(String warrantyInfo) {
        this.warrantyInfo = warrantyInfo;
    }
    
    public CarModel getCarModel() {
        return carModel;
    }
    
    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }
    
    // Utility methods
    
    /**
     * Get formatted engine description
     */
    public String getEngineDescription() {
        StringBuilder desc = new StringBuilder();
        
        if (engineSize != null && cylinders != null) {
            desc.append(engineSize).append("L ").append(cylinders).append("-Cylinder");
        } else if (engineType != null) {
            desc.append(engineType);
        }
        
        if (horsepower != null) {
            desc.append(" (").append(horsepower).append(" HP)");
        }
        
        return desc.toString();
    }
    
    /**
     * Get formatted fuel economy description
     */
    public String getFuelEconomyDescription() {
        if (fuelEconomy != null) {
            return fuelEconomy + " MPG";
        }
        return "N/A";
    }
    
    /**
     * Get transmission type display
     */
    public String getTransmissionDisplay() {
        if (transmission != null) {
            return transmission.substring(0, 1).toUpperCase() + transmission.substring(1);
        }
        return "N/A";
    }
    
    /**
     * Get fuel type display
     */
    public String getFuelTypeDisplay() {
        if (fuelType != null) {
            return fuelType.substring(0, 1).toUpperCase() + fuelType.substring(1);
        }
        return "N/A";
    }
    
    /**
     * Check if this is an electric vehicle
     */
    public boolean isElectric() {
        return fuelType != null && fuelType.toLowerCase().contains("electric");
    }
    
    /**
     * Check if this is a hybrid vehicle
     */
    public boolean isHybrid() {
        return fuelType != null && fuelType.toLowerCase().contains("hybrid");
    }
    
    /**
     * Get features as array
     */
    public String[] getFeaturesArray() {
        if (features != null && !features.trim().isEmpty()) {
            return features.split(",\\s*");
        }
        return new String[0];
    }
    
    /**
     * Add a feature to the features list
     */
    public void addFeature(String feature) {
        if (features == null || features.trim().isEmpty()) {
            features = feature;
        } else {
            features += ", " + feature;
        }
    }
    
    /**
     * Get summary of key specifications
     */
    public String getSpecificationSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (engineType != null) {
            summary.append("Engine: ").append(getEngineDescription()).append(" | ");
        }
        
        if (transmission != null) {
            summary.append("Trans: ").append(getTransmissionDisplay()).append(" | ");
        }
        
        if (fuelType != null) {
            summary.append("Fuel: ").append(getFuelTypeDisplay()).append(" | ");
        }
        
        if (seats != null) {
            summary.append("Seats: ").append(seats).append(" | ");
        }
        
        if (fuelEconomy != null) {
            summary.append("MPG: ").append(fuelEconomy);
        }
        
        // Remove trailing " | " if present
        String result = summary.toString();
        if (result.endsWith(" | ")) {
            result = result.substring(0, result.length() - 3);
        }
        
        return result;
    }
    
    /**
     * Validate specification data
     */
    public boolean isValid() {
        // Basic validation
        if (modelId <= 0) return false;
        if (engineType == null || engineType.trim().isEmpty()) return false;
        if (transmission == null || transmission.trim().isEmpty()) return false;
        if (fuelType == null || fuelType.trim().isEmpty()) return false;
        if (seats != null && (seats < 1 || seats > 10)) return false;
        if (horsepower != null && horsepower < 0) return false;
        if (fuelEconomy != null && fuelEconomy < 0) return false;
        if (engineSize != null && engineSize < 0) return false;
        if (cylinders != null && (cylinders < 1 || cylinders > 16)) return false;
        
        return true;
    }
    
    @Override
    public String toString() {
        return "Specification{" +
                "specId=" + specId +
                ", modelId=" + modelId +
                ", engineType='" + engineType + '\'' +
                ", transmission='" + transmission + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", horsepower=" + horsepower +
                ", seats=" + seats +
                ", fuelEconomy=" + fuelEconomy +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Specification that = (Specification) obj;
        return specId == that.specId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(specId);
    }
}