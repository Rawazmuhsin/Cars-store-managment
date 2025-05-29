package com.example.OOP.backend;


import com.example.Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AuditLog entity
 * Handles all database operations related to audit logs
 */
public class AuditLogDAO {
    
    private DatabaseConnection dbConnection;
    
    public AuditLogDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Log an action to the audit trail
     */
    public boolean logAction(int staffId, String actionType, String actionDetails, String status) {
        String sql = """
            INSERT INTO audit_logs (staff_id, action_type, action_details, timestamp)
            VALUES (?, ?, ?, NOW())
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, staffId);
            pstmt.setString(2, actionType);
            pstmt.setString(3, actionDetails);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error logging audit action: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get audit logs with filtering
     */
    public List<AuditLog> getAuditLogs(AuditLogFilter filter) {
        StringBuilder sql = new StringBuilder("""
            SELECT al.*, s.username, s.full_name
            FROM audit_logs al
            LEFT JOIN staff s ON al.staff_id = s.staff_id
            WHERE 1=1
            """);
        
        List<Object> parameters = new ArrayList<>();
        
        // Apply filters
        if (filter != null) {
            if (filter.getStaffId() != null) {
                sql.append(" AND al.staff_id = ?");
                parameters.add(filter.getStaffId());
            }
            
            if (filter.getActionType() != null && !filter.getActionType().isEmpty()) {
                sql.append(" AND al.action_type LIKE ?");
                parameters.add("%" + filter.getActionType() + "%");
            }
            
            if (filter.getStartDate() != null) {
                sql.append(" AND al.timestamp >= ?");
                parameters.add(filter.getStartDate());
            }
            
            if (filter.getEndDate() != null) {
                sql.append(" AND al.timestamp <= ?");
                parameters.add(filter.getEndDate());
            }
            
            if (filter.getSearchKeyword() != null && !filter.getSearchKeyword().isEmpty()) {
                sql.append(" AND (al.action_details LIKE ? OR s.full_name LIKE ?)");
                String searchPattern = "%" + filter.getSearchKeyword() + "%";
                parameters.add(searchPattern);
                parameters.add(searchPattern);
            }
        }
        
        sql.append(" ORDER BY al.timestamp DESC");
        
        // Apply limit if specified
        if (filter != null && filter.getLimit() > 0) {
            sql.append(" LIMIT ?");
            parameters.add(filter.getLimit());
        }
        
        List<AuditLog> auditLogs = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AuditLog auditLog = new AuditLog();
                    auditLog.setLogId(rs.getInt("log_id"));
                    auditLog.setStaffId(rs.getInt("staff_id"));
                    auditLog.setActionType(rs.getString("action_type"));
                    auditLog.setTableAffected(rs.getString("table_affected"));
                    auditLog.setRecordId(rs.getInt("record_id"));
                    auditLog.setActionDetails(rs.getString("action_details"));
                    auditLog.setTimestamp(rs.getTimestamp("timestamp"));
                    
                    // Set staff information if available
                    String staffName = rs.getString("full_name");
                    if (staffName != null) {
                        auditLog.setStaffName(staffName);
                        auditLog.setUsername(rs.getString("username"));
                    }
                    
                    auditLogs.add(auditLog);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting audit logs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return auditLogs;
    }
    
    /**
     * Get recent audit logs (last N records)
     */
    public List<AuditLog> getRecentAuditLogs(int limit) {
        AuditLogFilter filter = new AuditLogFilter();
        filter.setLimit(limit);
        return getAuditLogs(filter);
    }
    
    /**
     * Get audit logs by staff member
     */
    public List<AuditLog> getAuditLogsByStaff(int staffId, int limit) {
        AuditLogFilter filter = new AuditLogFilter();
        filter.setStaffId(staffId);
        filter.setLimit(limit);
        return getAuditLogs(filter);
    }
    
    /**
     * Get audit logs by action type
     */
    public List<AuditLog> getAuditLogsByActionType(String actionType, int limit) {
        AuditLogFilter filter = new AuditLogFilter();
        filter.setActionType(actionType);
        filter.setLimit(limit);
        return getAuditLogs(filter);
    }
    
    /**
     * Search audit logs
     */
    public List<AuditLog> searchAuditLogs(String keyword, int limit) {
        AuditLogFilter filter = new AuditLogFilter();
        filter.setSearchKeyword(keyword);
        filter.setLimit(limit);
        return getAuditLogs(filter);
    }
    
    /**
     * Get audit log statistics
     */
    public AuditLogStats getAuditLogStats(Timestamp startDate, Timestamp endDate) {
        String sql = """
            SELECT 
                COUNT(*) as total_logs,
                COUNT(DISTINCT staff_id) as unique_users,
                COUNT(DISTINCT action_type) as unique_actions
            FROM audit_logs
            WHERE timestamp BETWEEN ? AND ?
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, startDate);
            pstmt.setTimestamp(2, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    AuditLogStats stats = new AuditLogStats();
                    stats.setTotalLogs(rs.getInt("total_logs"));
                    stats.setUniqueUsers(rs.getInt("unique_users"));
                    stats.setUniqueActions(rs.getInt("unique_actions"));
                    return stats;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting audit log stats: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new AuditLogStats();
    }
    
    /**
     * Clean old audit logs (older than specified days)
     */
    public int cleanOldAuditLogs(int daysToKeep) {
        String sql = "DELETE FROM audit_logs WHERE timestamp < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, daysToKeep);
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error cleaning old audit logs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Audit log filter class
     */
    public static class AuditLogFilter {
        private Integer staffId;
        private String actionType;
        private Timestamp startDate;
        private Timestamp endDate;
        private String searchKeyword;
        private int limit;
        
        // Getters and Setters
        public Integer getStaffId() { return staffId; }
        public void setStaffId(Integer staffId) { this.staffId = staffId; }
        
        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }
        
        public Timestamp getStartDate() { return startDate; }
        public void setStartDate(Timestamp startDate) { this.startDate = startDate; }
        
        public Timestamp getEndDate() { return endDate; }
        public void setEndDate(Timestamp endDate) { this.endDate = endDate; }
        
        public String getSearchKeyword() { return searchKeyword; }
        public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }
        
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
    }
    
    /**
     * Audit log statistics class
     */
    public static class AuditLogStats {
        private int totalLogs;
        private int uniqueUsers;
        private int uniqueActions;
        
        // Getters and Setters
        public int getTotalLogs() { return totalLogs; }
        public void setTotalLogs(int totalLogs) { this.totalLogs = totalLogs; }
        
        public int getUniqueUsers() { return uniqueUsers; }
        public void setUniqueUsers(int uniqueUsers) { this.uniqueUsers = uniqueUsers; }
        
        public int getUniqueActions() { return uniqueActions; }
        public void setUniqueActions(int uniqueActions) { this.uniqueActions = uniqueActions; }
    }
}
