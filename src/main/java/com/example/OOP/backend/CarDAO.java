package com.example.OOP.backend;


import com.example.Database.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Car entity
 * Handles all database operations related to cars
 */
public class CarDAO {
    
    private DatabaseConnection dbConnection;
    
    public CarDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Create a new car in the database
     */
    public boolean createCar(Car car) {
        String sql = """
            INSERT INTO cars (model_id, vin, color, manufacture_year, mileage, 
                            price, cost, status, location, image_url, added_by, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, car.getModelId());
            pstmt.setString(2, car.getVin());
            pstmt.setString(3, car.getColor());
            pstmt.setInt(4, car.getManufactureYear());
            pstmt.setInt(5, car.getMileage());
            pstmt.setBigDecimal(6, car.getPrice());
            pstmt.setBigDecimal(7, car.getCost());
            pstmt.setString(8, car.getStatus().getValue());
            pstmt.setString(9, car.getLocation());
            pstmt.setString(10, car.getImageUrl());
            pstmt.setInt(11, car.getAddedBy());
            pstmt.setString(12, car.getNotes());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        car.setCarId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating car: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get car by ID with full details including model and manufacturer
     */
    public Car getCarById(int carId) {
        String sql = """
            SELECT c.*, cm.model_name, cm.category, m.name as manufacturer_name,
                   s.engine_type, s.transmission, s.fuel_type, s.horsepower, 
                   s.seats, s.fuel_economy, s.features
            FROM cars c
            LEFT JOIN car_models cm ON c.model_id = cm.model_id
            LEFT JOIN manufacturers m ON cm.manufacturer_id = m.manufacturer_id
            LEFT JOIN specifications s ON cm.model_id = s.model_id
            WHERE c.car_id = ?
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, carId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCar(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting car by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get car by VIN
     */
    public Car getCarByVin(String vin) {
        String sql = """
            SELECT c.*, cm.model_name, cm.category, m.name as manufacturer_name
            FROM cars c
            LEFT JOIN car_models cm ON c.model_id = cm.model_id
            LEFT JOIN manufacturers m ON cm.manufacturer_id = m.manufacturer_id
            WHERE c.vin = ?
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, vin);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCar(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting car by VIN: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all cars with optional filtering
     */
    public List<Car> getCars(CarFilter filter) {
        StringBuilder sql = new StringBuilder("""
            SELECT c.*, cm.model_name, cm.category, m.name as manufacturer_name
            FROM cars c
            LEFT JOIN car_models cm ON c.model_id = cm.model_id
            LEFT JOIN manufacturers m ON cm.manufacturer_id = m.manufacturer_id
            WHERE 1=1
            """);
        
        List<Object> parameters = new ArrayList<>();
        
        // Apply filters
        if (filter != null) {
            if (filter.getStatus() != null) {
                sql.append(" AND c.status = ?");
                parameters.add(filter.getStatus().getValue());
            }
            
            if (filter.getMinPrice() != null) {
                sql.append(" AND c.price >= ?");
                parameters.add(filter.getMinPrice());
            }
            
            if (filter.getMaxPrice() != null) {
                sql.append(" AND c.price <= ?");
                parameters.add(filter.getMaxPrice());
            }
            
            if (filter.getManufacturerName() != null && !filter.getManufacturerName().isEmpty()) {
                sql.append(" AND m.name LIKE ?");
                parameters.add("%" + filter.getManufacturerName() + "%");
            }
            
            if (filter.getModelName() != null && !filter.getModelName().isEmpty()) {
                sql.append(" AND cm.model_name LIKE ?");
                parameters.add("%" + filter.getModelName() + "%");
            }
            
            if (filter.getCategory() != null) {
                sql.append(" AND cm.category = ?");
                parameters.add(filter.getCategory().getValue());
            }
            
            if (filter.getYearFrom() != null) {
                sql.append(" AND c.manufacture_year >= ?");
                parameters.add(filter.getYearFrom());
            }
            
            if (filter.getYearTo() != null) {
                sql.append(" AND c.manufacture_year <= ?");
                parameters.add(filter.getYearTo());
            }
            
            if (filter.getColor() != null && !filter.getColor().isEmpty()) {
                sql.append(" AND c.color LIKE ?");
                parameters.add("%" + filter.getColor() + "%");
            }
        }
        
        sql.append(" ORDER BY c.date_added DESC");
        
        List<Car> cars = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cars.add(mapResultSetToCar(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting cars: " + e.getMessage());
            e.printStackTrace();
        }
        
        return cars;
    }
    
    /**
     * Get all available cars
     */
    public List<Car> getAvailableCars() {
        CarFilter filter = new CarFilter();
        filter.setStatus(Car.CarStatus.AVAILABLE);
        return getCars(filter);
    }
    
    /**
     * Get all sold cars
     */
    public List<Car> getSoldCars() {
        CarFilter filter = new CarFilter();
        filter.setStatus(Car.CarStatus.SOLD);
        return getCars(filter);
    }
    
    /**
     * Update car details
     */
    public boolean updateCar(Car car) {
        String sql = """
            UPDATE cars SET model_id = ?, vin = ?, color = ?, manufacture_year = ?,
                          mileage = ?, price = ?, cost = ?, status = ?, 
                          location = ?, image_url = ?, notes = ?
            WHERE car_id = ?
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, car.getModelId());
            pstmt.setString(2, car.getVin());
            pstmt.setString(3, car.getColor());
            pstmt.setInt(4, car.getManufactureYear());
            pstmt.setInt(5, car.getMileage());
            pstmt.setBigDecimal(6, car.getPrice());
            pstmt.setBigDecimal(7, car.getCost());
            pstmt.setString(8, car.getStatus().getValue());
            pstmt.setString(9, car.getLocation());
            pstmt.setString(10, car.getImageUrl());
            pstmt.setString(11, car.getNotes());
            pstmt.setInt(12, car.getCarId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating car: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update car status
     */
    public boolean updateCarStatus(int carId, Car.CarStatus status) {
        String sql = "UPDATE cars SET status = ? WHERE car_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.getValue());
            pstmt.setInt(2, carId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating car status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete car
     */
    public boolean deleteCar(int carId) {
        String sql = "DELETE FROM cars WHERE car_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, carId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting car: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if VIN already exists
     */
    public boolean vinExists(String vin) {
        String sql = "SELECT COUNT(*) FROM cars WHERE vin = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, vin);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking VIN existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get car count by status
     */
    public int getCarCountByStatus(Car.CarStatus status) {
        String sql = "SELECT COUNT(*) FROM cars WHERE status = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.getValue());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting car count by status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get total car count
     */
    public int getTotalCarCount() {
        String sql = "SELECT COUNT(*) FROM cars";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total car count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get recent cars (added in last N days)
     */
    public List<Car> getRecentCars(int days) {
        String sql = """
            SELECT c.*, cm.model_name, cm.category, m.name as manufacturer_name
            FROM cars c
            LEFT JOIN car_models cm ON c.model_id = cm.model_id
            LEFT JOIN manufacturers m ON cm.manufacturer_id = m.manufacturer_id
            WHERE c.date_added >= DATE_SUB(NOW(), INTERVAL ? DAY)
            ORDER BY c.date_added DESC
            """;
        
        List<Car> cars = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, days);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cars.add(mapResultSetToCar(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting recent cars: " + e.getMessage());
            e.printStackTrace();
        }
        
        return cars;
    }
    
    /**
     * Search cars by keyword (searches in model name, manufacturer, color, VIN)
     */
    public List<Car> searchCars(String keyword) {
        String sql = """
            SELECT c.*, cm.model_name, cm.category, m.name as manufacturer_name
            FROM cars c
            LEFT JOIN car_models cm ON c.model_id = cm.model_id
            LEFT JOIN manufacturers m ON cm.manufacturer_id = m.manufacturer_id
            WHERE m.name LIKE ? OR cm.model_name LIKE ? OR c.color LIKE ? OR c.vin LIKE ?
            ORDER BY c.date_added DESC
            """;
        
        List<Car> cars = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cars.add(mapResultSetToCar(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching cars: " + e.getMessage());
            e.printStackTrace();
        }
        
        return cars;
    }
    
    /**
     * Helper method to map ResultSet to Car object
     */
    private Car mapResultSetToCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        
        // Basic car fields
        car.setCarId(rs.getInt("car_id"));
        car.setModelId(rs.getInt("model_id"));
        car.setVin(rs.getString("vin"));
        car.setColor(rs.getString("color"));
        car.setManufactureYear(rs.getInt("manufacture_year"));
        car.setMileage(rs.getInt("mileage"));
        car.setPrice(rs.getBigDecimal("price"));
        car.setCost(rs.getBigDecimal("cost"));
        car.setStatus(Car.CarStatus.fromString(rs.getString("status")));
        car.setDateAdded(rs.getTimestamp("date_added"));
        car.setLocation(rs.getString("location"));
        car.setImageUrl(rs.getString("image_url"));
        car.setAddedBy(rs.getInt("added_by"));
        car.setNotes(rs.getString("notes"));
        
        // Create and set CarModel if available
        try {
            String modelName = rs.getString("model_name");
            if (modelName != null) {
                CarModel carModel = new CarModel();
                carModel.setModelId(rs.getInt("model_id"));
                carModel.setModelName(modelName);
                
                // Set category if available
                String categoryStr = rs.getString("category");
                if (categoryStr != null) {
                    carModel.setCategory(CarModel.CarCategory.fromString(categoryStr));
                }
                
                // Create and set Manufacturer if available
                String manufacturerName = rs.getString("manufacturer_name");
                if (manufacturerName != null) {
                    Manufacturer manufacturer = new Manufacturer();
                    manufacturer.setName(manufacturerName);
                    carModel.setManufacturer(manufacturer);
                }
                
                car.setCarModel(carModel);
            }
        } catch (SQLException e) {
            // Some fields might not be available in all queries, ignore
        }
        
        return car;
    }
    
    /**
     * Car filter class for filtering cars
     */
    public static class CarFilter {
        private Car.CarStatus status;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private String manufacturerName;
        private String modelName;
        private CarModel.CarCategory category;
        private Integer yearFrom;
        private Integer yearTo;
        private String color;
        private Integer addedBy;
        
        // Getters and Setters
        public Car.CarStatus getStatus() { return status; }
        public void setStatus(Car.CarStatus status) { this.status = status; }
        
        public BigDecimal getMinPrice() { return minPrice; }
        public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
        
        public BigDecimal getMaxPrice() { return maxPrice; }
        public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
        
        public String getManufacturerName() { return manufacturerName; }
        public void setManufacturerName(String manufacturerName) { this.manufacturerName = manufacturerName; }
        
        public String getModelName() { return modelName; }
        public void setModelName(String modelName) { this.modelName = modelName; }
        
        public CarModel.CarCategory getCategory() { return category; }
        public void setCategory(CarModel.CarCategory category) { this.category = category; }
        
        public Integer getYearFrom() { return yearFrom; }
        public void setYearFrom(Integer yearFrom) { this.yearFrom = yearFrom; }
        
        public Integer getYearTo() { return yearTo; }
        public void setYearTo(Integer yearTo) { this.yearTo = yearTo; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public Integer getAddedBy() { return addedBy; }
        public void setAddedBy(Integer addedBy) { this.addedBy = addedBy; }
    }
}
