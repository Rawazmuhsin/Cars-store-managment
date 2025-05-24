package com.example.OOP.backend;


import com.example.OOP.backend.CarDAO;
import com.example.OOP.backend.SalesDAO;
import com.example.OOP.backend.AuditLogDAO;
import com.example.OOP.backend.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Car Service - Business logic layer for car operations
 * Handles validation, business rules, and coordination between DAOs
 */
public class CarService {
    
    private CarDAO carDAO;
    private SalesDAO salesDAO;
    private AuditLogDAO auditLogDAO;
    
    public CarService() {
        this.carDAO = new CarDAO();
        this.salesDAO = new SalesDAO();
        this.auditLogDAO = new AuditLogDAO();
    }
    
    /**
     * Add new car to inventory
     */
    public ServiceResult<Car> addCar(Car car, int staffId) {
        try {
            // Validate car data
            ServiceResult<Car> validationResult = validateCar(car);
            if (!validationResult.isSuccess()) {
                return validationResult;
            }
            
            // Check if VIN already exists
            if (carDAO.vinExists(car.getVin())) {
                return ServiceResult.failure("A car with VIN '" + car.getVin() + "' already exists in the system.");
            }
            
            // Set default values
            if (car.getStatus() == null) {
                car.setStatus(Car.CarStatus.AVAILABLE);
            }
            if (car.getMileage() == 0) {
                car.setMileage(0);
            }
            if (car.getLocation() == null || car.getLocation().isEmpty()) {
                car.setLocation("Main Lot");
            }
            
            car.setAddedBy(staffId);
            
            // Save to database
            boolean success = carDAO.createCar(car);
            
            if (success) {
                // Log the action
                auditLogDAO.logAction(staffId, "Car Added", 
                    "Added new car: " + car.getDisplayName() + " (VIN: " + car.getVin() + ")", 
                    "success");
                
                return ServiceResult.success(car, "Car added successfully to inventory.");
            } else {
                return ServiceResult.failure("Failed to add car to database.");
            }
            
        } catch (Exception e) {
            System.err.println("Error in addCar: " + e.getMessage());
            return ServiceResult.failure("An error occurred while adding the car: " + e.getMessage());
        }
    }
    
    /**
     * Update existing car
     */
    public ServiceResult<Car> updateCar(Car car, int staffId) {
        try {
            // Validate car data
            ServiceResult<Car> validationResult = validateCar(car);
            if (!validationResult.isSuccess()) {
                return validationResult;
            }
            
            // Get existing car to check VIN conflicts
            Car existingCar = carDAO.getCarById(car.getCarId());
            if (existingCar == null) {
                return ServiceResult.failure("Car not found with ID: " + car.getCarId());
            }
            
            // Check VIN conflict with other cars
            if (!existingCar.getVin().equals(car.getVin()) && carDAO.vinExists(car.getVin())) {
                return ServiceResult.failure("A car with VIN '" + car.getVin() + "' already exists in the system.");
            }
            
            // Update in database
            boolean success = carDAO.updateCar(car);
            
            if (success) {
                // Log the action
                auditLogDAO.logAction(staffId, "Car Updated", 
                    "Updated car: " + car.getDisplayName() + " (ID: " + car.getCarId() + ")", 
                    "success");
                
                return ServiceResult.success(car, "Car updated successfully.");
            } else {
                return ServiceResult.failure("Failed to update car in database.");
            }
            
        } catch (Exception e) {
            System.err.println("Error in updateCar: " + e.getMessage());
            return ServiceResult.failure("An error occurred while updating the car: " + e.getMessage());
        }
    }
    
