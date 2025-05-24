package com.example.OOP.backend;


import java.sql.Timestamp;

/**
 * AuditLog entity model representing system audit logs
 * Maps to the audit_logs table in the database
 */
public class AuditLog {
    
    private int logId;
    private int staffId;
    private String actionType;
    private String tableAffected;
    private Integer recordId;
    private String actionDetails;
    private Timestamp timestamp;
    
    // Additional fields for display (from joins)
    private String staffName;
    private String username;
    
    /**
     * Default constructor
     */
    public AuditLog() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Constructor with basic details
     */
    public AuditLog(int staffId, String actionType, String actionDetails) {
        this();
        this.staffId = staffId;
        this.actionType = actionType;
        this.actionDetails = actionDetails;
    }
    
    /**
     * Full constructor
     */
    public AuditLog(int logId, int staffId, String actionType, String tableAffected, 
                   Integer recordId, String actionDetails, Timestamp timestamp) {
        this.logId = logId;
        this.staffId = staffId;
        this.actionType = actionType;
        this.tableAffected = tableAffected;
        this.recordId = recordId;
        this.actionDetails = actionDetails;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    
    public int getLogId() {
        return logId;
    }
    
    public void setLogId(int logId) {
        this.logId = logId;
    }
    
    public int getStaffId() {
        return staffId;
    }
    
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getTableAffected() {
        return tableAffected;
    }
    
    public void setTableAffected(String tableAffected) {
        this.tableAffected = tableAffected;
    }
    
    public Integer getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
    
    public String getActionDetails() {
        return actionDetails;
    }
    
    public void setActionDetails(String actionDetails) {
        this.actionDetails = actionDetails;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStaffName() {
        return staffName;
    }
    
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    // Utility methods
    
    /**
     * Get formatted timestamp
     */
    public String getFormattedTimestamp() {
        if (timestamp != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            return sdf.format(timestamp);
        }
        return "N/A";
    }
    
    /**
     * Get formatted date only
     */
    public String getFormattedDate() {
        if (timestamp != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
            return sdf.format(timestamp);
        }
        return "N/A";
    }
    
    /**
     * Get formatted time only
     */
    public String getFormattedTime() {
        if (timestamp != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
            return sdf.format(timestamp);
        }
        return "N/A";
    }
    
    /**
     * Get display name for staff (name or username)
     */
    public String getStaffDisplayName() {
        if (staffName != null && !staffName.isEmpty()) {
            return staffName;
        } else if (username != null && !username.isEmpty()) {
            return username;
        } else {
            return "Staff ID: " + staffId;
        }
    }
    
    @Override
    public String toString() {
        return "AuditLog{" +
                "logId=" + logId +
                ", staffName='" + getStaffDisplayName() + '\'' +
                ", actionType='" + actionType + '\'' +
                ", actionDetails='" + actionDetails + '\'' +
                ", timestamp=" + getFormattedTimestamp() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        AuditLog auditLog = (AuditLog) obj;
        return logId == auditLog.logId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(logId);
    }
}
