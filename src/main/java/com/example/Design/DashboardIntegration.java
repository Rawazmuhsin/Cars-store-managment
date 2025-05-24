package com.example.Design;

import com.example.OOP.backend.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Integration class for Dashboard UI to connect with backend database
 */
public class DashboardIntegration {
    
    private static DashboardIntegration instance;
    private CarDAO carDAO;
    private SalesDAO salesDAO;
    private AuditLogDAO auditLogDAO;
    
    private DashboardIntegration() {
        try {
            this.carDAO = new CarDAO();
            this.salesDAO = new SalesDAO();
            this.auditLogDAO = new AuditLogDAO();
        } catch (Exception e) {
            System.err.println("Error initializing DAOs: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static DashboardIntegration getInstance() {
        if (instance == null) {
            instance = new DashboardIntegration();
        }
        return instance;
    }
    
    /**
     * Get dashboard statistics from database
     */
    public DashboardStats getDashboardStats() {
        // Always initialize stats object first
        DashboardStats stats = new DashboardStats();
        
        try {
            // Check if DAOs are properly initialized
            if (carDAO == null || salesDAO == null) {
                System.err.println("DAOs not properly initialized, using default values");
                return getDefaultStats();
            }
            
            // Test database connection first
            if (!testDatabaseConnection()) {
                System.err.println("Database connection test failed, using default values");
                return getDefaultStats();
            }
            
            // Get car statistics
            stats.availableCars = carDAO.getCarCountByStatus(Car.CarStatus.AVAILABLE);
            stats.soldCars = carDAO.getCarCountByStatus(Car.CarStatus.SOLD);
            stats.totalCars = carDAO.getTotalCarCount();
            
            // Get coming soon cars count (placeholder for now)
            stats.comingSoonCars = 8; // You can implement ComingSoonDAO later
            
            // Get today's sales
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Timestamp todayStart = new Timestamp(cal.getTimeInMillis());
            
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Timestamp tomorrowStart = new Timestamp(cal.getTimeInMillis());
            
            SalesDAO.SalesStats todayStats = salesDAO.getSalesStats(todayStart, tomorrowStart);
            if (todayStats != null) {
                stats.soldToday = todayStats.getTotalSales();
                stats.totalRevenue = todayStats.getTotalRevenue() != null ? 
                    todayStats.getTotalRevenue().doubleValue() : 0.0;
            }
            
            // Get monthly statistics
            cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Timestamp monthStart = new Timestamp(cal.getTimeInMillis());
            
            SalesDAO.SalesStats monthStats = salesDAO.getSalesStats(monthStart, new Timestamp(System.currentTimeMillis()));
            if (monthStats != null) {
                stats.monthlySales = monthStats.getTotalSales();
                stats.monthlyRevenue = monthStats.getTotalRevenue() != null ? 
                    monthStats.getTotalRevenue().doubleValue() : 0.0;
            }
            
            System.out.println("Successfully loaded dashboard stats from database:");
            System.out.println("  - Available cars: " + stats.availableCars);
            System.out.println("  - Total cars: " + stats.totalCars);
            System.out.println("  - Sold today: " + stats.soldToday);
            System.out.println("  - Monthly revenue: $" + stats.monthlyRevenue);
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard stats: " + e.getMessage());
            e.printStackTrace();
            
            // Return default stats if any error occurs
            return getDefaultStats();
        }
        
        return stats;
    }
    
    /**
     * Get default statistics when database is unavailable
     */
    private DashboardStats getDefaultStats() {
        DashboardStats stats = new DashboardStats();
        stats.availableCars = 5;  // Sample data
        stats.soldCars = 3;
        stats.totalCars = 8;
        stats.comingSoonCars = 4;
        stats.soldToday = 1;
        stats.totalRevenue = 45000.0;
        stats.monthlySales = 12;
        stats.monthlyRevenue = 340000.0;
        
        System.out.println("Using default/sample dashboard statistics");
        return stats;
    }
    
    /**
     * Test database connection
     */
    private boolean testDatabaseConnection() {
        try {
            if (carDAO == null) {
                System.err.println("CarDAO is null");
                return false;
            }
            
            // Try a simple query to test connection
            int testCount = carDAO.getTotalCarCount();
            System.out.println("Database connection test successful. Total cars: " + testCount);
            return true;
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Dashboard statistics class
     */
    public static class DashboardStats {
        public int availableCars = 0;
        public int soldCars = 0;
        public int totalCars = 0;
        public int comingSoonCars = 0;
        public int soldToday = 0;
        public double totalRevenue = 0;
        public int monthlySales = 0;
        public double monthlyRevenue = 0;
        
        // Constructor to ensure all fields are initialized
        public DashboardStats() {
            this.availableCars = 0;
            this.soldCars = 0;
            this.totalCars = 0;
            this.comingSoonCars = 0;
            this.soldToday = 0;
            this.totalRevenue = 0.0;
            this.monthlySales = 0;
            this.monthlyRevenue = 0.0;
        }
        
        @Override
        public String toString() {
            return "DashboardStats{" +
                    "availableCars=" + availableCars +
                    ", soldCars=" + soldCars +
                    ", totalCars=" + totalCars +
                    ", comingSoonCars=" + comingSoonCars +
                    ", soldToday=" + soldToday +
                    ", totalRevenue=" + totalRevenue +
                    ", monthlySales=" + monthlySales +
                    ", monthlyRevenue=" + monthlyRevenue +
                    '}';
        }
    }
}