    /**
     * Sell a car - creates sale record and updates car status
     */
    public ServiceResult<Sale> sellCar(int carId, Sale saleInfo, int staffId) {
        try {
            // Get the car
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                return ServiceResult.failure("Car not found with ID: " + carId);
            }
            
            // Check if car is available for sale
            if (car.getStatus() != Car.CarStatus.AVAILABLE && car.getStatus() != Car.CarStatus.RESERVED) {
                return ServiceResult.failure("Car is not available for sale. Current status: " + car.getStatus());
            }
            
            // Validate sale information
            if (saleInfo.getBuyerName() == null || saleInfo.getBuyerName().trim().isEmpty()) {
                return ServiceResult.failure("Buyer name is required.");
            }
            
            if (saleInfo.getBuyerContact() == null || saleInfo.getBuyerContact().trim().isEmpty()) {
                return ServiceResult.failure("Buyer contact is required.");
            }
            
            if (saleInfo.getSalePrice() == null || saleInfo.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
                return ServiceResult.failure("Valid sale price is required.");
            }
            
            // Set sale details
            saleInfo.setCarId(carId);
            saleInfo.setHandledBy(staffId);
            
            // Calculate tax and total (assuming 8% tax rate)
            BigDecimal taxRate = new BigDecimal("0.08");
            BigDecimal taxAmount = saleInfo.getSalePrice().multiply(taxRate);
            BigDecimal totalAmount = saleInfo.getSalePrice().add(taxAmount);
            
            saleInfo.setTaxAmount(taxAmount);
            saleInfo.setTotalAmount(totalAmount);
            
            if (saleInfo.getPaymentStatus() == null) {
                saleInfo.setPaymentStatus(Sale.PaymentStatus.COMPLETED);
            }
            
            // Create sale record
            boolean saleCreated = salesDAO.createSale(saleInfo);
            if (!saleCreated) {
                return ServiceResult.failure("Failed to create sale record.");
            }
            
            // Update car status to sold
            boolean carUpdated = carDAO.updateCarStatus(carId, Car.CarStatus.SOLD);
            if (!carUpdated) {
                // If car update fails, we should ideally rollback the sale, but for simplicity we'll log the error
                System.err.println("Warning: Sale created but failed to update car status for car ID: " + carId);
            }
            
            // Log the action
            auditLogDAO.logAction(staffId, "Car Sold", 
                "Sold car: " + car.getDisplayName() + " to " + saleInfo.getBuyerName() + 
                " for " + saleInfo.getSalePrice(), 
                "success");
            
            return ServiceResult.success(saleInfo, "Car sold successfully!");
            
        } catch (Exception e) {
            System.err.println("Error in sellCar: " + e.getMessage());
            return ServiceResult.failure("An error occurred while processing the sale: " + e.getMessage());
        }
    }
    
    /**
     * Reserve a car
     */
    public ServiceResult<Car> reserveCar(int carId, int staffId) {
        try {
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                return ServiceResult.failure("Car not found with ID: " + carId);
            }
            
            if (car.getStatus() != Car.CarStatus.AVAILABLE) {
                return ServiceResult.failure("Car is not available for reservation. Current status: " + car.getStatus());
            }
            
            boolean success = carDAO.updateCarStatus(carId, Car.CarStatus.RESERVED);
            
            if (success) {
                car.setStatus(Car.CarStatus.RESERVED);
                
                // Log the action
                auditLogDAO.logAction(staffId, "Car Reserved", 
                    "Reserved car: " + car.getDisplayName() + " (ID: " + carId + ")", 
                    "success");
                
                return ServiceResult.success(car, "Car reserved successfully.");
            } else {
                return ServiceResult.failure("Failed to reserve car.");
            }
            
        } catch (Exception e) {
            System.err.println("Error in reserveCar: " + e.getMessage());
            return ServiceResult.failure("An error occurred while reserving the car: " + e.getMessage());
        }
    }
    
    /**
     * Release car reservation (make it available again)
     */
    public ServiceResult<Car> releaseReservation(int carId, int staffId) {
        try {
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                return ServiceResult.failure("Car not found with ID: " + carId);
            }
            
            if (car.getStatus() != Car.CarStatus.RESERVED) {
                return ServiceResult.failure("Car is not currently reserved. Current status: " + car.getStatus());
            }
            
            boolean success = carDAO.updateCarStatus(carId, Car.CarStatus.AVAILABLE);
            
            if (success) {
                car.setStatus(Car.CarStatus.AVAILABLE);
                
                // Log the action
                auditLogDAO.logAction(staffId, "Reservation Released", 
                    "Released reservation for car: " + car.getDisplayName() + " (ID: " + carId + ")", 
                    "success");
                
                return ServiceResult.success(car, "Car reservation released successfully.");
            } else {
                return ServiceResult.failure("Failed to release car reservation.");
            }
            
        } catch (Exception e) {
            System.err.println("Error in releaseReservation: " + e.getMessage());
            return ServiceResult.failure("An error occurred while releasing the reservation: " + e.getMessage());
        }
    }
    
    /**
     * Delete car from inventory
     */
    public ServiceResult<Boolean> deleteCar(int carId, int staffId) {
        try {
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                return ServiceResult.failure("Car not found with ID: " + carId);
            }
            
            // Don't allow deletion of sold cars (business rule)
            if (car.getStatus() == Car.CarStatus.SOLD) {
                return ServiceResult.failure("Cannot delete sold cars. Car status: " + car.getStatus());
            }
            
            // Check if car has any sales records
            List<Sale> carSales = salesDAO.getSalesByCarId(carId);
            if (!carSales.isEmpty()) {
                return ServiceResult.failure("Cannot delete car with existing sales records.");
            }
            
            boolean success = carDAO.deleteCar(carId);
            
            if (success) {
                // Log the action
                auditLogDAO.logAction(staffId, "Car Deleted", 
                    "Deleted car: " + car.getDisplayName() + " (ID: " + carId + ")", 
                    "success");
                
                return ServiceResult.success(true, "Car deleted successfully.");
            } else {
                return ServiceResult.failure("Failed to delete car from database.");
            }
            
        } catch (Exception e) {
            System.err.println("Error in deleteCar: " + e.getMessage());
            return ServiceResult.failure("An error occurred while deleting the car: " + e.getMessage());
        }
    }
    
    /**
     * Get car by ID
     */
    public ServiceResult<Car> getCarById(int carId) {
        try {
            Car car = carDAO.getCarById(carId);
            if (car != null) {
                return ServiceResult.success(car);
            } else {
                return ServiceResult.failure("Car not found with ID: " + carId);
            }
        } catch (Exception e) {
            System.err.println("Error in getCarById: " + e.getMessage());
            return ServiceResult.failure("An error occurred while retrieving the car: " + e.getMessage());
        }
    }
    
    /**
     * Get all cars with filtering
     */
    public ServiceResult<List<Car>> getCars(CarDAO.CarFilter filter) {
        try {
            List<Car> cars = carDAO.getCars(filter);
            return ServiceResult.success(cars);
        } catch (Exception e) {
            System.err.println("Error in getCars: " + e.getMessage());
            return ServiceResult.failure("An error occurred while retrieving cars: " + e.getMessage());
        }
    }
    
    /**
     * Search cars
     */
    public ServiceResult<List<Car>> searchCars(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getCars(null); // Return all cars if no keyword
            }
            
            List<Car> cars = carDAO.searchCars(keyword.trim());
            return ServiceResult.success(cars);
        } catch (Exception e) {
            System.err.println("Error in searchCars: " + e.getMessage());
            return ServiceResult.failure("An error occurred while searching cars: " + e.getMessage());
        }
    }
    
    /**
     * Get inventory statistics
     */
    public InventoryStats getInventoryStats() {
        try {
            InventoryStats stats = new InventoryStats();
            stats.setTotalCars(carDAO.getTotalCarCount());
            stats.setAvailableCars(carDAO.getCarCountByStatus(Car.CarStatus.AVAILABLE));
            stats.setSoldCars(carDAO.getCarCountByStatus(Car.CarStatus.SOLD));
            stats.setReservedCars(carDAO.getCarCountByStatus(Car.CarStatus.RESERVED));
            stats.setMaintenanceCars(carDAO.getCarCountByStatus(Car.CarStatus.MAINTENANCE));
            
            // Get recent cars (last 30 days)
            List<Car> recentCars = carDAO.getRecentCars(30);
            stats.setRecentCars(recentCars.size());
            
            return stats;
        } catch (Exception e) {
            System.err.println("Error in getInventoryStats: " + e.getMessage());
            return new InventoryStats(); // Return empty stats
        }
    }
    
    /**
     * Validate car data
     */
    private ServiceResult<Car> validateCar(Car car) {
        if (car == null) {
            return ServiceResult.failure("Car data is required.");
        }
        
        if (car.getVin() == null || car.getVin().trim().isEmpty()) {
            return ServiceResult.failure("VIN is required.");
        }
        
        if (car.getVin().length() != 17) {
            return ServiceResult.failure("VIN must be exactly 17 characters.");
        }
        
        if (car.getModelId() <= 0) {
            return ServiceResult.failure("Valid car model is required.");
        }
        
        if (car.getColor() == null || car.getColor().trim().isEmpty()) {
            return ServiceResult.failure("Color is required.");
        }
        
        if (car.getManufactureYear() < 1990 || car.getManufactureYear() > 2030) {
            return ServiceResult.failure("Manufacture year must be between 1990 and 2030.");
        }
        
        if (car.getPrice() == null || car.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return ServiceResult.failure("Valid price is required.");
        }
        
        if (car.getCost() == null || car.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            return ServiceResult.failure("Valid cost is required.");
        }
        
        if (car.getMileage() < 0) {
            return ServiceResult.failure("Mileage cannot be negative.");
        }
        
        return ServiceResult.success(car);
    }
    
    /**
     * Inventory statistics class
     */
    public static class InventoryStats {
        private int totalCars;
        private int availableCars;
        private int soldCars;
        private int reservedCars;
        private int maintenanceCars;
        private int recentCars;
        
        // Getters and Setters
        public int getTotalCars() { return totalCars; }
        public void setTotalCars(int totalCars) { this.totalCars = totalCars; }
        
        public int getAvailableCars() { return availableCars; }
        public void setAvailableCars(int availableCars) { this.availableCars = availableCars; }
        
        public int getSoldCars() { return soldCars; }
        public void setSoldCars(int soldCars) { this.soldCars = soldCars; }
        
        public int getReservedCars() { return reservedCars; }
        public void setReservedCars(int reservedCars) { this.reservedCars = reservedCars; }
        
        public int getMaintenanceCars() { return maintenanceCars; }
        public void setMaintenanceCars(int maintenanceCars) { this.maintenanceCars = maintenanceCars; }
        
        public int getRecentCars() { return recentCars; }
        public void setRecentCars(int recentCars) { this.recentCars = recentCars; }
    }
}
