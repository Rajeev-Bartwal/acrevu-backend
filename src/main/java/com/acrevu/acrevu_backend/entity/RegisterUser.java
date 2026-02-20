package com.acrevu.acrevu_backend.dto;


import com.acrevu.acrevu_backend.enums.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Sign-up")
@Builder
public class RegisterUser {
        private String name;
        private String email;
        private String password;
        private String accountType;
        private String mobileNumber;
        private UserStatus status;
        private String emailOtp;
        private LocalDateTime otpExpiry;
        private Boolean emailVerified = false;
}


