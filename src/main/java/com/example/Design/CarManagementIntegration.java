package com.example.Design;

import com.example.OOP.backend.*;
import com.example.Database.DatabaseConnection;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Integration class showing how to connect your existing GUI with the new backend
 * This replaces the singleton CarStatusManager with actual database operations
 */
public class CarManagementIntegration {
    
    private static CarManagementIntegration instance;
    private CarService carService;
    private int currentStaffId = 1; // Default staff ID - should come from login
    
    private CarManagementIntegration() {
        this.carService = new CarService();
    }
    
    public static CarManagementIntegration getInstance() {
        if (instance == null) {
            instance = new CarManagementIntegration();
        }
        return instance;
    }
    
    /**
     * Set current logged-in staff ID
     */
    public void setCurrentStaffId(int staffId) {
        this.currentStaffId = staffId;
    }
    
    /**
     * Convert your existing Car class to backend Car model
     */
    private Car convertToBackendCar(CarManagement.Car frontendCar) {
        Car backendCar = new Car();
        
        backendCar.setCarId(frontendCar.getId());
        // backendCar.setVin(frontendCar.getVin() != null ? frontendCar.getVin() : generateVIN());
        backendCar.setColor(frontendCar.getColor());
        backendCar.setManufactureYear(Integer.parseInt(frontendCar.getYear()));
        
        // Parse price - remove $ and commas
        String priceStr = frontendCar.getPrice().replace("$", "").replace(",", "");
        backendCar.setPrice(new BigDecimal(priceStr));
        
        // Set a default cost (80% of price for example)
        backendCar.setCost(backendCar.getPrice().multiply(new BigDecimal("0.8")));
        
        // Set model ID - you'll need to map this properly
        backendCar.setModelId(getModelIdFromType(frontendCar.getType()));
        
        // Convert status
        backendCar.setStatus(Car.CarStatus.fromString(frontendCar.getStatus().toLowerCase()));
        
        // Set other fields
        backendCar.setImageUrl(frontendCar.getImagePath());
        backendCar.setAddedBy(currentStaffId);
        
        return backendCar;
    }
    
    /**
     * Convert backend Car model to your existing Car class
     */
    private CarManagement.Car convertToFrontendCar(Car backendCar) {
        String model = backendCar.getCarModel() != null ? backendCar.getCarModel().getModelName() : "Unknown";
        String manufacturer = "";
        if (backendCar.getCarModel() != null && backendCar.getCarModel().getManufacturer() != null) {
            manufacturer = backendCar.getCarModel().getManufacturer().getName();
            model = manufacturer + " " + model;
        }
        
        String type = backendCar.getCarModel() != null ? 
                     backendCar.getCarModel().getCategory().toString() : "Sedan";
        
        String statusText = backendCar.getStatus().getValue();
        // Convert first letter to uppercase
        statusText = statusText.substring(0, 1).toUpperCase() + statusText.substring(1);
        
        return new CarManagement.Car(
            backendCar.getCarId(),
            model,
            String.valueOf(backendCar.getManufactureYear()),
            type,
            backendCar.getColor(),
            backendCar.getFormattedPrice(),
            statusText,
            backendCar.getDateAdded() != null ? 
                new java.text.SimpleDateFormat("MMM dd, yyyy").format(backendCar.getDateAdded()) : 
                "Today",
            backendCar.getImageUrl() != null ? backendCar.getImageUrl() : "placeholder.jpg"
        );
    }
    
