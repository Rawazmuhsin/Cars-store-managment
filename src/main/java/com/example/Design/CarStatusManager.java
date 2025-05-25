package com.example.Design;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Car Status Manager handles all car status changes and movements between different sections
 */
public class CarStatusManager {
    private static CarStatusManager instance;
    private List<CarManagement.Car> allCars;
    private List<SoldCarRecord> soldCars;
    
    private CarStatusManager() {
        allCars = new ArrayList<>();
        soldCars = new ArrayList<>();
        initializeSampleData();
    }
    
    public static CarStatusManager getInstance() {
        if (instance == null) {
            instance = new CarStatusManager();
        }
        return instance;
    }
    
    /**
     * Initialize with sample data
     */
    private void initializeSampleData() {
        // Sample cars - same as in CarManagement
        allCars.add(new CarManagement.Car(1, "Toyota Camry", "2023", "Sedan", "Silver", "$28,500", "Available", "May 15, 2025", "placeholder_toyota.jpg"));
        allCars.add(new CarManagement.Car(2, "Honda CR-V", "2023", "SUV", "White", "$32,000", "Available", "May 18, 2025", "placeholder_honda.jpg"));
        allCars.add(new CarManagement.Car(3, "Ford F-150", "2022", "Truck", "Black", "$45,000", "Available", "May 20, 2025", "placeholder_ford.jpg"));
        allCars.add(new CarManagement.Car(4, "BMW X5", "2023", "SUV", "Blue", "$62,000", "Sold", "May 10, 2025", "placeholder_bmw.jpg"));
        allCars.add(new CarManagement.Car(5, "Chevrolet Malibu", "2022", "Sedan", "Red", "$26,000", "Reserved", "May 22, 2025", "placeholder_chevrolet.jpg"));
        allCars.add(new CarManagement.Car(6, "Tesla Model 3", "2023", "Electric", "Black", "$47,000", "Available", "May 12, 2025", "placeholder_tesla.jpg"));
        allCars.add(new CarManagement.Car(7, "Jeep Wrangler", "2023", "SUV", "Green", "$38,000", "Available", "May 25, 2025", "placeholder_jeep.jpg"));
        allCars.add(new CarManagement.Car(8, "Nissan Altima", "2022", "Sedan", "Gray", "$25,000", "Available", "May 14, 2025", "placeholder_nissan.jpg"));
        allCars.add(new CarManagement.Car(9, "Audi Q7", "2023", "SUV", "Black", "$58,000", "Available", "May 16, 2025", "placeholder_audi.jpg"));
        allCars.add(new CarManagement.Car(10, "Kia Sorento", "2022", "SUV", "White", "$33,000", "Sold", "May 11, 2025", "placeholder_kia.jpg"));
        
        // Initialize sold cars from cars that are already marked as sold
        for (CarManagement.Car car : allCars) {
            if ("Sold".equals(car.getStatus())) {
                soldCars.add(new SoldCarRecord(car, "May 23, 2025", car.getPrice(), "John Doe", "555-0123", "Cash"));
            }
        }
    }
    
    /**
     * Get all cars
     */
    public List<CarManagement.Car> getAllCars() {
        return new ArrayList<>(allCars);
    }
    
