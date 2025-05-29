package com.example.Design;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of CarStatusManager in case the original is not available
 * This can be used as a fallback to ensure the application compiles and runs
 */
public class CarStatusManager {
    private static CarStatusManager instance;
    private List<CarManagement.Car> allCars;
    private List<SoldCarRecord> soldCars;
    
    /**
     * Default constructor
     */
    public CarStatusManager() {
        allCars = new ArrayList<>();
        soldCars = new ArrayList<>();
        
        // Add some sample data
        initializeSampleData();
    }
    
    /**
     * Get the singleton instance
     */
    public static synchronized CarStatusManager getInstance() {
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
            if ("Sold".equalsIgnoreCase(car.getStatus())) {
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
            if ("Available".equalsIgnoreCase(car.getStatus()) || "Reserved".equalsIgnoreCase(car.getStatus())) {
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
     * Find car by ID
     */
    public CarManagement.Car findCarById(int carId) {
        for (CarManagement.Car car : allCars) {
            if (car.getId() == carId) {
                return car;
            }
        }
        return null;
    }
    
    /**
     * Sell a car
     */
    public boolean sellCar(int carId, String buyerName, String buyerContact, String salePrice, String paymentMethod) {
        CarManagement.Car carToSell = findCarById(carId);
        if (carToSell == null) {
            return false;
        }
        
        // Update car status
        carToSell.setStatus("Sold");
        
        // Create sold car record
        SoldCarRecord soldRecord = new SoldCarRecord(
            carToSell, 
            java.time.LocalDate.now().toString(), 
            salePrice, 
            buyerName, 
            buyerContact, 
            paymentMethod
        );
        
        soldCars.add(soldRecord);
        return true;
    }
    
    /**
     * Update car info
     */
    public boolean updateCar(CarManagement.Car updatedCar) {
        for (int i = 0; i < allCars.size(); i++) {
            if (allCars.get(i).getId() == updatedCar.getId()) {
                allCars.set(i, updatedCar);
                
                // Update in sold cars if needed
                for (SoldCarRecord soldCar : soldCars) {
                    if (soldCar.getCar().getId() == updatedCar.getId()) {
                        soldCar.setCar(updatedCar);
                    }
                }
                
                return true;
            }
        }
        return false;
    }
    
    /**
     * Add a new car
     */
    public void addCar(CarManagement.Car car) {
        // Ensure this car doesn't already exist
        for (int i = 0; i < allCars.size(); i++) {
            if (allCars.get(i).getId() == car.getId()) {
                // Update existing car instead
                allCars.set(i, car);
                return;
            }
        }
        
        // Add as new car
        allCars.add(car);
        
        // If already sold, add to sold cars
        if ("Sold".equalsIgnoreCase(car.getStatus())) {
            soldCars.add(new SoldCarRecord(car, car.getDateAdded(), car.getPrice(), "Unknown", "N/A", "Cash"));
        }
    }
    
    /**
     * Remove a car
     */
    public boolean removeCar(int carId) {
        boolean removed = allCars.removeIf(car -> car.getId() == carId);
        soldCars.removeIf(soldCar -> soldCar.getCar().getId() == carId);
        return removed;
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