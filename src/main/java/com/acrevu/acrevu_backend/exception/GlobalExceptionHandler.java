package com.acrevu.acrevu_backend.exception;


import com.acrevu.acrevu_backend.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> resourceNotFoundException(ResourceNotFoundException ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Bad Request (400) – e.g. Invalid OTP
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> badRequestException(BadRequestException ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Generic Exception (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> globalException(Exception ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message("Something went wrong")
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

