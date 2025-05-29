package com.example.Design;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

/**
 * Integration class for Car Management to connect with backend
 * This class serves as a bridge between the UI and the backend
 */
public class CarManagementIntegration {
    
    private static CarManagementIntegration instance;
    private static final Object lock = new Object();
    
    private CarStatusManager carManager;
    private int currentStaffId = 1; // Default admin ID
    
    /**
     * Private constructor for singleton pattern
     */
    private CarManagementIntegration() {
        try {
            this.carManager = CarStatusManager.getInstance();
            System.out.println("CarManagementIntegration initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing CarManagementIntegration: " + e.getMessage());
            e.printStackTrace();
            // Initialize with empty manager to avoid null pointer exceptions
            this.carManager = new CarStatusManager();
        }
    }
    
    /**
     * Get the singleton instance with thread safety
     */
    public static CarManagementIntegration getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new CarManagementIntegration();
                }
            }
        }
        return instance;
    }
    
    /**
     * Set the current staff ID for logging actions
     */
    public void setCurrentStaffId(int staffId) {
        this.currentStaffId = staffId;
        System.out.println("Current staff ID set to: " + staffId);
    }
    
    /**
     * Get current staff ID
     */
    public int getCurrentStaffId() {
        return this.currentStaffId;
    }
    
    /**
     * Get all cars
     */
    public List<CarManagement.Car> getAllCars() {
        try {
            List<CarManagement.Car> cars = carManager.getAllCars();
            System.out.println("Retrieved " + cars.size() + " cars from manager");
            return cars;
        } catch (Exception e) {
            System.err.println("Error getting all cars: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get available cars
     */
    public List<CarManagement.Car> getAvailableCars() {
        try {
            List<CarManagement.Car> availableCars = carManager.getAvailableCars();
            System.out.println("Retrieved " + availableCars.size() + " available cars from manager");
            return availableCars;
        } catch (Exception e) {
            System.err.println("Error getting available cars: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get sold cars
     */
    public List<CarStatusManager.SoldCarRecord> getSoldCars() {
        try {
            List<CarStatusManager.SoldCarRecord> soldCars = carManager.getSoldCars();
            System.out.println("Retrieved " + soldCars.size() + " sold cars from manager");
            return soldCars;
        } catch (Exception e) {
            System.err.println("Error getting sold cars: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get cars by status
     */
    public List<CarManagement.Car> getCarsByStatus(String status) {
        try {
            List<CarManagement.Car> cars = carManager.getAllCars();
            List<CarManagement.Car> filteredCars = new ArrayList<>();
            
            // Convert from backend model to UI model
            for (CarManagement.Car car : cars) {
                if (status.equalsIgnoreCase(car.getStatus())) {
                    filteredCars.add(car);
                }
            }
            
            System.out.println("Retrieved " + filteredCars.size() + " cars with status '" + status + "'");
            return filteredCars;
        } catch (Exception e) {
            System.err.println("Error getting cars by status: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
 * Get car by ID with enhanced error handling
 */
public CarManagement.Car getCarById(int id) {
    try {
        if (carManager == null) {
            System.err.println("ERROR: Car manager is null in getCarById");
            
            // Fallback: Try to reinitialize car manager
            carManager = CarStatusManager.getInstance();
            if (carManager == null) {
                System.err.println("CRITICAL: Unable to initialize car manager");
                return null;
            }
        }
        
        System.out.println("Searching for car with ID: " + id);
        CarManagement.Car car = carManager.findCarById(id);
        
        if (car != null) {
            System.out.println("Found car by ID " + id + ": " + car.getModel());
        } else {
            System.err.println("Car with ID " + id + " not found");
            
            // Try again with re-initialized car manager
            carManager = CarStatusManager.getInstance();
            car = carManager.findCarById(id);
            
            if (car != null) {
                System.out.println("Found car after reinitializing car manager: " + car.getModel());
            }
        }
        return car;
    } catch (Exception e) {
        System.err.println("Error getting car by ID: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
    /**
     * Save a new car from AddCarUI
     */
    public boolean saveCarFromAddCarUI(
            String vin, String manufacturer, String model, Integer year, String category, String color,
            Double salePrice, Double costPrice, Integer mileage, String location, String engineType,
            String transmission, String fuelType, Integer horsepower, Integer seats,
            String fuelEconomy, String features, String notes, String imagePath) {
        
        try {
            // Get a new unique ID
            List<CarManagement.Car> allCars = carManager.getAllCars();
            int maxId = 0;
            for (CarManagement.Car car : allCars) {
                if (car.getId() > maxId) {
                    maxId = car.getId();
                }
            }
            int newId = maxId + 1;
            
            // Format price for display
            String formattedPrice = String.format("$%,.0f", salePrice);
            
            // Create a new Car object
            CarManagement.Car newCar = new CarManagement.Car(
                newId,
                model, // Model name
                String.valueOf(year), // Year as string
                category, // Type/category
                color,
                formattedPrice, // Formatted price
                "Available", // Initial status
                java.time.LocalDate.now().toString(), // Current date
                imagePath.isEmpty() ? "placeholder_car.jpg" : imagePath // Image path
            );
            
            // Add to car manager
            carManager.addCar(newCar);
            
            System.out.println("Car saved successfully: " + model + " (ID: " + newId + ")");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error saving car: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, 
                "Error saving car: " + e.getMessage(), 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
            
            return false;
        }
    }
 /**
 * Update an existing car with enhanced error handling
 */
public boolean updateCar(CarManagement.Car car) {
    try {
        if (car == null) {
            System.err.println("ERROR: Car object is null in updateCar");
            return false;
        }
        
        System.out.println("Attempting to update car ID: " + car.getId() + ", Model: " + car.getModel());
        
        // First check if the car exists
        CarManagement.Car existingCar = getCarById(car.getId());
        if (existingCar == null) {
            System.err.println("WARNING: Car with ID " + car.getId() + " not found for update");
            
            // Add as new car instead
            System.out.println("Adding as new car since existing car not found");
            carManager.addCar(car);
            
            System.out.println("Car added successfully: " + car.getModel() + " (ID: " + car.getId() + ")");
            return true;
        }
        
        // Print debug info
        System.out.println("Existing car found: " + existingCar.getModel() + ", Status: " + existingCar.getStatus());
        System.out.println("Updating to: " + car.getModel() + ", Status: " + car.getStatus());
        
        // Try to update
        boolean success = carManager.updateCar(car);
        
        if (success) {
            System.out.println("Car updated successfully: " + car.getModel() + " (ID: " + car.getId() + ")");
        } else {
            System.err.println("Failed to update car normally, trying force update...");
            
            // Try force update by removing and re-adding
            carManager.removeCar(car.getId());
            carManager.addCar(car);
            
            System.out.println("Force update completed for car: " + car.getModel() + " (ID: " + car.getId() + ")");
            return true;
        }
        
        return success;
        
    } catch (Exception e) {
        System.err.println("Error updating car: " + e.getMessage());
        e.printStackTrace();
        
        // Try one last approach - remove and add again
        try {
            System.out.println("Attempting emergency recovery...");
            carManager.removeCar(car.getId());
            carManager.addCar(car);
            System.out.println("Emergency recovery successful");
            return true;
        } catch (Exception ex) {
            System.err.println("Emergency recovery failed: " + ex.getMessage());
        }
        
        JOptionPane.showMessageDialog(null, 
            "Error updating car: " + e.getMessage(), 
            "Update Error", 
            JOptionPane.ERROR_MESSAGE);
        
        return false;
    }
}
    
    /**
     * Delete a car
     */
    public boolean deleteCar(int carId) {
        try {
            // Delete from car manager
            boolean success = carManager.removeCar(carId);
            
            if (success) {
                System.out.println("Car deleted successfully: ID " + carId);
            } else {
                System.err.println("Failed to delete car: ID " + carId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error deleting car: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, 
                "Error deleting car: " + e.getMessage(), 
                "Delete Error", 
                JOptionPane.ERROR_MESSAGE);
            
            return false;
        }
    }
    
    /**
     * Sell a car
     */
    public boolean sellCar(int carId, String buyerName, String buyerContact, String salePrice, String paymentMethod) {
        try {
            // Sell the car using car manager
            boolean success = carManager.sellCar(carId, buyerName, buyerContact, salePrice, paymentMethod);
            
            if (success) {
                System.out.println("Car sold successfully: ID " + carId + " to " + buyerName);
            } else {
                System.err.println("Failed to sell car: ID " + carId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error selling car: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, 
                "Error selling car: " + e.getMessage(), 
                "Sale Error", 
                JOptionPane.ERROR_MESSAGE);
            
            return false;
        }
    }
    
    /**
     * Get sale statistics
     */
    public SalesStatistics getSalesStatistics() {
        SalesStatistics stats = new SalesStatistics();
        
        try {
            List<CarManagement.Car> allCars = carManager.getAllCars();
            List<CarStatusManager.SoldCarRecord> soldCars = carManager.getSoldCars();
            
            // Calculate statistics
            stats.totalCarCount = allCars.size();
            stats.availableCarCount = 0;
            stats.soldCarCount = soldCars.size();
            stats.reservedCarCount = 0;
            
            double totalSalesValue = 0.0;
            
            // Count cars by status
            for (CarManagement.Car car : allCars) {
                if ("Available".equalsIgnoreCase(car.getStatus())) {
                    stats.availableCarCount++;
                } else if ("Reserved".equalsIgnoreCase(car.getStatus())) {
                    stats.reservedCarCount++;
                }
            }
            
            // Calculate sales value and average price
            for (CarStatusManager.SoldCarRecord soldCar : soldCars) {
                try {
                    String priceStr = soldCar.getSalePrice().replace("$", "").replace(",", "");
                    double price = Double.parseDouble(priceStr);
                    totalSalesValue += price;
                } catch (Exception e) {
                    // Skip invalid prices
                }
            }
            
            stats.totalSalesValue = totalSalesValue;
            stats.averageSalePrice = soldCars.size() > 0 ? totalSalesValue / soldCars.size() : 0;
            
            System.out.println("Retrieved sales statistics: " + stats);
            
            return stats;
            
        } catch (Exception e) {
            System.err.println("Error getting sales statistics: " + e.getMessage());
            e.printStackTrace();
            return stats; // Return default stats
        }
    }
    
    /**
     * Get filtered sales data
     */
    public List<Object[]> getFilteredSoldCarsData(Object filter) {
        try {
            // This is a simplified implementation - in a real system, you would apply the filter
            List<Object[]> filteredData = new ArrayList<>();
            List<CarStatusManager.SoldCarRecord> soldCars = carManager.getSoldCars();
            
            for (CarStatusManager.SoldCarRecord soldCar : soldCars) {
                CarManagement.Car car = soldCar.getCar();
                
                if (car != null) {
                    Object[] rowData = new Object[] {
                        car.getModel(),
                        car.getYear(),
                        car.getType(),
                        soldCar.getSaleDate(),
                        soldCar.getSalePrice(),
                        soldCar.getBuyerName(),
                        "View",
                        car.getId()
                    };
                    
                    filteredData.add(rowData);
                }
            }
            
            System.out.println("Retrieved " + filteredData.size() + " filtered sold cars");
            return filteredData;
            
        } catch (Exception e) {
            System.err.println("Error getting filtered sold cars data: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Statistics class for sales data
     */
    public static class SalesStatistics {
        public int totalCarCount = 0;
        public int availableCarCount = 0;
        public int soldCarCount = 0;
        public int reservedCarCount = 0;
        public double totalSalesValue = 0.0;
        public double averageSalePrice = 0.0;
        
        @Override
        public String toString() {
            return "SalesStatistics{" +
                   "totalCarCount=" + totalCarCount +
                   ", availableCarCount=" + availableCarCount +
                   ", soldCarCount=" + soldCarCount +
                   ", reservedCarCount=" + reservedCarCount +
                   ", totalSalesValue=" + totalSalesValue +
                   ", averageSalePrice=" + averageSalePrice +
                   '}';
        }
    }
}