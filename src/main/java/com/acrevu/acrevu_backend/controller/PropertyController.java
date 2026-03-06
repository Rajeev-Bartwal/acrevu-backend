package com.acrevu.acrevu_backend.controller;

import com.acrevu.acrevu_backend.dto.ApiResponse;
import com.acrevu.acrevu_backend.dto.PageResponse;
import com.acrevu.acrevu_backend.dto.PropertyDTO;
import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.service.PropertyService;
import com.acrevu.acrevu_backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

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

        PropertyDTO savedProperty = propertyService.addProperty(property,user);

       ApiResponse<Object> apiResponse = ApiResponse.builder().
               success(true)
               .message("Created")
               .data(savedProperty)
               .build();

       return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/properties")
    public ResponseEntity<ApiResponse<PageResponse<PropertyDTO>>> getAllProperties(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir
    ) {

        Pageable pageable = (Pageable) PageRequest.of(page, size,
                sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending()
        );

        PageResponse<PropertyDTO> data = propertyService.getAllProperties(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "All Property Fetched", data));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Object>> getMyProperties(@AuthenticationPrincipal User user) {
        System.out.println("getMyProperties");

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .success(true)
                .message("User properties fetched successfully")
                .data(propertyService.getPropertyByUserId(user))
                .build();

        return  new ResponseEntity<>(apiResponse , HttpStatus.OK);
    }
}
