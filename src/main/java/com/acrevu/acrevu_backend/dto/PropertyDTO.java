package com.acrevu.acrevu_backend.dto;

import com.acrevu.acrevu_backend.enums.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {

    private Long id;

    private String userType;
    private String listingType;
    private String propertyType;
    private String category;
    private String bhk;
    private String area;
    private String areaUnit;

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


    public PropertyDTO(
            Long id,
            Role role,
            String listingType,
            String propertyType,
            String category,
            String bhk,
            String area,
            String areaUnit,
            String city,
            String locality,
            String address,
            String pincode,
            String floor,
            String totalFloors,
            String price,
            String rent,
            String deposit,
            String bedrooms,
            String bathrooms,
            String balconies,
            String furnishing,
            String contactName,
            String mobile,
            String email,
            String description,
            String amenities
    ) {
        this.id = id;
        this.userType = role.name();
        this.listingType = listingType;
        this.propertyType = propertyType;
        this.category = category;
        this.bhk = bhk;
        this.area = area;
        this.areaUnit = areaUnit;
        this.city = city;
        this.locality = locality;
        this.address = address;
        this.pincode = pincode;
        this.floor = floor;
        this.totalFloors = totalFloors;
        this.price = price;
        this.rent = rent;
        this.deposit = deposit;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.balconies = balconies;
        this.furnishing = furnishing;
        this.contactName = contactName;
        this.mobile = mobile;
        this.email = email;
        this.description = description;
        this.amenities = amenities;
    }
}
