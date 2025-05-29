package com.example.OOP.backend;



import com.example.Database.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Sale entity
 * Handles all database operations related to sales
 */
public class SalesDAO {
    
    private DatabaseConnection dbConnection;
    
    public SalesDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Create a new sale record
     */
    public boolean createSale(Sale sale) {
        String sql = """
            INSERT INTO sales (car_id, buyer_name, buyer_contact, sale_date, 
                             sale_price, tax_amount, total_amount, payment_method, 
                             payment_status, handled_by, sale_notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, sale.getCarId());
            pstmt.setString(2, sale.getBuyerName());
            pstmt.setString(3, sale.getBuyerContact());
            pstmt.setTimestamp(4, sale.getSaleDate());
            pstmt.setBigDecimal(5, sale.getSalePrice());
            pstmt.setBigDecimal(6, sale.getTaxAmount());
            pstmt.setBigDecimal(7, sale.getTotalAmount());
            pstmt.setString(8, sale.getPaymentMethod().getValue());
            pstmt.setString(9, sale.getPaymentStatus().getValue());
            pstmt.setInt(10, sale.getHandledBy());
            pstmt.setString(11, sale.getSaleNotes());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sale.setSaleId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating sale: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get sale by ID with full details
     */
    public Sale getSaleById(int saleId) {
        String sql = """
            SELECT s.*, c.vin, c.color, c.manufacture_year, c.cost,
                   cm.model_name, m.name as manufacturer_name,
                   st.full_name as staff_name
            FROM sales s
            LEFT JOIN cars c ON s.car_id = c.car_id
            LEFT JOIN car_models cm ON c.model_id = cm.model_id
            LEFT JOIN manufacturers m ON cm.manufacturer_id = m.manufacturer_id
            LEFT JOIN staff st ON s.handled_by = st.staff_id
            WHERE s.sale_id = ?
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, saleId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSale(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting sale by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all sales with optional filtering
     */
    public List<Sale> getSales(SalesFilter filter) {
        StringBuilder sql = new StringBuilder("""
            SELECT s.*, c.vin, c.color, c.manufacture_year, c.cost,
                   cm.model_name, m.name as manufacturer_name,
                   st.full_name as staff_name
            FROM sales s
            LEFT JOIN cars c ON s.car_id = c.car_id
            LEFT JOIN car_models cm ON c.model_id = cm.model_id
            LEFT JOIN manufacturers m ON cm.manufacturer_id = m.manufacturer_id
            LEFT JOIN staff st ON s.handled_by = st.staff_id
            WHERE 1=1
            """);
        
        List<Object> parameters = new ArrayList<>();
        
        // Apply filters
        if (filter != null) {
            if (filter.getStartDate() != null) {
                sql.append(" AND s.sale_date >= ?");
                parameters.add(filter.getStartDate());
            }
            
            if (filter.getEndDate() != null) {
                sql.append(" AND s.sale_date <= ?");
                parameters.add(filter.getEndDate());
            }
            
            if (filter.getMinAmount() != null) {
                sql.append(" AND s.total_amount >= ?");
                parameters.add(filter.getMinAmount());
            }
            
            if (filter.getMaxAmount() != null) {
                sql.append(" AND s.total_amount <= ?");
                parameters.add(filter.getMaxAmount());
            }
            
            if (filter.getPaymentMethod() != null) {
                sql.append(" AND s.payment_method = ?");
                parameters.add(filter.getPaymentMethod().getValue());
            }
            
            if (filter.getPaymentStatus() != null) {
                sql.append(" AND s.payment_status = ?");
                parameters.add(filter.getPaymentStatus().getValue());
            }
            
            if (filter.getBuyerName() != null && !filter.getBuyerName().isEmpty()) {
                sql.append(" AND s.buyer_name LIKE ?");
                parameters.add("%" + filter.getBuyerName() + "%");
            }
            
            if (filter.getHandledBy() != null) {
                sql.append(" AND s.handled_by = ?");
                parameters.add(filter.getHandledBy());
            }
        }
        
        sql.append(" ORDER BY s.sale_date DESC");
        
        List<Sale> sales = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(mapResultSetToSale(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting sales: " + e.getMessage());
            e.printStackTrace();
        }
        
        return sales;
    }
    
    /**
     * Get sales by car ID
     */
    public List<Sale> getSalesByCarId(int carId) {
        SalesFilter filter = new SalesFilter();
        filter.setCarId(carId);
        return getSales(filter);
    }
    
    /**
     * Get recent sales (last N days)
     */
    public List<Sale> getRecentSales(int days) {
        SalesFilter filter = new SalesFilter();
        // Set date range for last N days
        java.util.Calendar cal = java.util.Calendar.getInstance();
        filter.setEndDate(new Timestamp(cal.getTimeInMillis()));
        cal.add(java.util.Calendar.DAY_OF_MONTH, -days);
        filter.setStartDate(new Timestamp(cal.getTimeInMillis()));
        
        return getSales(filter);
    }
    
    /**
     * Update sale record
     */
    public boolean updateSale(Sale sale) {
        String sql = """
            UPDATE sales SET buyer_name = ?, buyer_contact = ?, sale_price = ?,
                           tax_amount = ?, total_amount = ?, payment_method = ?,
                           payment_status = ?, sale_notes = ?
            WHERE sale_id = ?
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sale.getBuyerName());
            pstmt.setString(2, sale.getBuyerContact());
            pstmt.setBigDecimal(3, sale.getSalePrice());
            pstmt.setBigDecimal(4, sale.getTaxAmount());
            pstmt.setBigDecimal(5, sale.getTotalAmount());
            pstmt.setString(6, sale.getPaymentMethod().getValue());
            pstmt.setString(7, sale.getPaymentStatus().getValue());
            pstmt.setString(8, sale.getSaleNotes());
            pstmt.setInt(9, sale.getSaleId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating sale: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update payment status
     */
    public boolean updatePaymentStatus(int saleId, Sale.PaymentStatus status) {
        String sql = "UPDATE sales SET payment_status = ? WHERE sale_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.getValue());
            pstmt.setInt(2, saleId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete sale (should be used carefully)
     */
    public boolean deleteSale(int saleId) {
        String sql = "DELETE FROM sales WHERE sale_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, saleId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting sale: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get sales statistics
     */
    public SalesStats getSalesStats(Timestamp startDate, Timestamp endDate) {
        String sql = """
            SELECT 
                COUNT(*) as total_sales,
                COALESCE(SUM(s.total_amount), 0) as total_revenue,
                COALESCE(AVG(s.total_amount), 0) as average_sale,
                COALESCE(SUM(s.sale_price - c.cost), 0) as total_profit
            FROM sales s
            LEFT JOIN cars c ON s.car_id = c.car_id
            WHERE s.sale_date BETWEEN ? AND ?
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, startDate);
            pstmt.setTimestamp(2, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SalesStats stats = new SalesStats();
                    stats.setTotalSales(rs.getInt("total_sales"));
                    stats.setTotalRevenue(rs.getBigDecimal("total_revenue"));
                    stats.setAverageSale(rs.getBigDecimal("average_sale"));
                    stats.setTotalProfit(rs.getBigDecimal("total_profit"));
                    return stats;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting sales stats: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new SalesStats(); // Return empty stats
    }
    
    /**
     * Get monthly sales summary
     */
    public List<MonthlySales> getMonthlySales(int year) {
        String sql = """
            SELECT 
                MONTH(s.sale_date) as month,
                COUNT(*) as sales_count,
                COALESCE(SUM(s.total_amount), 0) as total_revenue
            FROM sales s
            WHERE YEAR(s.sale_date) = ?
            GROUP BY MONTH(s.sale_date)
            ORDER BY MONTH(s.sale_date)
            """;
        
        List<MonthlySales> monthlySales = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MonthlySales monthly = new MonthlySales();
                    monthly.setMonth(rs.getInt("month"));
                    monthly.setSalesCount(rs.getInt("sales_count"));
                    monthly.setTotalRevenue(rs.getBigDecimal("total_revenue"));
                    monthlySales.add(monthly);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting monthly sales: " + e.getMessage());
            e.printStackTrace();
        }
        
        return monthlySales;
    }
    
    /**
     * Search sales by buyer name or car details
     */
    public List<Sale> searchSales(String keyword) {
        String sql = """
            SELECT s.*, c.vin, c.color, c.manufacture_year, c.cost,
                   cm.model_name, m.name as manufacturer_name,
                   st.full_name as staff_name
            FROM sales s
            LEFT JOIN cars c ON s.car_id = c.car_id
            LEFT JOIN car_models cm ON c.model_id = cm.model_id
            LEFT JOIN manufacturers m ON cm.manufacturer_id = m.manufacturer_id
            LEFT JOIN staff st ON s.handled_by = st.staff_id
            WHERE s.buyer_name LIKE ? OR s.buyer_contact LIKE ? 
               OR m.name LIKE ? OR cm.model_name LIKE ? OR c.vin LIKE ?
            ORDER BY s.sale_date DESC
            """;
        
        List<Sale> sales = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(mapResultSetToSale(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching sales: " + e.getMessage());
            e.printStackTrace();
        }
        
        return sales;
    }
    
    /**
     * Helper method to map ResultSet to Sale object
     */
    private Sale mapResultSetToSale(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        
        // Basic sale fields
        sale.setSaleId(rs.getInt("sale_id"));
        sale.setCarId(rs.getInt("car_id"));
        sale.setBuyerName(rs.getString("buyer_name"));
        sale.setBuyerContact(rs.getString("buyer_contact"));
        sale.setSaleDate(rs.getTimestamp("sale_date"));
        sale.setSalePrice(rs.getBigDecimal("sale_price"));
        sale.setTaxAmount(rs.getBigDecimal("tax_amount"));
        sale.setTotalAmount(rs.getBigDecimal("total_amount"));
        sale.setPaymentMethod(Sale.PaymentMethod.fromString(rs.getString("payment_method")));
        sale.setPaymentStatus(Sale.PaymentStatus.fromString(rs.getString("payment_status")));
        sale.setHandledBy(rs.getInt("handled_by"));
        sale.setSaleNotes(rs.getString("sale_notes"));
        
        // Create and set Car if available
        try {
            String vin = rs.getString("vin");
            if (vin != null) {
                Car car = new Car();
                car.setCarId(rs.getInt("car_id"));
                car.setVin(vin);
                car.setColor(rs.getString("color"));
                car.setManufactureYear(rs.getInt("manufacture_year"));
                car.setCost(rs.getBigDecimal("cost"));
                
                // Create and set CarModel if available
                String modelName = rs.getString("model_name");
                if (modelName != null) {
                    CarModel carModel = new CarModel();
                    carModel.setModelName(modelName);
                    
                    // Create and set Manufacturer if available
                    String manufacturerName = rs.getString("manufacturer_name");
                    if (manufacturerName != null) {
                        Manufacturer manufacturer = new Manufacturer();
                        manufacturer.setName(manufacturerName);
                        carModel.setManufacturer(manufacturer);
                    }
                    
                    car.setCarModel(carModel);
                }
                
                sale.setCar(car);
            }
        } catch (SQLException e) {
            // Some fields might not be available in all queries, ignore
        }
        
        // Create and set Staff if available
        try {
            String staffName = rs.getString("staff_name");
            if (staffName != null) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getInt("handled_by"));
                staff.setFullName(staffName);
                sale.setHandledByStaff(staff);
            }
        } catch (SQLException e) {
            // Staff name might not be available in all queries, ignore
        }
        
        return sale;
    }
    
    /**
     * Sales filter class for filtering sales
     */
    public static class SalesFilter {
        private Timestamp startDate;
        private Timestamp endDate;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private Sale.PaymentMethod paymentMethod;
        private Sale.PaymentStatus paymentStatus;
        private String buyerName;
        private Integer handledBy;
        private Integer carId;
        
        // Getters and Setters
        public Timestamp getStartDate() { return startDate; }
        public void setStartDate(Timestamp startDate) { this.startDate = startDate; }
        
        public Timestamp getEndDate() { return endDate; }
        public void setEndDate(Timestamp endDate) { this.endDate = endDate; }
        
        public BigDecimal getMinAmount() { return minAmount; }
        public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
        
        public BigDecimal getMaxAmount() { return maxAmount; }
        public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
        
        public Sale.PaymentMethod getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(Sale.PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public Sale.PaymentStatus getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(Sale.PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
        
        public String getBuyerName() { return buyerName; }
        public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
        
        public Integer getHandledBy() { return handledBy; }
        public void setHandledBy(Integer handledBy) { this.handledBy = handledBy; }
        
        public Integer getCarId() { return carId; }
        public void setCarId(Integer carId) { this.carId = carId; }
    }
    
    /**
     * Sales statistics class
     */
    public static class SalesStats {
        private int totalSales;
        private BigDecimal totalRevenue;
        private BigDecimal averageSale;
        private BigDecimal totalProfit;
        
        public SalesStats() {
            this.totalRevenue = BigDecimal.ZERO;
            this.averageSale = BigDecimal.ZERO;
            this.totalProfit = BigDecimal.ZERO;
        }
        
        // Getters and Setters
        public int getTotalSales() { return totalSales; }
        public void setTotalSales(int totalSales) { this.totalSales = totalSales; }
        
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
        
        public BigDecimal getAverageSale() { return averageSale; }
        public void setAverageSale(BigDecimal averageSale) { this.averageSale = averageSale; }
        
        public BigDecimal getTotalProfit() { return totalProfit; }
        public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }
        
        // Utility methods
        public String getFormattedTotalRevenue() {
            return String.format("$%,.2f", totalRevenue);
        }
        
        public String getFormattedAverageSale() {
            return String.format("$%,.2f", averageSale);
        }
        
        public String getFormattedTotalProfit() {
            return String.format("$%,.2f", totalProfit);
        }
    }
    
    /**
     * Monthly sales class
     */
    public static class MonthlySales {
        private int month;
        private int salesCount;
        private BigDecimal totalRevenue;
        
        public MonthlySales() {
            this.totalRevenue = BigDecimal.ZERO;
        }
        
        // Getters and Setters
        public int getMonth() { return month; }
        public void setMonth(int month) { this.month = month; }
        
        public int getSalesCount() { return salesCount; }
        public void setSalesCount(int salesCount) { this.salesCount = salesCount; }
        
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
        
        // Utility methods
        public String getMonthName() {
            String[] months = {"", "January", "February", "March", "April", "May", "June",
                             "July", "August", "September", "October", "November", "December"};
            return (month >= 1 && month <= 12) ? months[month] : "Unknown";
        }
        
        public String getFormattedRevenue() {
            return String.format("$%,.2f", totalRevenue);
        }
    }
}