package com.acrevu.acrevu_backend.controller;

import com.acrevu.acrevu_backend.dto.ApiResponse;
import com.acrevu.acrevu_backend.dto.PropertyDTO;
import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/property")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Object>> createProperty(@RequestBody PropertyDTO property,
                                                   @AuthenticationPrincipal User user) {
        System.out.println("createProperty");
        System.out.println(property);
        System.out.println(user);
        PropertyDTO savedProperty = propertyService.addProperty(property,user);

       ApiResponse<Object> apiResponse = ApiResponse.builder().
               success(true)
               .message("Created")
               .data(savedProperty)
               .build();

       return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/properties")
    public ResponseEntity<ApiResponse<Object>> getAllProperties() {
        System.out.println("getAllProperties");

        ApiResponse<Object> apiResponse =  ApiResponse.builder()
                .message("All Property Fetched")
                .success(true)
                .data(propertyService.getAllProperties())
                .build();

        return  new ResponseEntity<>(apiResponse , HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Object>> getMyProperties(@AuthenticationPrincipal User user) {
        System.out.println("getMyProperties");

        propertyService.getAllMyProperties(user);
        ApiResponse<Object> apiResponse = ApiResponse.builder().build();
        return  new ResponseEntity<>(apiResponse , HttpStatus.OK);
    }
}
