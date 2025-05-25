package com.example.Design;

import com.example.OOP.backend.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

/**
 * Integration class for SoldCarsUI to connect with backend
 */
public class SoldCarsIntegration {
    
    private static SoldCarsIntegration instance;
    private SalesDAO salesDAO;
    private CarDAO carDAO;
    private AuditLogDAO auditLogDAO;
    private int currentStaffId = 1;
    
    private SoldCarsIntegration() {
        this.salesDAO = new SalesDAO();
        this.carDAO = new CarDAO();
        this.auditLogDAO = new AuditLogDAO();
    }
    
    public static SoldCarsIntegration getInstance() {
        if (instance == null) {
            instance = new SoldCarsIntegration();
        }
        return instance;
    }
    
    public void setCurrentStaffId(int staffId) {
        this.currentStaffId = staffId;
    }
    
    /**
     * Get all sold cars data for display in SoldCarsUI
     */
    public List<Object[]> getSoldCarsData() {
        List<Object[]> soldCarsData = new ArrayList<>();
        
        try {
            System.out.println("Fetching sold cars data from database...");
            
            // First try to get sold cars from database
            CarDAO.CarFilter filter = new CarDAO.CarFilter();
            filter.setStatus(Car.CarStatus.SOLD);
            List<Car> soldCars = carDAO.getCars(filter);
            
            System.out.println("Found " + soldCars.size() + " sold cars in database");
            
            if (!soldCars.isEmpty()) {
                // Get all sales to match with cars
                List<Sale> allSales = salesDAO.getSales(null);
                System.out.println("Found " + allSales.size() + " total sales records");
                
                // Create table rows for each sold car
                for (Car car : soldCars) {
                    // Find matching sale record
                    Sale carSale = null;
                    for (Sale sale : allSales) {
                        if (sale.getCarId() == car.getCarId()) {
                            carSale = sale;
                            break;
                        }
                    }
                    
                    // Skip if no sale record found (shouldn't happen for sold cars)
                    if (carSale == null) {
                        System.out.println("Warning: No sale record found for sold car ID: " + car.getCarId());
                        continue;
                    }
                    
                    // Get car details
                    String model = car.getDisplayName();
                    String year = String.valueOf(car.getManufactureYear());
                    String type = car.getCarModel() != null ? 
                                 car.getCarModel().getCategoryDisplayName() : "Unknown";
                    
                    // Create row data
                    Object[] rowData = new Object[] {
                        model,
                        year,
                        type,
                        carSale.getFormattedSaleDate(),
                        carSale.getFormattedSalePrice(),
                        carSale.getBuyerName(),
                        "View"
                    };
                    
                    soldCarsData.add(rowData);
                    System.out.println("Added sold car to display: " + model);
                }
            } else {
                // If no sold cars in database, fallback to CarStatusManager
                System.out.println("No sold cars found in database, using CarStatusManager");
                List<CarStatusManager.SoldCarRecord> inMemorySoldCars = 
                    CarStatusManager.getInstance().getSoldCars();
                
                for (CarStatusManager.SoldCarRecord record : inMemorySoldCars) {
                    CarManagement.Car car = record.getCar();
                    
                    Object[] rowData = new Object[] {
                        car.getModel(),
                        car.getYear(),
                        car.getType(),
                        record.getSaleDate(),
                        record.getSalePrice(),
                        record.getBuyerName(),
                        "View"
                    };
                    
                    soldCarsData.add(rowData);
                }
                
                System.out.println("Added " + inMemorySoldCars.size() + " sold cars from CarStatusManager");
            }
            
        } catch (Exception e) {
            System.err.println("Error loading sold cars: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to CarStatusManager if database access fails
            try {
                List<CarStatusManager.SoldCarRecord> inMemorySoldCars = 
                    CarStatusManager.getInstance().getSoldCars();
                
                for (CarStatusManager.SoldCarRecord record : inMemorySoldCars) {
                    CarManagement.Car car = record.getCar();
                    
                    Object[] rowData = new Object[] {
                        car.getModel(),
                        car.getYear(),
                        car.getType(),
                        record.getSaleDate(),
                        record.getSalePrice(),
                        record.getBuyerName(),
                        "View"
                    };
                    
                    soldCarsData.add(rowData);
                }
                
                System.out.println("Added " + inMemorySoldCars.size() + " sold cars from CarStatusManager (fallback)");
            } catch (Exception fallbackEx) {
                System.err.println("Fallback to CarStatusManager also failed: " + fallbackEx.getMessage());
            }
        }
        
        return soldCarsData;
    }
    
    /**
     * Get sale by index in the display list
     */
    public Sale getSaleByIndex(int index) {
        try {
            // Get all sold cars
            CarDAO.CarFilter filter = new CarDAO.CarFilter();
            filter.setStatus(Car.CarStatus.SOLD);
            List<Car> soldCars = carDAO.getCars(filter);
            
            // Check if index is valid
            if (index < 0 || index >= soldCars.size()) {
                System.err.println("Error: Invalid index " + index + " for " + soldCars.size() + " sold cars");
                return null;
            }
            
            // Get the car at the specified index
            Car car = soldCars.get(index);
            
            // Find the sale for this car
            List<Sale> carSales = salesDAO.getSalesByCarId(car.getCarId());
            if (!carSales.isEmpty()) {
                return carSales.get(0); // Return the first (usually only) sale
            } else {
                System.err.println("Error: No sale record found for car ID: " + car.getCarId());
            }
            
        } catch (Exception e) {
            System.err.println("Error getting sale by index: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Fallback to creating a placeholder sale
        try {
            // Get sold car record from CarStatusManager
            List<CarStatusManager.SoldCarRecord> inMemorySoldCars = 
                CarStatusManager.getInstance().getSoldCars();
                
            if (index < 0 || index >= inMemorySoldCars.size()) {
                System.err.println("Error: Invalid index " + index + " for " + inMemorySoldCars.size() + " in-memory sold cars");
                return null;
            }
            
            CarStatusManager.SoldCarRecord record = inMemorySoldCars.get(index);
            
            // Create a placeholder Sale object
            Sale placeholderSale = new Sale();
            placeholderSale.setBuyerName(record.getBuyerName());
            placeholderSale.setBuyerContact(record.getBuyerContact());
            
            // Parse price
            String priceStr = record.getSalePrice().replace("$", "").replace(",", "");
            if (!priceStr.isEmpty()) {
                try {
                    placeholderSale.setSalePrice(new BigDecimal(priceStr));
                } catch (NumberFormatException e) {
                    placeholderSale.setSalePrice(BigDecimal.ZERO);
                }
            }
            
            // Set payment method if available
            if (record.getPaymentMethod() != null && !record.getPaymentMethod().isEmpty()) {
                placeholderSale.setPaymentMethod(Sale.PaymentMethod.fromString(record.getPaymentMethod().toLowerCase()));
            }
            
            return placeholderSale;
        } catch (Exception fallbackEx) {
            System.err.println("Fallback to CarStatusManager also failed: " + fallbackEx.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get sales statistics for display
     */
    public SalesStatistics getSalesStatistics() {
        SalesStatistics stats = new SalesStatistics();
        
        try {
            // Get current date for timestamp range
            java.util.Date now = new java.util.Date();
            Timestamp currentTime = new Timestamp(now.getTime());
            
            // Create timestamp for start of current month
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(now);
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            Timestamp monthStart = new Timestamp(cal.getTimeInMillis());
            
            // Get sales statistics from database
            SalesDAO.SalesStats dbStats = salesDAO.getSalesStats(null, currentTime);
            
            // Set overall statistics
            stats.totalSoldCount = dbStats.getTotalSales();
            stats.totalSalesValue = dbStats.getTotalRevenue().doubleValue();
            
            // Calculate average price
            if (stats.totalSoldCount > 0) {
                stats.averagePrice = stats.totalSalesValue / stats.totalSoldCount;
            }
            
            // Get monthly sales count
            SalesDAO.SalesFilter monthFilter = new SalesDAO.SalesFilter();
            monthFilter.setStartDate(monthStart);
            monthFilter.setEndDate(currentTime);
            stats.monthlySalesCount = salesDAO.getSales(monthFilter).size();
            
            System.out.println("Retrieved sales statistics from database:");
            System.out.println("Total sold: " + stats.totalSoldCount);
            System.out.println("Total value: " + stats.totalSalesValue);
            System.out.println("Average price: " + stats.averagePrice);
            System.out.println("Monthly sales: " + stats.monthlySalesCount);
            
        } catch (Exception e) {
            System.err.println("Error getting sales statistics from database: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to calculating from CarStatusManager
            try {
                List<CarStatusManager.SoldCarRecord> inMemorySoldCars = 
                    CarStatusManager.getInstance().getSoldCars();
                
                stats.totalSoldCount = inMemorySoldCars.size();
                
                // Calculate total value and average price
                double totalValue = 0;
                for (CarStatusManager.SoldCarRecord record : inMemorySoldCars) {
                    String priceStr = record.getSalePrice().replace("$", "").replace(",", "");
                    try {
                        double price = Double.parseDouble(priceStr);
                        totalValue += price;
                    } catch (NumberFormatException nfe) {
                        // Skip invalid prices
                    }
                }
                
                stats.totalSalesValue = totalValue;
                
                if (stats.totalSoldCount > 0) {
                    stats.averagePrice = totalValue / stats.totalSoldCount;
                }
                
                // Count monthly sales (assume current month format is "MMM")
                String currentMonth = new java.text.SimpleDateFormat("MMM").format(new java.util.Date());
                stats.monthlySalesCount = 0;
                
                for (CarStatusManager.SoldCarRecord record : inMemorySoldCars) {
                    if (record.getSaleDate().contains(currentMonth)) {
                        stats.monthlySalesCount++;
                    }
                }
                
                System.out.println("Calculated sales statistics from CarStatusManager (fallback):");
                System.out.println("Total sold: " + stats.totalSoldCount);
                System.out.println("Total value: " + stats.totalSalesValue);
                System.out.println("Average price: " + stats.averagePrice);
                System.out.println("Monthly sales: " + stats.monthlySalesCount);
                
            } catch (Exception fallbackEx) {
                System.err.println("Fallback to CarStatusManager also failed: " + fallbackEx.getMessage());
                
                // Set default values
                stats.totalSoldCount = 0;
                stats.totalSalesValue = 0;
                stats.averagePrice = 0;
                stats.monthlySalesCount = 0;
            }
        }
        
        return stats;
    }
    
    /**
     * Delete a sale record (if allowed)
     */
    public boolean deleteSale(int saleId) {
        try {
            // First check if the user has permission to delete sales
            boolean isAdmin = true; // This should be determined by user role
            
            if (!isAdmin) {
                JOptionPane.showMessageDialog(null,
                    "You do not have permission to delete sales.",
                    "Permission Denied",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            // Get the sale to log details
            Sale sale = salesDAO.getSaleById(saleId);
            if (sale == null) {
                JOptionPane.showMessageDialog(null,
                    "Sale not found with ID: " + saleId,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Delete the sale
            boolean success = salesDAO.deleteSale(saleId);
            
            if (success) {
                // Log the deletion
                auditLogDAO.logAction(currentStaffId, "Sale Deleted",
                    "Deleted sale ID: " + saleId + " - Buyer: " + sale.getBuyerName() +
                    " - Amount: " + sale.getFormattedSalePrice(),
                    "Success");
                
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                    "Failed to delete sale record.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error deleting sale: " + e.getMessage(),
                "System Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Sales statistics class
     */
    public static class SalesStatistics {
        public int totalSoldCount = 0;
        public double totalSalesValue = 0;
        public double averagePrice = 0;
        public int monthlySalesCount = 0;
    }
}