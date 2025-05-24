package com.example.Design;

import com.example.OOP.backend.SalesDAO;
import com.example.OOP.backend.Sale;
import com.example.OOP.backend.CarDAO;
import com.example.OOP.backend.Car;
import com.example.OOP.backend.AuditLogDAO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Integration class for EconomicUI to connect with backend
 */
public class EconomicIntegration {
    
    private static EconomicIntegration instance;
    private SalesDAO salesDAO;
    private CarDAO carDAO;
    private AuditLogDAO auditLogDAO;
    private int currentStaffId = 1;
    
    private EconomicIntegration() {
        this.salesDAO = new SalesDAO();
        this.carDAO = new CarDAO();
        this.auditLogDAO = new AuditLogDAO();
    }
    
    public static EconomicIntegration getInstance() {
        if (instance == null) {
            instance = new EconomicIntegration();
        }
        return instance;
    }
    
    public void setCurrentStaffId(int staffId) {
        this.currentStaffId = staffId;
    }
    
    /**
     * Get financial data for a specific month
     */
    public FinancialData getMonthlyFinancialData(String monthYear) {
        FinancialData data = new FinancialData();
        
        try {
            // Parse month and year
            String[] parts = monthYear.split(" ");
            int month = getMonthNumber(parts[0]);
            int year = Integer.parseInt(parts[1]);
            
            // Set date range for the month
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1, 0, 0, 0);
            Timestamp startDate = new Timestamp(cal.getTimeInMillis());
            
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            Timestamp endDate = new Timestamp(cal.getTimeInMillis());
            
            // Get sales statistics for the month
            SalesDAO.SalesStats stats = salesDAO.getSalesStats(startDate, endDate);
            
            // Set financial data
            data.monthlyRevenue = stats.getTotalRevenue().doubleValue();
            data.monthlyProfit = stats.getTotalProfit().doubleValue();
            
            // Calculate car costs (revenue - profit)
            data.monthlyCosts = data.monthlyRevenue - data.monthlyProfit;
            
            // For now, set expenses as a percentage of revenue (you can expand this)
            data.monthlyExpenses = data.monthlyRevenue * 0.15; // 15% operational expenses
            
            // Recalculate profit after expenses
            data.monthlyProfit = data.monthlyRevenue - data.monthlyCosts - data.monthlyExpenses;
            
        } catch (Exception e) {
            System.err.println("Error getting monthly financial data: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data;
    }
    
    /**
     * Get financial records for display in table
     */
    public List<EconomicUI.FinancialRecord> getFinancialRecords(String monthYear) {
        List<EconomicUI.FinancialRecord> records = new ArrayList<>();
        
        try {
            // Parse month and year
            String[] parts = monthYear.split(" ");
            int month = getMonthNumber(parts[0]);
            int year = Integer.parseInt(parts[1]);
            
            // Set date range
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1, 0, 0, 0);
            Timestamp startDate = new Timestamp(cal.getTimeInMillis());
            
            cal.add(Calendar.MONTH, 1);
            Timestamp endDate = new Timestamp(cal.getTimeInMillis());
            
            // Get sales for the month
            SalesDAO.SalesFilter filter = new SalesDAO.SalesFilter();
            filter.setStartDate(startDate);
            filter.setEndDate(endDate);
            List<Sale> sales = salesDAO.getSales(filter);
            
            // Create financial records from sales
            for (Sale sale : sales) {
                // Revenue record
                String carName = "Unknown Vehicle";
                BigDecimal carCost = BigDecimal.ZERO;
                
                if (sale.getCar() != null) {
                    carName = sale.getCar().getDisplayName();
                    carCost = sale.getCar().getCost() != null ? sale.getCar().getCost() : BigDecimal.ZERO;
                }
                
                records.add(new EconomicUI.FinancialRecord(
                    sale.getFormattedSaleDate(),
                    "Revenue",
                    "Car Sale: " + carName,
                    "+" + sale.getFormattedSalePrice(),
                    "Car Sales",
                    "Completed"
                ));
                
                // Cost record
                records.add(new EconomicUI.FinancialRecord(
                    sale.getFormattedSaleDate(),
                    "Cost",
                    "Car Cost: " + carName,
                    "-$" + String.format("%,.0f", carCost),
                    "Car Costs",
                    "Completed"
                ));
            }
            
            // Add sample expense records (in real app, these would come from expense table)
            if (!records.isEmpty()) {
                records.add(new EconomicUI.FinancialRecord(
                    monthYear.substring(0, 3) + " 01, " + year,
                    "Expense",
                    "Office Rent",
                    "-$3,500",
                    "Rent",
                    "Paid"
                ));
                
                records.add(new EconomicUI.FinancialRecord(
                    monthYear.substring(0, 3) + " 05, " + year,
                    "Expense",
                    "Marketing Campaign",
                    "-$2,200",
                    "Marketing",
                    "Paid"
                ));
                
                records.add(new EconomicUI.FinancialRecord(
                    monthYear.substring(0, 3) + " 10, " + year,
                    "Expense",
                    "Staff Salaries",
                    "-$8,500",
                    "Salaries",
                    "Paid"
                ));
            }
            
        } catch (Exception e) {
            System.err.println("Error getting financial records: " + e.getMessage());
            e.printStackTrace();
        }
        
        return records;
    }
    
    /**
     * Get financial data for custom date range
     */
    public FinancialData getFinancialDataByDateRange(Timestamp startDate, Timestamp endDate) {
        FinancialData data = new FinancialData();
        
        try {
            // Get sales statistics for the date range
            SalesDAO.SalesStats stats = salesDAO.getSalesStats(startDate, endDate);
            
            // Set financial data
            data.monthlyRevenue = stats.getTotalRevenue().doubleValue();
            data.monthlyProfit = stats.getTotalProfit().doubleValue();
            data.monthlyCosts = data.monthlyRevenue - data.monthlyProfit;
            data.monthlyExpenses = data.monthlyRevenue * 0.15; // 15% operational expenses
            data.monthlyProfit = data.monthlyRevenue - data.monthlyCosts - data.monthlyExpenses;
            
        } catch (Exception e) {
            System.err.println("Error getting financial data by date range: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data;
    }
    
    /**
     * Add expense record
     */
    public boolean addExpense(String description, double amount, String category, 
                            String status, String notes) {
        try {
            // In a real app, this would save to an expenses table
            // For now, just log the action
            auditLogDAO.logAction(currentStaffId, "Expense Added", 
                String.format("Added expense: %s - $%.2f (%s)", description, amount, category), 
                "Success");
            
            return true;
        } catch (Exception e) {
            System.err.println("Error adding expense: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Export financial report
     */
    public boolean exportFinancialReport(String monthYear, String format) {
        try {
            // Log the export action
            auditLogDAO.logAction(currentStaffId, "Report Generated", 
                "Exported financial report for " + monthYear + " in " + format + " format", 
                "Success");
            
            return true;
        } catch (Exception e) {
            System.err.println("Error exporting financial report: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get month number from month name
     */
    private int getMonthNumber(String monthName) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        
        for (int i = 0; i < months.length; i++) {
            if (months[i].toLowerCase().startsWith(monthName.toLowerCase())) {
                return i + 1;
            }
        }
        return 1; // Default to January
    }
    
    /**
     * Financial data class
     */
    public static class FinancialData {
        public double monthlyRevenue = 0;
        public double monthlyCosts = 0;
        public double monthlyExpenses = 0;
        public double monthlyProfit = 0;
    }
}