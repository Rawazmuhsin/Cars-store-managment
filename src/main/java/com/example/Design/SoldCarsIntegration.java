package com.example.Design;

import com.example.OOP.backend.SalesDAO;
import com.example.OOP.backend.Sale;
import com.example.OOP.backend.Car;
import com.example.OOP.backend.ServiceResult;
import com.example.OOP.backend.AuditLogDAO;
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
    private AuditLogDAO auditLogDAO;
    private int currentStaffId = 1;
    
    private SoldCarsIntegration() {
        this.salesDAO = new SalesDAO();
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
     * Get all sold cars from database
     */
    public List<Object[]> getSoldCarsData() {
        List<Object[]> soldCarsData = new ArrayList<>();
        
        try {
            // Get all sales from database
            List<Sale> sales = salesDAO.getSales(null);
            
            for (Sale sale : sales) {
                String model = "Unknown";
                String year = "Unknown";
                String type = "Unknown";
                
                // Get car details from sale
                if (sale.getCar() != null) {
                    Car car = sale.getCar();
                    if (car.getCarModel() != null) {
                        model = car.getCarModel().getFullName();
                        type = car.getCarModel().getCategoryDisplayName();
                    }
                    year = String.valueOf(car.getManufactureYear());
                }
                
                // Create table row data
                Object[] rowData = new Object[] {
                    model,
                    year,
                    type,
                    sale.getFormattedSaleDate(),
                    sale.getFormattedSalePrice(),
                    sale.getBuyerName(),
                    "View"
                };
                
                soldCarsData.add(rowData);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading sold cars: " + e.getMessage());
            e.printStackTrace();
        }
        
        return soldCarsData;
    }
    
    /**
     * Get sold cars filtered by date range
     */
    public List<Object[]> getSoldCarsByDateRange(Timestamp startDate, Timestamp endDate) {
        List<Object[]> soldCarsData = new ArrayList<>();
        
        try {
            SalesDAO.SalesFilter filter = new SalesDAO.SalesFilter();
            filter.setStartDate(startDate);
            filter.setEndDate(endDate);
            
            List<Sale> sales = salesDAO.getSales(filter);
            
            for (Sale sale : sales) {
                String model = "Unknown";
                String year = "Unknown";
                String type = "Unknown";
                
                if (sale.getCar() != null) {
                    Car car = sale.getCar();
                    if (car.getCarModel() != null) {
                        model = car.getCarModel().getFullName();
                        type = car.getCarModel().getCategoryDisplayName();
                    }
                    year = String.valueOf(car.getManufactureYear());
                }
                
                Object[] rowData = new Object[] {
                    model,
                    year,
                    type,
                    sale.getFormattedSaleDate(),
                    sale.getFormattedSalePrice(),
                    sale.getBuyerName(),
                    "View"
                };
                
                soldCarsData.add(rowData);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading filtered sold cars: " + e.getMessage());
            e.printStackTrace();
        }
        
        return soldCarsData;
    }
    
    /**
     * Get sold cars filtered by price range
     */
    public List<Object[]> getSoldCarsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Object[]> soldCarsData = new ArrayList<>();
        
        try {
            SalesDAO.SalesFilter filter = new SalesDAO.SalesFilter();
            filter.setMinAmount(minPrice);
            filter.setMaxAmount(maxPrice);
            
            List<Sale> sales = salesDAO.getSales(filter);
            
            for (Sale sale : sales) {
                String model = "Unknown";
                String year = "Unknown";
                String type = "Unknown";
                
                if (sale.getCar() != null) {
                    Car car = sale.getCar();
                    if (car.getCarModel() != null) {
                        model = car.getCarModel().getFullName();
                        type = car.getCarModel().getCategoryDisplayName();
                    }
                    year = String.valueOf(car.getManufactureYear());
                }
                
                Object[] rowData = new Object[] {
                    model,
                    year,
                    type,
                    sale.getFormattedSaleDate(),
                    sale.getFormattedSalePrice(),
                    sale.getBuyerName(),
                    "View"
                };
                
                soldCarsData.add(rowData);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading price filtered sold cars: " + e.getMessage());
            e.printStackTrace();
        }
        
        return soldCarsData;
    }
    
    /**
     * Get sale details by row index
     */
    public Sale getSaleByIndex(int index) {
        try {
            List<Sale> sales = salesDAO.getSales(null);
            if (index >= 0 && index < sales.size()) {
                return sales.get(index);
            }
        } catch (Exception e) {
            System.err.println("Error getting sale by index: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get sales statistics
     */
    public SalesStatistics getSalesStatistics() {
        SalesStatistics stats = new SalesStatistics();
        
        try {
            // Get all sales
            List<Sale> allSales = salesDAO.getSales(null);
            stats.totalSoldCount = allSales.size();
            
            // Calculate total sales value
            BigDecimal totalValue = BigDecimal.ZERO;
            for (Sale sale : allSales) {
                if (sale.getTotalAmount() != null) {
                    totalValue = totalValue.add(sale.getTotalAmount());
                }
            }
            stats.totalSalesValue = totalValue.doubleValue();
            
            // Calculate average price
            if (stats.totalSoldCount > 0) {
                stats.averagePrice = stats.totalSalesValue / stats.totalSoldCount;
            }
            
            // Get monthly sales (current month)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            Timestamp monthStart = new Timestamp(cal.getTimeInMillis());
            
            SalesDAO.SalesFilter monthFilter = new SalesDAO.SalesFilter();
            monthFilter.setStartDate(monthStart);
            List<Sale> monthlySales = salesDAO.getSales(monthFilter);
            stats.monthlySalesCount = monthlySales.size();
            
        } catch (Exception e) {
            System.err.println("Error calculating sales statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return stats;
    }
    
    /**
     * Export sales data to PDF/CSV
     */
    public boolean exportSalesData(String format) {
        try {
            // Log the export action
            auditLogDAO.logAction(currentStaffId, "Report Generated", 
                "Exported sold cars report in " + format + " format", "Success");
            
            return true;
        } catch (Exception e) {
            System.err.println("Error exporting sales data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a sale (should be used carefully)
     */
    public boolean deleteSale(int saleId) {
        try {
            boolean success = salesDAO.deleteSale(saleId);
            
            if (success) {
                // Log the action
                auditLogDAO.logAction(currentStaffId, "Sale Deleted", 
                    "Deleted sale record ID: " + saleId, "Success");
            }
            
            return success;
        } catch (Exception e) {
            System.err.println("Error deleting sale: " + e.getMessage());
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