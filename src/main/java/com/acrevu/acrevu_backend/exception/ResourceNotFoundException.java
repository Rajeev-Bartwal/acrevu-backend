package com.acrevu.acrevu_backend.exception;

import lombok.Data;

@Data
public class ResourceNotFoundException extends  RuntimeException {

    String resourceName;
    String fieldName;
    String fieldValue;

    public ResourceNotFoundException(String resourceName ,String fieldName,String fieldValue) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.fieldValue = fieldValue;
        this.fieldName = fieldName;
        this.resourceName = resourceName;
    }
}