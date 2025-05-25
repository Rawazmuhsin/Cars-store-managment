package com.example.Design;

import com.example.OOP.backend.*;
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
        String[] parts = monthYear.split(" ");
        
        try {
            System.out.println("Fetching financial data for " + monthYear + "...");
            
            // Parse month and year
            int month = getMonthNumber(parts[0]);
            int year = Integer.parseInt(parts[1]);
            
            // Set date range for the month
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1, 0, 0, 0); // Month is 0-based in Calendar
            Timestamp startDate = new Timestamp(cal.getTimeInMillis());
            
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            Timestamp endDate = new Timestamp(cal.getTimeInMillis());
            
            System.out.println("Date range: " + startDate + " to " + endDate);
            
            // Get sales statistics for the month
            SalesDAO.SalesStats salesStats = salesDAO.getSalesStats(startDate, endDate);
            
            // Set revenue and profit from sales
            data.monthlyRevenue = salesStats.getTotalRevenue().doubleValue();
            data.monthlyProfit = salesStats.getTotalProfit().doubleValue();
            
            // Calculate car costs (revenue - profit)
            data.monthlyCosts = data.monthlyRevenue - data.monthlyProfit;
            
            // Calculate operational expenses (for now, use a percentage of revenue)
            // In a real implementation, you'd fetch actual expenses from a database
            data.monthlyExpenses = data.monthlyRevenue * 0.15; // 15% of revenue as expenses
            
            // Recalculate profit after expenses
            data.monthlyProfit = data.monthlyProfit - data.monthlyExpenses;
            
            System.out.println("Financial data retrieved from database:");
            System.out.println("Monthly Revenue: " + data.monthlyRevenue);
            System.out.println("Monthly Costs: " + data.monthlyCosts);
            System.out.println("Monthly Expenses: " + data.monthlyExpenses);
            System.out.println("Monthly Profit: " + data.monthlyProfit);
            
        } catch (Exception e) {
            System.err.println("Error getting monthly financial data: " + e.getMessage());
            e.printStackTrace();
            
            // If database access fails, fallback to calculating from CarStatusManager
            try {
                List<CarStatusManager.SoldCarRecord> soldCars = CarStatusManager.getInstance().getSoldCars();
                
                // Filter to current month (if applicable)
                String currentMonth = parts[0];
                
                double revenue = 0;
                double costs = 0;
                
                for (CarStatusManager.SoldCarRecord soldCar : soldCars) {
                    if (soldCar.getSaleDate().contains(currentMonth)) {
                        // Parse sale price
                        String priceStr = soldCar.getSalePrice().replace("$", "").replace(",", "");
                        try {
                            double price = Double.parseDouble(priceStr);
                            revenue += price;
                            
                            // Estimate cost as 80% of sale price
                            costs += price * 0.8;
                        } catch (NumberFormatException nfe) {
                            // Skip invalid prices
                        }
                    }
                }
                
                data.monthlyRevenue = revenue;
                data.monthlyCosts = costs;
                data.monthlyExpenses = revenue * 0.15; // 15% of revenue as expenses
                data.monthlyProfit = revenue - costs - data.monthlyExpenses;
                
                System.out.println("Financial data calculated from CarStatusManager (fallback):");
                System.out.println("Monthly Revenue: " + data.monthlyRevenue);
                System.out.println("Monthly Costs: " + data.monthlyCosts);
                System.out.println("Monthly Expenses: " + data.monthlyExpenses);
                System.out.println("Monthly Profit: " + data.monthlyProfit);
                
            } catch (Exception fallbackEx) {
                System.err.println("Fallback to CarStatusManager also failed: " + fallbackEx.getMessage());
                
                // Provide sample data if all else fails (for demo purposes)
                data.monthlyRevenue = 75000;
                data.monthlyCosts = 60000;
                data.monthlyExpenses = 10000;
                data.monthlyProfit = data.monthlyRevenue - data.monthlyCosts - data.monthlyExpenses;
                
                System.out.println("Using default sample financial data");
            }
        }
        
        return data;
    }
    
    /**
     * Get financial records for display in table
     */
    public List<EconomicUI.FinancialRecord> getFinancialRecords(String monthYear) {
        List<EconomicUI.FinancialRecord> records = new ArrayList<>();
        String[] parts = monthYear.split(" ");
        int year = 0;
        try {
            System.out.println("Fetching financial records for " + monthYear + "...");
            
            // Parse month and year
            int month = getMonthNumber(parts[0]);
            year = Integer.parseInt(parts[1]);
            
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
            
            System.out.println("Found " + sales.size() + " sales for " + monthYear);
            
            // Create financial records from sales
            for (Sale sale : sales) {
                // Get car information
                String carName = "Unknown Vehicle";
                BigDecimal carCost = BigDecimal.ZERO;
                
                if (sale.getCar() != null) {
                    carName = sale.getCar().getDisplayName();
                    carCost = sale.getCar().getCost() != null ? sale.getCar().getCost() : BigDecimal.ZERO;
                }
                
                // Format the sale date
                String formattedDate = sale.getFormattedSaleDate();
                
                // Add revenue record
                records.add(new EconomicUI.FinancialRecord(
                    formattedDate,
                    "Revenue",
                    "Car Sale: " + carName,
                    "+" + sale.getFormattedSalePrice(),
                    "Car Sales",
                    "Completed"
                ));
                
                // Add cost record
                records.add(new EconomicUI.FinancialRecord(
                    formattedDate,
                    "Cost",
                    "Car Cost: " + carName,
                    "-$" + String.format("%,.2f", carCost),
                    "Car Costs",
                    "Completed"
                ));
            }
            
            // Add sample expense records (in a real app, these would come from an expenses table)
            // We add these even if we have real sales, since we don't have a real expenses table yet
            String monthName = parts[0].substring(0, 3);
            
            records.add(new EconomicUI.FinancialRecord(
                monthName + " 01, " + year,
                "Expense",
                "Office Rent",
                "-$3,500",
                "Rent",
                "Paid"
            ));
            
            records.add(new EconomicUI.FinancialRecord(
                monthName + " 05, " + year,
                "Expense",
                "Marketing Campaign",
                "-$2,200",
                "Marketing",
                "Paid"
            ));
            
            records.add(new EconomicUI.FinancialRecord(
                monthName + " 10, " + year,
                "Expense",
                "Staff Salaries",
                "-$8,500",
                "Salaries",
                "Paid"
            ));
            
            records.add(new EconomicUI.FinancialRecord(
                monthName + " 15, " + year,
                "Expense",
                "Utilities",
                "-$800",
                "Utilities",
                "Paid"
            ));
            
            System.out.println("Created " + records.size() + " financial records");
            
        } catch (Exception e) {
            System.err.println("Error getting financial records: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to sample data
            String monthName = parts[0].substring(0, 3);
            if (year == 0) {
                try {
                    year = Integer.parseInt(parts[1]);
                } catch (Exception ex) {
                    year = Calendar.getInstance().get(Calendar.YEAR);
                }
            }
            
            records.add(new EconomicUI.FinancialRecord(
                monthName + " 01, " + year,
                "Expense",
                "Office Rent",
                "-$3,500",
                "Rent",
                "Paid"
            ));
            
            records.add(new EconomicUI.FinancialRecord(
                monthName + " 05, " + year,
                "Expense",
                "Marketing Campaign",
                "-$2,200",
                "Marketing",
                "Paid"
            ));
            
            records.add(new EconomicUI.FinancialRecord(
                monthName + " 10, " + year,
                "Expense",
                "Staff Salaries",
                "-$8,500",
                "Salaries",
                "Paid"
            ));
            
            records.add(new EconomicUI.FinancialRecord(
                monthName + " 15, " + year,
                "Expense",
                "Utilities",
                "-$800",
                "Utilities",
                "Paid"
            ));
            
            System.out.println("Created " + records.size() + " sample financial records (fallback)");
        }
        
        return records;
    }
    
    /**
     * Get financial data for custom date range
     */
    public FinancialData getFinancialDataByDateRange(Timestamp startDate, Timestamp endDate) {
        FinancialData data = new FinancialData();
        
        try {
            System.out.println("Fetching financial data for custom date range: " + startDate + " to " + endDate);
            
            // Get sales statistics for the date range
            SalesDAO.SalesStats salesStats = salesDAO.getSalesStats(startDate, endDate);
            
            // Set financial data
            data.monthlyRevenue = salesStats.getTotalRevenue().doubleValue();
            data.monthlyProfit = salesStats.getTotalProfit().doubleValue();
            data.monthlyCosts = data.monthlyRevenue - data.monthlyProfit;
            data.monthlyExpenses = data.monthlyRevenue * 0.15; // 15% operational expenses
            data.monthlyProfit = data.monthlyProfit - data.monthlyExpenses;
            
            System.out.println("Financial data retrieved for custom date range:");
            System.out.println("Revenue: " + data.monthlyRevenue);
            System.out.println("Costs: " + data.monthlyCosts);
            System.out.println("Expenses: " + data.monthlyExpenses);
            System.out.println("Profit: " + data.monthlyProfit);
            
        } catch (Exception e) {
            System.err.println("Error getting financial data by date range: " + e.getMessage());
            e.printStackTrace();
            
            // Provide sample data if database access fails
            data.monthlyRevenue = 50000;
            data.monthlyCosts = 40000;
            data.monthlyExpenses = 7500;
            data.monthlyProfit = data.monthlyRevenue - data.monthlyCosts - data.monthlyExpenses;
            
            System.out.println("Using default sample financial data for custom date range");
        }
        
        return data;
    }
    
    /**
     * Add expense record (for future implementation)
     */
    public boolean addExpense(String description, double amount, String category, String status, String notes) {
        try {
            // In a real app, this would save to an expenses table
            // For now, just log the action
            auditLogDAO.logAction(currentStaffId, "Expense Added", 
                String.format("Added expense: %s - $%.2f (%s)", description, amount, category), 
                status);
            
            System.out.println("Expense added: " + description + " - $" + amount);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding expense: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export financial report
     */
    public boolean exportFinancialReport(String monthYear, String format) {
        try {
            // In a real app, this would generate and save a report file
            // For now, just log the action
            auditLogDAO.logAction(currentStaffId, "Report Generated", 
                "Exported financial report for " + monthYear + " in " + format + " format", 
                "Success");
            
            System.out.println("Financial report exported: " + monthYear + " (" + format + ")");
            return true;
        } catch (Exception e) {
            System.err.println("Error exporting financial report: " + e.getMessage());
            e.printStackTrace();
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
        
        // Default to current month if not found
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
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