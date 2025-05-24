package com.example.Design;

import com.example.OOP.backend.CarService;
import com.example.OOP.backend.ServiceResult;
import com.example.OOP.backend.Car;
import com.example.OOP.backend.CarModel;
import com.example.OOP.backend.Sale;
import com.example.OOP.backend.CarDAO;
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
        backendCar.setStatus(Car.CarStatus.fromString(frontendCar.getStatus()));
        
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
        
        return new CarManagement.Car(
            backendCar.getCarId(),
            model,
            String.valueOf(backendCar.getManufactureYear()),
            type,
            backendCar.getColor(),
            backendCar.getFormattedPrice(),
            backendCar.getStatus().getValue(),
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
     * Update an existing car
     */
    public boolean updateCar(CarManagement.Car frontendCar) {
        try {
            Car backendCar = convertToBackendCar(frontendCar);
            ServiceResult<Car> result = carService.updateCar(backendCar, currentStaffId);
            
            if (result.isSuccess()) {
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
            return false;
        }
    }
    
    /**
     * Get all available cars (replaces CarStatusManager.getAvailableCars())
     */
    public List<CarManagement.Car> getAvailableCars() {
        try {
            CarDAO.CarFilter filter = new CarDAO.CarFilter();
            filter.setStatus(Car.CarStatus.AVAILABLE);
            
            ServiceResult<List<Car>> result = carService.getCars(filter);
            
            if (result.isSuccess()) {
                List<CarManagement.Car> frontendCars = new ArrayList<>();
                for (Car backendCar : result.getData()) {
                    frontendCars.add(convertToFrontendCar(backendCar));
                }
                return frontendCars;
            } else {
                System.err.println("Error getting available cars: " + result.getMessage());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error getting available cars: " + e.getMessage());
            return new ArrayList<>();
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
            return new ArrayList<>();
        }
    }
    
    /**
     * Sell a car
     */
    public boolean sellCar(int carId, String buyerName, String buyerContact, 
                          String salePrice, String paymentMethod) {
        try {
            // Create sale object
            Sale sale = new Sale();
            sale.setBuyerName(buyerName);
            sale.setBuyerContact(buyerContact);
            
            // Parse sale price
            String cleanPrice = salePrice.replace("$", "").replace(",", "");
            sale.setSalePrice(new BigDecimal(cleanPrice));
            
            // Set payment method
            sale.setPaymentMethod(Sale.PaymentMethod.fromString(paymentMethod.toLowerCase()));
            
            ServiceResult<Sale> result = carService.sellCar(carId, sale, currentStaffId);
            
            if (result.isSuccess()) {
                JOptionPane.showMessageDialog(null, 
                    "Car sold successfully!\n" + result.getMessage(), 
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
            default: return 1; // Default to sedan
        }
    }
    
    /**
     * Generate a temporary VIN if not provided
     */
    private String generateVIN() {
        return "TMP" + System.currentTimeMillis() % 100000000000000L;
    }
    
    /**
     * Update your existing CarManagement class to use this integration
     * Replace the CarStatusManager calls with this integration
     */
    public static void integrateWithCarManagement() {
        // Example of how to update your CarManagement.initializeSampleData() method:
        /*
        private void initializeSampleData() {
            CarManagementIntegration integration = CarManagementIntegration.getInstance();
            cars = integration.getAvailableCars(); // This now comes from database
        }
        */
    }
    
    /**
     * Integration method for AddCarUI
     */
    public boolean saveCarFromAddCarUI(String vin, String manufacturer, String model, 
                                     int year, String category, String color, 
                                     double salePrice, double costPrice, int mileage,
                                     String location, String engineType, String transmission,
                                     String fuelType, int horsepower, int seats,
                                     String fuelEconomy, String features, String notes,
                                     String imagePath) {
        try {
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
            
            // Set model ID based on category (simplified)
            backendCar.setModelId(getModelIdFromType(category));
            
            ServiceResult<Car> result = carService.addCar(backendCar, currentStaffId);
            
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
            return false;
        }
    }
}