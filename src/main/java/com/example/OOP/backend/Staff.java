package com.example.OOP.backend;


import java.sql.Timestamp;

/**
 * Staff entity model representing system users
 * Maps to the staff table in the database
 */
public class Staff {
    
    private int staffId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private StaffRole role;
    private Timestamp createdAt;
    private Timestamp lastLogin;
    private StaffStatus status;
    
    /**
     * Staff role enumeration
     */
    public enum StaffRole {
        ADMIN("admin"),
        STAFF("staff");
        
        private final String value;
        
        StaffRole(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static StaffRole fromString(String value) {
            for (StaffRole role : StaffRole.values()) {
                if (role.value.equalsIgnoreCase(value)) {
                    return role;
                }
            }
            return STAFF; // default
        }
        
        @Override
        public String toString() {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }
    }
    
    /**
     * Staff status enumeration
     */
    public enum StaffStatus {
        ACTIVE("active"),
        INACTIVE("inactive");
        
        private final String value;
        
        StaffStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static StaffStatus fromString(String value) {
            for (StaffStatus status : StaffStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            return ACTIVE; // default
        }
        
        @Override
        public String toString() {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }
    }
    
    /**
     * Default constructor
     */
    public Staff() {
        this.role = StaffRole.STAFF;
        this.status = StaffStatus.ACTIVE;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Constructor with basic details
     */
    public Staff(String username, String password, String fullName, String email, StaffRole role) {
        this();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
    
    /**
     * Full constructor
     */
    public Staff(int staffId, String username, String password, String fullName, 
                String email, String phone, StaffRole role, Timestamp createdAt, 
                Timestamp lastLogin, StaffStatus status) {
        this.staffId = staffId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.status = status;
    }
    
    // Getters and Setters
    
    public int getStaffId() {
        return staffId;
    }
    
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public StaffRole getRole() {
        return role;
    }
    
    public void setRole(StaffRole role) {
        this.role = role;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public StaffStatus getStatus() {
        return status;
    }
    
    public void setStatus(StaffStatus status) {
        this.status = status;
    }
    
    // Utility methods
    
    /**
     * Check if staff is admin
     */
    public boolean isAdmin() {
        return role == StaffRole.ADMIN;
    }
    
    /**
     * Check if staff is active
     */
    public boolean isActive() {
        return status == StaffStatus.ACTIVE;
    }
    
    /**
     * Get formatted creation date
     */
    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
            return sdf.format(createdAt);
        }
        return "N/A";
    }
    
    /**
     * Get formatted last login
     */
    public String getFormattedLastLogin() {
        if (lastLogin != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm");
            return sdf.format(lastLogin);
        }
        return "Never";
    }
    
    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Staff staff = (Staff) obj;
        return staffId == staff.staffId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(staffId);
    }
}
