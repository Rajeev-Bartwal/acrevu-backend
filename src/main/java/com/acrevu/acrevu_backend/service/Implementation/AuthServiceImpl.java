package com.acrevu.acrevu_backend.service.Implementation;

import com.acrevu.acrevu_backend.dto.DealerPreferenceRequest;
import com.acrevu.acrevu_backend.dto.RegisterReq;
import com.acrevu.acrevu_backend.entity.DealerPreference;
import com.acrevu.acrevu_backend.entity.RegisterUser;
import com.acrevu.acrevu_backend.dto.UserDTO;
import com.acrevu.acrevu_backend.dto.VerifyOtpReq;
import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.enums.Role;
import com.acrevu.acrevu_backend.enums.UserStatus;
import com.acrevu.acrevu_backend.exception.BadRequestException;
import com.acrevu.acrevu_backend.repository.DealerPreferenceRepository;
import com.acrevu.acrevu_backend.repository.RegisterRepo;
import com.acrevu.acrevu_backend.repository.UserRepository;
import com.acrevu.acrevu_backend.service.AuthService;
import com.acrevu.acrevu_backend.service.EmailService;
import com.acrevu.acrevu_backend.util.OtpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RegisterRepo registerRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final DealerPreferenceRepository  dealerPreferenceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public AuthServiceImpl(UserRepository userRepository,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           RegisterRepo registerRepo,
                           DealerPreferenceRepository dealerPreferenceRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.registerRepo = registerRepo;
        this.dealerPreferenceRepository = dealerPreferenceRepository;

    }


    public UserDTO registerUser(RegisterReq request) {

        // Step 1: Check in users table
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered");
        }

        if (request.getMobileNumber() != null &&
                userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            throw new BadRequestException("Mobile number already registered");
        }

        //  Dealer validation
        if ("DEALER".equalsIgnoreCase(request.getAccountType())) {
            if (request.getPreferences() == null || request.getPreferences().isEmpty()) {
                throw new BadRequestException("Dealer must select at least one preference");
            }
        }

        // Generate OTP
        String otp = OtpUtil.generateOtp();

        // Step 2: Check in register_user (pending table)
        Optional<RegisterUser> existingUser =
                registerRepo.findByEmailOrMobileNumber(
                        request.getEmail(),
                        request.getMobileNumber()
                );

        if (existingUser.isPresent()) {
            RegisterUser tempUser = existingUser.get();

            if (tempUser.getOtpExpiry().isAfter(LocalDateTime.now())) {
                tempUser.setEmailOtp(otp);
                tempUser.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

                RegisterUser updatedUser = registerRepo.save(tempUser);
                emailService.sendOTP(updatedUser.getEmail(), otp);

                return modelMapper.map(updatedUser, UserDTO.class);
            }

            registerRepo.delete(tempUser);
        }

        //  Convert dealer preferences to JSON (if dealer)
        String preferencesJson = null;

        if ("DEALER".equalsIgnoreCase(request.getAccountType())) {
            try {
                preferencesJson =
                        objectMapper.writeValueAsString(request.getPreferences());
            } catch (Exception e) {
                throw new RuntimeException("Error processing dealer preferences");
            }
        }

        // Step 3: Create new pending user
        RegisterUser user = RegisterUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.PENDING)
                .accountType(request.getAccountType())
                .emailOtp(otp)
                .otpExpiry(LocalDateTime.now().plusMinutes(5))
                .emailVerified(false)
                .mobileNumber(
                        request.getMobileNumber() != null && !request.getMobileNumber().isBlank()
                                ? request.getMobileNumber()
                                : null
                )
                .companyName(request.getCompanyName())
                .dealerPreferencesJson(preferencesJson)
                .build();

        // Step 4: Save
        RegisterUser savedUser = registerRepo.save(user);

        // Step 5: Send OTP
        emailService.sendOTP(savedUser.getEmail(), otp);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    private static Role getRole(RegisterUser request) {
        Role role = request.getAccountType().equalsIgnoreCase("DEALER")
                ? Role.DEALER
                : Role.USER;

        if (role == Role.DEALER) {

            if (request.getMobileNumber() == null || request.getMobileNumber().isBlank()) {
                throw new RuntimeException("Mobile number is required for dealer");
            }


        }

        return role;
    }


    public String verifyOtp(VerifyOtpReq request) {

        RegisterUser user = registerRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmailOtp().equals(request.getOtp())) {
            throw new BadRequestException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expired");
        }

        // Step 1: Create actual user
        User newUser = modelMapper.map(user, User.class);
        newUser.setId(null);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setStatus(UserStatus.ACTIVE);
        newUser.setRole(
                Objects.equals(user.getAccountType(), "DEALER")
                        ? Role.DEALER
                        : Role.USER
        );

        User savedUser = userRepository.save(newUser);

        // Step 2: If DEALER → Save preferences
        if (savedUser.getRole() == Role.DEALER &&
                user.getDealerPreferencesJson() != null) {

            try {
                List<DealerPreferenceRequest> preferences =
                        objectMapper.readValue(
                                user.getDealerPreferencesJson(),
                                new TypeReference<List<DealerPreferenceRequest>>() {}
                        );

                for (DealerPreferenceRequest pref : preferences) {
                    for (String type : pref.getTypes()) {

                        DealerPreference dp = new DealerPreference();
                        dp.setUser(savedUser);
                        dp.setCategory(pref.getCategory());
                        dp.setListingType(type);

                        dealerPreferenceRepository.save(dp);
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException("Error saving dealer preferences");
            }
        }

        // Step 3: Delete temporary user
        registerRepo.delete(user);

        return "OTP verified successfully.";
    }
}
