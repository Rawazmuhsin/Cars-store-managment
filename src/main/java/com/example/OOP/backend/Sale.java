package com.example.OOP.backend;


import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Sale entity model representing a car sale transaction
 * Maps to the sales table in the database
 */
public class Sale {
    
    // Primary key
    private int saleId;
    
    // Foreign keys
    private int carId;
    private int handledBy;
    
    // Sale details
    private String buyerName;
    private String buyerContact;
    private Timestamp saleDate;
    private BigDecimal salePrice;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String saleNotes;
    
    // Related objects (for joins)
    private Car car;
    private Staff handledByStaff;
    
    /**
     * Payment method enumeration
     */
    public enum PaymentMethod {
        CASH("cash"),
        CREDIT_CARD("credit_card"),
        BANK_TRANSFER("bank_transfer"),
        FINANCING("financing");
        
        private final String value;
        
        PaymentMethod(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static PaymentMethod fromString(String value) {
            for (PaymentMethod method : PaymentMethod.values()) {
                if (method.value.equalsIgnoreCase(value)) {
                    return method;
                }
            }
            return CASH; // default
        }
        
        @Override
        public String toString() {
            return value.replace("_", " ").toUpperCase();
        }
    }
    
    /**
     * Payment status enumeration
     */
    public enum PaymentStatus {
        PENDING("pending"),
        COMPLETED("completed");
        
        private final String value;
        
        PaymentStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static PaymentStatus fromString(String value) {
            for (PaymentStatus status : PaymentStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            return COMPLETED; // default
        }
        
        @Override
        public String toString() {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }
    }
    
    /**
     * Default constructor
     */
    public Sale() {
        this.saleDate = new Timestamp(System.currentTimeMillis());
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.paymentMethod = PaymentMethod.CASH;
    }
    
    /**
     * Constructor with basic details
     */
    public Sale(int carId, String buyerName, String buyerContact, BigDecimal salePrice, 
                PaymentMethod paymentMethod, int handledBy) {
        this();
        this.carId = carId;
        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.salePrice = salePrice;
        this.paymentMethod = paymentMethod;
        this.handledBy = handledBy;
    }
    
    /**
     * Full constructor
     */
    public Sale(int saleId, int carId, String buyerName, String buyerContact, 
                Timestamp saleDate, BigDecimal salePrice, BigDecimal taxAmount, 
                BigDecimal totalAmount, PaymentMethod paymentMethod, 
                PaymentStatus paymentStatus, int handledBy, String saleNotes) {
        this.saleId = saleId;
        this.carId = carId;
        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.saleDate = saleDate;
        this.salePrice = salePrice;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.handledBy = handledBy;
        this.saleNotes = saleNotes;
    }
    
    // Getters and Setters
    
    public int getSaleId() {
        return saleId;
    }
    
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    
    public int getCarId() {
        return carId;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public String getBuyerName() {
        return buyerName;
    }
    
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
    
    public String getBuyerContact() {
        return buyerContact;
    }
    
    public void setBuyerContact(String buyerContact) {
        this.buyerContact = buyerContact;
    }
    
    public Timestamp getSaleDate() {
        return saleDate;
    }
    
    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }
    
    public BigDecimal getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public int getHandledBy() {
        return handledBy;
    }
    
    public void setHandledBy(int handledBy) {
        this.handledBy = handledBy;
    }
    
    public String getSaleNotes() {
        return saleNotes;
    }
    
    public void setSaleNotes(String saleNotes) {
        this.saleNotes = saleNotes;
    }
    
    public Car getCar() {
        return car;
    }
    
    public void setCar(Car car) {
        this.car = car;
    }
    
    public Staff getHandledByStaff() {
        return handledByStaff;
    }
    
    public void setHandledByStaff(Staff handledByStaff) {
        this.handledByStaff = handledByStaff;
    }
    
    // Utility methods
    
    /**
     * Get formatted sale price
     */
    public String getFormattedSalePrice() {
        if (salePrice != null) {
            return String.format("$%,.2f", salePrice);
        }
        return "$0.00";
    }
    
    /**
     * Get formatted tax amount
     */
    public String getFormattedTaxAmount() {
        if (taxAmount != null) {
            return String.format("$%,.2f", taxAmount);
        }
        return "$0.00";
    }
    
    /**
     * Get formatted total amount
     */
    public String getFormattedTotalAmount() {
        if (totalAmount != null) {
            return String.format("$%,.2f", totalAmount);
        }
        return "$0.00";
    }
    
    /**
     * Get formatted sale date
     */
    public String getFormattedSaleDate() {
        if (saleDate != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
            return sdf.format(saleDate);
        }
        return "N/A";
    }
    
    /**
     * Get car display name (if car is loaded)
     */
    public String getCarDisplayName() {
        if (car != null) {
            return car.getDisplayName();
        }
        return "Car ID: " + carId;
    }
    
    /**
     * Calculate profit from this sale (sale price - car cost)
     */
    public BigDecimal getProfit() {
        if (car != null && salePrice != null && car.getCost() != null) {
            return salePrice.subtract(car.getCost());
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Get profit margin percentage
     */
    public double getProfitMargin() {
        if (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profit = getProfit();
            return profit.divide(salePrice, 4, java.math.RoundingMode.HALF_UP)
                   .multiply(new BigDecimal("100")).doubleValue();
        }
        return 0.0;
    }
    
    /**
     * Check if payment is completed
     */
    public boolean isPaymentCompleted() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }
    
    /**
     * Check if payment is pending
     */
    public boolean isPaymentPending() {
        return paymentStatus == PaymentStatus.PENDING;
    }
    
    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", carDisplayName='" + getCarDisplayName() + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", salePrice=" + getFormattedSalePrice() +
                ", saleDate=" + getFormattedSaleDate() +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Sale sale = (Sale) obj;
        return saleId == sale.saleId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(saleId);
    }
}
