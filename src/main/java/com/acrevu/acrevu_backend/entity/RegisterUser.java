package com.acrevu.acrevu_backend.entity;


import com.acrevu.acrevu_backend.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "register_users")
@Builder
public class RegisterUser {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Column(nullable = false, unique = true)
        @Email
        private String email;

        @Column(unique = true)
        private String mobileNumber;


        @Column(nullable = false)
        private String password;


        private String accountType;

        @Enumerated(EnumType.STRING)
        private UserStatus status;

        private String emailOtp;
        private LocalDateTime otpExpiry;
        private Boolean emailVerified = false;
}


