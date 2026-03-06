package com.acrevu.acrevu_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String listingType;
    private String propertyType;
    private String category;
    private String bhk;
    private String area;
    private String areaUnit; // "sqft", "sqyard", "sqmeter"

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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
