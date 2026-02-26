package com.acrevu.acrevu_backend.dto;

import lombok.Data;

@Data
public class PropertyDTO {

    private Long id;

    private String userType;
    private String listingType;
    private String propertyType;
    private String category;
    private String bhk;
    private String area;

    private String city;
    private String locality;
    private String address;
    private String pincode;

    private String floor;
    private String totalFloors;

    private String price;
    private String rent;
    private String deposit;

    private String bedrooms;
    private String bathrooms;
    private String balconies;
    private String furnishing;

    private String contactName;
    private String mobile;
    private String email;

    private String description;
    private String amenities;
}
