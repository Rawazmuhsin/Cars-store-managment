package com.example.OOP.backend;


/**
 * Generic service result wrapper for consistent service responses
 * Encapsulates success/failure status, data, and messages
 */
public class ServiceResult<T> {
    
    private boolean success;
    private T data;
    private String message;
    private String errorCode;
    
    /**
     * Private constructor to enforce factory methods
     */
    private ServiceResult(boolean success, T data, String message, String errorCode) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
    }
    
    /**
     * Create successful result with data
     */
    public static <T> ServiceResult<T> success(T data) {
        return new ServiceResult<>(true, data, null, null);
    }
    
    /**
     * Create successful result with data and message
     */
    public static <T> ServiceResult<T> success(T data, String message) {
        return new ServiceResult<>(true, data, message, null);
    }
    
    /**
     * Create successful result with message only
     */
    public static <T> ServiceResult<T> success(String message) {
        return new ServiceResult<>(true, null, message, null);
    }
    
    /**
     * Create failure result with message
     */
    public static <T> ServiceResult<T> failure(String message) {
        return new ServiceResult<>(false, null, message, null);
    }
    
    /**
     * Create failure result with message and error code
     */
    public static <T> ServiceResult<T> failure(String message, String errorCode) {
        return new ServiceResult<>(false, null, message, errorCode);
    }
    
    /**
     * Create failure result with data and message (for partial failures)
     */
    public static <T> ServiceResult<T> failure(T data, String message) {
        return new ServiceResult<>(false, data, message, null);
    }
    
    // Getters
    
    public boolean isSuccess() {
        return success;
    }
    
    public boolean isFailure() {
        return !success;
    }
    
    public T getData() {
        return data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Get data or throw exception if failed
     */
    public T getDataOrThrow() throws ServiceException {
        if (!success) {
            throw new ServiceException(message, errorCode);
        }
        return data;
    }
    
    /**
     * Get data or return default value if failed
     */
    public T getDataOrDefault(T defaultValue) {
        return success ? data : defaultValue;
    }
    
    /**
     * Check if result has data
     */
    public boolean hasData() {
        return data != null;
    }
    
    @Override
    public String toString() {
        return "ServiceResult{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
    
    /**
     * Custom exception for service errors
     */
    public static class ServiceException extends Exception {
        private String errorCode;
        
        public ServiceException(String message) {
            super(message);
        }
        
        public ServiceException(String message, String errorCode) {
            super(message);
            this.errorCode = errorCode;
        }
        
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
        
        public String getErrorCode() {
            return errorCode;
        }
    }
}