    /**
     * Add a new car to inventory
     */
    public boolean addCar(CarManagement.Car frontendCar) {
        try {
            Car backendCar = convertToBackendCar(frontendCar);
            ServiceResult<Car> result = carService.addCar(backendCar, currentStaffId);
            
            if (result.isSuccess()) {
                // Update the frontend car with the generated ID
                frontendCar.setId(result.getData().getCarId());
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Error adding car: " + result.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error adding car: " + e.getMessage(), 
                "System Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
   /**
 * Update an existing car in the database and UI
 */
public boolean updateCar(CarManagement.Car frontendCar) {
    try {
        System.out.println("Updating car ID: " + frontendCar.getId() + " - " + frontendCar.getModel());
        
        // Convert frontend car to backend car
        Car backendCar = convertToBackendCar(frontendCar);
        
        // Get existing car from database to preserve any data not updated by the UI
        ServiceResult<Car> existingCarResult = carService.getCarById(backendCar.getCarId());
        if (!existingCarResult.isSuccess() || existingCarResult.getData() == null) {
            JOptionPane.showMessageDialog(null, 
                "Error: Car not found with ID: " + backendCar.getCarId(), 
                "Update Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        Car existingCar = existingCarResult.getData();
        
        // Preserve fields that might not be set by the UI
        if (backendCar.getVin() == null || backendCar.getVin().isEmpty()) {
            backendCar.setVin(existingCar.getVin());
        }
        
        if (backendCar.getCost() == null || backendCar.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            backendCar.setCost(existingCar.getCost());
        }
        
        if (backendCar.getDateAdded() == null) {
            backendCar.setDateAdded(existingCar.getDateAdded());
        }
        
        if (backendCar.getAddedBy() <= 0) {
            backendCar.setAddedBy(existingCar.getAddedBy());
        }
        
        if (backendCar.getMileage() <= 0) {
            backendCar.setMileage(existingCar.getMileage());
        }
        
        if (backendCar.getLocation() == null || backendCar.getLocation().isEmpty()) {
            backendCar.setLocation(existingCar.getLocation());
        }
        
        // Use the service to update the car in the database
        ServiceResult<Car> result = carService.updateCar(backendCar, currentStaffId);
        
        if (result.isSuccess()) {
            // Also update the CarStatusManager to maintain UI state
            try {
                CarStatusManager.getInstance().updateCar(frontendCar);
                System.out.println("UI state updated successfully");
            } catch (Exception uiEx) {
                System.err.println("Warning: Database updated but UI state update failed: " + uiEx.getMessage());
                // Continue anyway - the database is the source of truth
            }
            
            // Create an audit log for this update
            try {
                AuditLogDAO auditLogDAO = new AuditLogDAO();
                auditLogDAO.logAction(currentStaffId, "Car Updated", 
                    "Updated car: " + backendCar.getDisplayName() + " (ID: " + backendCar.getCarId() + ")", 
                    "Success");
            } catch (Exception logEx) {
                System.err.println("Warning: Car updated but audit logging failed: " + logEx.getMessage());
                // Continue anyway - the update is still successful
            }
            
            JOptionPane.showMessageDialog(null, 
                "Car updated successfully!", 
                "Update Complete", 
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                "Error updating car: " + result.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Error updating car: " + e.getMessage(), 
            "System Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
}
    /**
     * Get all cars with status filter
     */
    public List<CarManagement.Car> getCarsByStatus(String status) {
        try {
            CarDAO.CarFilter filter = new CarDAO.CarFilter();
            filter.setStatus(Car.CarStatus.fromString(status));
            
            ServiceResult<List<Car>> result = carService.getCars(filter);
            
            if (result.isSuccess()) {
                List<CarManagement.Car> frontendCars = new ArrayList<>();
                for (Car backendCar : result.getData()) {
                    frontendCars.add(convertToFrontendCar(backendCar));
                }
                return frontendCars;
            } else {
                System.err.println("Error getting cars by status: " + result.getMessage());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error getting cars by status: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
  /**
 * Sell a car - updates both database and UI state
 */
public boolean sellCar(int carId, String buyerName, String buyerContact, 
                      String salePrice, String paymentMethod) {
    try {
        // First, get the car from database to access its cost
        ServiceResult<Car> carResult = carService.getCarById(carId);
        if (!carResult.isSuccess() || carResult.getData() == null) {
            JOptionPane.showMessageDialog(null, 
                "Error: Car not found with ID: " + carId, 
                "Sale Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        Car car = carResult.getData();
        
        // Create sale object
        Sale sale = new Sale();
        sale.setBuyerName(buyerName);
        sale.setBuyerContact(buyerContact);
        
        // Parse sale price
        String cleanPrice = salePrice.replace("$", "").replace(",", "");
        sale.setSalePrice(new BigDecimal(cleanPrice));
        
        // Set payment method
        sale.setPaymentMethod(Sale.PaymentMethod.fromString(paymentMethod.toLowerCase()));
        
        // Calculate tax (8%) and total amount
        BigDecimal taxRate = new BigDecimal("0.08");
        BigDecimal taxAmount = sale.getSalePrice().multiply(taxRate);
        BigDecimal totalAmount = sale.getSalePrice().add(taxAmount);
        
        sale.setTaxAmount(taxAmount);
        sale.setTotalAmount(totalAmount);
        sale.setPaymentStatus(Sale.PaymentStatus.COMPLETED);
        
        // Use the service to sell the car (this updates the database)
        ServiceResult<Sale> result = carService.sellCar(carId, sale, currentStaffId);
        
        if (result.isSuccess()) {
            // Also update the UI state through CarStatusManager
            try {
                CarStatusManager.getInstance().sellCar(
                    carId, buyerName, buyerContact, salePrice, paymentMethod
                );
                
                System.out.println("UI state updated successfully: Car marked as sold in CarStatusManager");
            } catch (Exception uiEx) {
                System.err.println("Warning: Database updated but UI state update failed: " + uiEx.getMessage());
                // Continue anyway - the database is the source of truth
            }
            
            // Create an audit log for this sale (for tracking and financial reports)
            try {
                AuditLogDAO auditLogDAO = new AuditLogDAO();
                
                // Log the sale action
                auditLogDAO.logAction(currentStaffId, "Car Sold", 
                    "Sold car: " + car.getDisplayName() + " to " + buyerName + 
                    " for " + salePrice + " using " + paymentMethod, 
                    "Success");
                
                // Log financial details for economic reports
                BigDecimal cost = car.getCost() != null ? car.getCost() : BigDecimal.ZERO;
                BigDecimal profit = sale.getSalePrice().subtract(cost);
                
                auditLogDAO.logAction(currentStaffId, "Financial Transaction", 
                    "Sale Revenue: " + salePrice + ", Cost: " + cost + 
                    ", Profit: " + profit, 
                    "Success");
                
                System.out.println("Audit logs created successfully for the sale and financial data");
            } catch (Exception logEx) {
                System.err.println("Warning: Sale completed but audit logging failed: " + logEx.getMessage());
                // Continue anyway - the sale is still successful
            }
            
            JOptionPane.showMessageDialog(null, 
                "Car sold successfully!\n" + result.getMessage() + 
                "\n\nThe car has been moved to the Sold Cars section and financial data has been recorded.", 
                "Sale Complete", 
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                "Error selling car: " + result.getMessage(), 
                "Sale Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Error selling car: " + e.getMessage(), 
            "System Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
}
    
    /**
     * Delete a car
     */
    public boolean deleteCar(int carId) {
        try {
            ServiceResult<Boolean> result = carService.deleteCar(carId, currentStaffId);
            
            if (result.isSuccess()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Error deleting car: " + result.getMessage(), 
                    "Delete Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error deleting car: " + e.getMessage(), 
                "System Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Search cars
     */
    public List<CarManagement.Car> searchCars(String keyword) {
        try {
            ServiceResult<List<Car>> result = carService.searchCars(keyword);
            
            if (result.isSuccess()) {
                List<CarManagement.Car> frontendCars = new ArrayList<>();
                for (Car backendCar : result.getData()) {
                    frontendCars.add(convertToFrontendCar(backendCar));
                }
                return frontendCars;
            } else {
                System.err.println("Error searching cars: " + result.getMessage());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error searching cars: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get inventory statistics for dashboard
     */
    public CarService.InventoryStats getInventoryStats() {
        return carService.getInventoryStats();
    }
    
    /**
     * Helper method to get or create a car model ID
     */
    private int getOrCreateCarModelId(String manufacturer, String modelName, String category) {
        try {
            // Create a DAO to work with car models
            // This is a simplified implementation - you might need to adjust based on your actual schema
            java.sql.Connection conn = DatabaseConnection.getInstance().getConnection();
            
            // First check if manufacturer exists
            int manufacturerId = -1;
            String manufacturerQuery = "SELECT manufacturer_id FROM manufacturers WHERE name = ?";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(manufacturerQuery)) {
                pstmt.setString(1, manufacturer);
                java.sql.ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    manufacturerId = rs.getInt("manufacturer_id");
                } else {
                    // Create new manufacturer
                    String insertManufacturerQuery = "INSERT INTO manufacturers (name, country) VALUES (?, 'Unknown')";
                    try (java.sql.PreparedStatement insertStmt = conn.prepareStatement(insertManufacturerQuery, 
                                                                         java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        insertStmt.setString(1, manufacturer);
                        insertStmt.executeUpdate();
                        
                        java.sql.ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            manufacturerId = generatedKeys.getInt(1);
                        } else {
                            throw new Exception("Failed to create manufacturer, no ID obtained.");
                        }
                    }
                }
            }
            
            // Now check if car model exists
            String modelQuery = "SELECT model_id FROM car_models WHERE manufacturer_id = ? AND model_name = ?";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(modelQuery)) {
                pstmt.setInt(1, manufacturerId);
                pstmt.setString(2, modelName);
                java.sql.ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    return rs.getInt("model_id");
                } else {
                    // Create new car model
                    String insertModelQuery = "INSERT INTO car_models (manufacturer_id, model_name, category) VALUES (?, ?, ?)";
                    try (java.sql.PreparedStatement insertStmt = conn.prepareStatement(insertModelQuery, 
                                                                      java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        insertStmt.setInt(1, manufacturerId);
                        insertStmt.setString(2, modelName);
                        insertStmt.setString(3, category.toLowerCase());
                        insertStmt.executeUpdate();
                        
                        java.sql.ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        } else {
                            throw new Exception("Failed to create car model, no ID obtained.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting/creating car model: " + e.getMessage());
            e.printStackTrace();
            return getModelIdFromType(category); // Fallback to default mapping
        }
    }
    
    /**
     * Helper method to map car type to model ID
     * You'll need to implement this based on your car_models table
     */
    private int getModelIdFromType(String type) {
        // This is a simplified mapping - you should create a proper lookup
        switch (type.toLowerCase()) {
            case "sedan": return 1;
            case "suv": return 2;
            case "truck": return 3;
            case "sports": return 4;
            case "luxury": return 5;
            case "hatchback": return 6;
            case "van": return 7;
            case "electric": return 8;
            default: return 1; // Default to sedan
        }
    }
    
    /**
     * Generate a temporary VIN if not provided
     */
    private String generateVIN() {
        return "TMP" + System.currentTimeMillis() % 100000000000000L;
    }
    
  public boolean saveCarFromAddCarUI(String vin, String manufacturer, String model, 
                                 int year, String category, String color, 
                                 double salePrice, double costPrice, int mileage,
                                 String location, String engineType, String transmission,
                                 String fuelType, int horsepower, int seats,
                                 String fuelEconomy, String features, String notes,
                                 String imagePath) {
    try {
        // ADDED THIS LINE: Validate staff ID before using it
        int staffId = ensureValidStaffId(currentStaffId);
        
        // First, get or create the car model
        int modelId = getOrCreateCarModelId(manufacturer, model, category);
        
        // Create backend car object
        Car backendCar = new Car();
        backendCar.setVin(vin);
        backendCar.setColor(color);
        backendCar.setManufactureYear(year);
        backendCar.setPrice(BigDecimal.valueOf(salePrice));
        backendCar.setCost(BigDecimal.valueOf(costPrice));
        backendCar.setMileage(mileage);
        backendCar.setLocation(location);
        backendCar.setImageUrl(imagePath);
        backendCar.setNotes(notes);
        backendCar.setStatus(Car.CarStatus.AVAILABLE);
        backendCar.setModelId(modelId);
        backendCar.setAddedBy(staffId); // FIXED: Use validated staff ID
        
        // Create specification if needed
        try {
            Specification spec = new Specification();
            spec.setModelId(modelId);
            spec.setEngineType(engineType);
            spec.setTransmission(transmission);
            spec.setFuelType(fuelType);
            spec.setHorsepower(horsepower);
            spec.setSeats(seats);
            spec.setFeatures(features);
            
            if (fuelEconomy != null && !fuelEconomy.isEmpty()) {
                try {
                    spec.setFuelEconomy(Double.parseDouble(fuelEconomy));
                } catch (NumberFormatException e) {
                    // Ignore if not a valid number
                }
            }
            
            // Save specification - this would normally go through a service class
            // but for simplicity we'll just print a message
            System.out.println("Would save specification: " + spec);
        } catch (Exception e) {
            System.err.println("Error creating specification: " + e.getMessage());
            // Continue anyway - the car can still be saved without specs
        }
        
        // Add the car through the service
        ServiceResult<Car> result = carService.addCar(backendCar, staffId); // FIXED: Use validated staff ID
        
        if (result.isSuccess()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                "Error saving car: " + result.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Error saving car: " + e.getMessage(), 
            "System Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
}


    /**
 * Ensure we have a valid staff ID that exists in the database
 * If not, return a default ID that we know exists
 */
private int ensureValidStaffId(int requestedId) {
    try {
        java.sql.Connection conn = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT COUNT(*) FROM staff WHERE staff_id = ?";
        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, requestedId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return requestedId; // ID exists, use it
                }
            }
        }
        
        // ID doesn't exist, try to get any existing staff ID
        query = "SELECT staff_id FROM staff LIMIT 1";
        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query);
             java.sql.ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("staff_id"); // Use any existing staff ID
            }
        }
        
        // If all else fails, just return 1 and hope for the best
        return 1;
        
    } catch (Exception e) {
        System.err.println("Error validating staff ID: " + e.getMessage());
        e.printStackTrace();
        return 1; // Default to ID 1 if there's an error
    }
}
}