    /**
     * Get available cars only (Available and Reserved)
     */
    public List<CarManagement.Car> getAvailableCars() {
        List<CarManagement.Car> availableCars = new ArrayList<>();
        for (CarManagement.Car car : allCars) {
            if ("Available".equals(car.getStatus()) || "Reserved".equals(car.getStatus())) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }
    
    /**
     * Get sold cars
     */
    public List<SoldCarRecord> getSoldCars() {
        return new ArrayList<>(soldCars);
    }
    
    /**
     * Sell a car - change status and create sold record
     */
    public boolean sellCar(int carId, String buyerName, String buyerContact, String salePrice, String paymentMethod) {
        for (CarManagement.Car car : allCars) {
            if (car.getId() == carId) {
                // Check if car is available for sale
                if (!car.getStatus().equals("Available") && !car.getStatus().equals("Reserved")) {
                    System.err.println("Error: Car is not available for sale. Current status: " + car.getStatus());
                    return false;
                }
                
                // Update car status
                car.setStatus("Sold");
                
                // Create sold car record
                String saleDate = new SimpleDateFormat("MMM dd, yyyy").format(new Date());
                SoldCarRecord soldRecord = new SoldCarRecord(car, saleDate, salePrice, buyerName, buyerContact, paymentMethod);
                soldCars.add(soldRecord);
                
                System.out.println("Car sold successfully: " + car.getModel() + " to " + buyerName + " for " + salePrice);
                
                return true;
            }
        }
        
        System.err.println("Error: Car not found with ID: " + carId);
        return false;
    }
    
    /**
     * Update car status
     */
    public boolean updateCarStatus(int carId, String newStatus) {
        for (CarManagement.Car car : allCars) {
            if (car.getId() == carId) {
                String oldStatus = car.getStatus();
                car.setStatus(newStatus);
                
                // If changing from sold to available, remove from sold cars
                if ("Sold".equals(oldStatus) && !"Sold".equals(newStatus)) {
                    soldCars.removeIf(soldCar -> soldCar.getCar().getId() == carId);
                }
                
                System.out.println("Car status updated successfully: " + car.getModel() + " - " + newStatus);
                return true;
            }
        }
        
        System.err.println("Error: Car not found with ID: " + carId);
        return false;
    }
    
    /**
     * Add a new car
     */
    public void addCar(CarManagement.Car car) {
        // Make sure this car doesn't already exist
        for (CarManagement.Car existingCar : allCars) {
            if (existingCar.getId() == car.getId()) {
                // Update existing car instead of adding
                existingCar.setModel(car.getModel());
                existingCar.setYear(car.getYear());
                existingCar.setType(car.getType());
                existingCar.setColor(car.getColor());
                existingCar.setPrice(car.getPrice());
                existingCar.setStatus(car.getStatus());
                existingCar.setDateAdded(car.getDateAdded());
                existingCar.setImagePath(car.getImagePath());
                System.out.println("Updated existing car: " + car.getModel());
                return;
            }
        }
        
        // Otherwise add as new car
        allCars.add(car);
        System.out.println("Added new car: " + car.getModel());
        
        // If car is already sold, add it to sold cars
        if ("Sold".equals(car.getStatus())) {
            soldCars.add(new SoldCarRecord(car, car.getDateAdded(), car.getPrice(), "Unknown Buyer", "N/A", "Cash"));
        }
    }
    
    /**
     * Remove a car completely
     */
    public boolean removeCar(int carId) {
        boolean removed = allCars.removeIf(car -> car.getId() == carId);
        soldCars.removeIf(soldCar -> soldCar.getCar().getId() == carId);
        
        if (removed) {
            System.out.println("Car removed successfully: ID " + carId);
        } else {
            System.err.println("Error: Car not found with ID: " + carId);
        }
        
        return removed;
    }
    
    /**
     * Find car by ID
     */
    public CarManagement.Car findCarById(int carId) {
        for (CarManagement.Car car : allCars) {
            if (car.getId() == carId) {
                return car;
            }
        }
        System.err.println("Error: Car not found with ID: " + carId);
        return null;
    }
    
    /**
     * Update an existing car's details
     */
    public boolean updateCar(CarManagement.Car updatedCar) {
        for (int i = 0; i < allCars.size(); i++) {
            CarManagement.Car car = allCars.get(i);
            if (car.getId() == updatedCar.getId()) {
                // Update car details
                car.setModel(updatedCar.getModel());
                car.setYear(updatedCar.getYear());
                car.setType(updatedCar.getType());
                car.setColor(updatedCar.getColor());
                car.setPrice(updatedCar.getPrice());
                car.setStatus(updatedCar.getStatus());
                car.setDateAdded(updatedCar.getDateAdded());
                car.setImagePath(updatedCar.getImagePath());
                
                // If status changed to sold, update sold cars list
                if ("Sold".equals(updatedCar.getStatus()) && !"Sold".equals(car.getStatus())) {
                    // Add to sold cars if not already there
                    boolean alreadySold = false;
                    for (SoldCarRecord soldCar : soldCars) {
                        if (soldCar.getCar().getId() == car.getId()) {
                            alreadySold = true;
                            break;
                        }
                    }
                    
                    if (!alreadySold) {
                        soldCars.add(new SoldCarRecord(car, new SimpleDateFormat("MMM dd, yyyy").format(new Date()), 
                                                      car.getPrice(), "Unknown Buyer", "N/A", "Cash"));
                    }
                }
                
                // If status changed from sold, update sold cars list
                if (!"Sold".equals(updatedCar.getStatus()) && "Sold".equals(car.getStatus())) {
                    soldCars.removeIf(soldCar -> soldCar.getCar().getId() == car.getId());
                }
                
                // Also update the car in any sold car records
                for (SoldCarRecord soldCar : soldCars) {
                    if (soldCar.getCar().getId() == car.getId()) {
                        soldCar.setCar(car);
                    }
                }
                
                System.out.println("Car updated successfully: " + car.getModel());
                return true;
            }
        }
        
        System.err.println("Error: Car not found with ID: " + updatedCar.getId());
        return false;
    }
    
    /**
     * Synchronize with database (to be implemented when database is ready)
     */
    public void syncWithDatabase() {
        // This will be implemented to sync in-memory state with database
        System.out.println("Synchronizing with database...");
    }
    
    /**
     * Get cars by status
     */
    public List<CarManagement.Car> getCarsByStatus(String status) {
        List<CarManagement.Car> filteredCars = new ArrayList<>();
        for (CarManagement.Car car : allCars) {
            if (status.equals(car.getStatus())) {
                filteredCars.add(car);
            }
        }
        return filteredCars;
    }
    
    /**
     * Inner class for sold car records
     */
    public static class SoldCarRecord {
        private CarManagement.Car car;
        private String saleDate;
        private String salePrice;
        private String buyerName;
        private String buyerContact;
        private String paymentMethod;
        private String notes;
        
        public SoldCarRecord(CarManagement.Car car, String saleDate, String salePrice, 
                           String buyerName, String buyerContact, String paymentMethod) {
            this.car = car;
            this.saleDate = saleDate;
            this.salePrice = salePrice;
            this.buyerName = buyerName;
            this.buyerContact = buyerContact;
            this.paymentMethod = paymentMethod;
            this.notes = "Vehicle sold and delivered successfully.";
        }
        
        // Getters and setters
        public CarManagement.Car getCar() { return car; }
        public String getSaleDate() { return saleDate; }
        public String getSalePrice() { return salePrice; }
        public String getBuyerName() { return buyerName; }
        public String getBuyerContact() { return buyerContact; }
        public String getPaymentMethod() { return paymentMethod; }
        public String getNotes() { return notes; }
        
        public void setCar(CarManagement.Car car) { this.car = car; }
        public void setSaleDate(String saleDate) { this.saleDate = saleDate; }
        public void setSalePrice(String salePrice) { this.salePrice = salePrice; }
        public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
        public void setBuyerContact(String buyerContact) { this.buyerContact = buyerContact; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}