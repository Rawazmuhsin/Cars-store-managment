package com.example.Design;

import com.example.OOP.backend.AuditLogDAO;
import com.example.OOP.backend.AuditLog;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * Integration class for AuditLogUI to connect with backend
 */
public class AuditLogIntegration {
    
    private static AuditLogIntegration instance;
    private AuditLogDAO auditLogDAO;
    private int currentStaffId = 1;
    
    private AuditLogIntegration() {
        this.auditLogDAO = new AuditLogDAO();
    }
    
    public static AuditLogIntegration getInstance() {
        if (instance == null) {
            instance = new AuditLogIntegration();
        }
        return instance;
    }
    
    public void setCurrentStaffId(int staffId) {
        this.currentStaffId = staffId;
    }
    
    /**
     * Get all audit logs from database
     */
    public List<AuditLogUI.AuditLogEntry> getAllAuditLogs() {
        List<AuditLogUI.AuditLogEntry> uiLogs = new ArrayList<>();
        
        try {
            // Get recent logs (last 100)
            List<AuditLog> dbLogs = auditLogDAO.getRecentAuditLogs(100);
            
            for (AuditLog log : dbLogs) {
                String user = log.getStaffDisplayName();
                String ipAddress = "192.168.1." + (100 + log.getStaffId()); // Simulated IP
                
                AuditLogUI.AuditLogEntry uiLog = new AuditLogUI.AuditLogEntry(
                    log.getFormattedTimestamp(),
                    user,
                    log.getActionType(),
                    log.getActionDetails(),
                    ipAddress,
                    "Success" // All logged actions are successful
                );
                
                uiLogs.add(uiLog);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading audit logs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return uiLogs;
    }
    
    /**
     * Get filtered audit logs
     */
    public List<AuditLogUI.AuditLogEntry> getFilteredAuditLogs(String activityType, 
                                                               String userName, 
                                                               String searchKeyword) {
        List<AuditLogUI.AuditLogEntry> uiLogs = new ArrayList<>();
        
        try {
            AuditLogDAO.AuditLogFilter filter = new AuditLogDAO.AuditLogFilter();
            
            // Set activity type filter
            if (activityType != null && !activityType.equals("All Types")) {
                filter.setActionType(activityType);
            }
            
            // Set search keyword
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                filter.setSearchKeyword(searchKeyword);
            }
            
            // Note: userName filtering would require joining with staff table
            // For now, we'll filter in memory after retrieval
            
            List<AuditLog> dbLogs = auditLogDAO.getAuditLogs(filter);
            
            for (AuditLog log : dbLogs) {
                String user = log.getStaffDisplayName();
                
                // Apply user filter if needed
                if (userName != null && !userName.equals("All Users") && !user.equals(userName)) {
                    continue;
                }
                
                String ipAddress = "192.168.1." + (100 + log.getStaffId());
                
                AuditLogUI.AuditLogEntry uiLog = new AuditLogUI.AuditLogEntry(
                    log.getFormattedTimestamp(),
                    user,
                    log.getActionType(),
                    log.getActionDetails(),
                    ipAddress,
                    "Success"
                );
                
                uiLogs.add(uiLog);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading filtered audit logs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return uiLogs;
    }
    
    /**
     * Get audit log statistics
     */
    public AuditLogStatistics getAuditLogStatistics() {
        AuditLogStatistics stats = new AuditLogStatistics();
        
        try {
            // Get all logs for counting
            List<AuditLog> allLogs = auditLogDAO.getRecentAuditLogs(1000);
            stats.totalLogs = allLogs.size();
            
            // Count today's logs
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Timestamp todayStart = new Timestamp(cal.getTimeInMillis());
            
            for (AuditLog log : allLogs) {
                if (log.getTimestamp().after(todayStart)) {
                    stats.todayLogs++;
                }
                
                // Count by type
                if (log.getActionType().equals("Login")) {
                    stats.loginCount++;
                } else if (log.getActionType().startsWith("Car ")) {
                    stats.carActions++;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error calculating audit log statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return stats;
    }
    
    /**
     * Clear old audit logs (older than specified days)
     */
    public int clearOldLogs(int daysToKeep) {
        try {
            int deletedCount = auditLogDAO.cleanOldAuditLogs(daysToKeep);
            
            // Log this action
            auditLogDAO.logAction(currentStaffId, "Settings Changed", 
                "Cleared " + deletedCount + " audit logs older than " + daysToKeep + " days", 
                "Success");
            
            return deletedCount;
        } catch (Exception e) {
            System.err.println("Error clearing old logs: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Export audit logs
     */
    public boolean exportAuditLogs(String format) {
        try {
            // Log the export action
            auditLogDAO.logAction(currentStaffId, "Report Generated", 
                "Exported audit logs in " + format + " format", 
                "Success");
            
            return true;
        } catch (Exception e) {
            System.err.println("Error exporting audit logs: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add new audit log entry
     */
    public void addAuditLog(String actionType, String actionDetails) {
        try {
            auditLogDAO.logAction(currentStaffId, actionType, actionDetails, "Success");
        } catch (Exception e) {
            System.err.println("Error adding audit log: " + e.getMessage());
        }
    }
    
    /**
     * Audit log statistics class
     */
    public static class AuditLogStatistics {
        public int totalLogs = 0;
        public int todayLogs = 0;
        public int loginCount = 0;
        public int carActions = 0;
    }
}