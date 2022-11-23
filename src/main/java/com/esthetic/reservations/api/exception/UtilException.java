package com.esthetic.reservations.api.exception;

public class UtilException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public UtilException(String resourceName, String reason, String fieldName, String fieldValue) {
        super(String.format("%s %s %s = '%s'", resourceName, reason, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public UtilException(String resourceName, String reason) {
        super(String.format("%s %s", resourceName, reason));
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return this.fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

}
