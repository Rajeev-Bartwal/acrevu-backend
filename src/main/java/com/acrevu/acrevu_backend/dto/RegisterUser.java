package com.acrevu.acrevu_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
        private String name;
        private String email;
        private String password;
        private String accountType;
        private String mobileNumber;
        private String address;
        private String city;
        private String state;
        private String pincode;

}


