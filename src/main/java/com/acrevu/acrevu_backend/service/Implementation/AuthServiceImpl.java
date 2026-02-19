package com.acrevu.acrevu_backend.service.Implementation;

import com.acrevu.acrevu_backend.dto.RegisterUser;
import com.acrevu.acrevu_backend.dto.VerifyOtpReq;
import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.enums.Role;
import com.acrevu.acrevu_backend.enums.UserStatus;
import com.acrevu.acrevu_backend.repository.UserRepository;
import com.acrevu.acrevu_backend.service.AuthService;
import com.acrevu.acrevu_backend.service.EmailService;
import com.acrevu.acrevu_backend.util.OtpUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    public String registerUser(RegisterUser request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        final var role = getRole(request);

        String otp = OtpUtil.generateOtp();

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(UserStatus.PENDING)
                .emailOtp(otp)
                .otpExpiry(LocalDateTime.now().plusMinutes(5))
                .emailVerified(false)
                .mobileNumber(request.getMobileNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .build();


        userRepository.save(user);

        emailService.sendOTP(user.getEmail(), otp);

        return "User registered successfully. OTP sent to email.";
    }

    private static Role getRole(RegisterUser request) {
        Role role = request.getAccountType().equalsIgnoreCase("DEALER")
                ? Role.DEALER
                : Role.USER;

        if (role == Role.DEALER) {

            if (request.getMobileNumber() == null || request.getMobileNumber().isBlank()) {
                throw new RuntimeException("Mobile number is required for dealer");
            }

            if (request.getAddress() == null || request.getAddress().isBlank()) {
                throw new RuntimeException("Address is required for dealer");
            }

            if (request.getCity() == null || request.getCity().isBlank()) {
                throw new RuntimeException("City is required for dealer");
            }

            if (request.getState() == null || request.getState().isBlank()) {
                throw new RuntimeException("State is required for dealer");
            }

            if (request.getPincode() == null || request.getPincode().isBlank()) {
                throw new RuntimeException("Pincode is required for dealer");
            }
        }
        return role;
    }


    public String verifyOtp(VerifyOtpReq request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmailOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(true);
        user.setEmailOtp(null);
        user.setOtpExpiry(null);
        user.setCreatedAt(LocalDateTime.now());


        userRepository.save(user);

        return "OTP verified successfully.";
    }

}
