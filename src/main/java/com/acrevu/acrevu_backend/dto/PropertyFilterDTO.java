package com.acrevu.acrevu_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyFilterDTO {
    private String city;
    private String locality;
    private String propertyType;
    private String listingType;
    private String category;
    private String bhk;
    private String furnishing;

    private String minPrice;
    private String maxPrice;

    private String minArea;
    private String maxArea;
    private String areaUnit;

    private String bedrooms;
    private String bathrooms;
}
