package com.acrevu.acrevu_backend.service.Implementation;

import com.acrevu.acrevu_backend.entity.RegisterUser;
import com.acrevu.acrevu_backend.dto.UserDTO;
import com.acrevu.acrevu_backend.dto.VerifyOtpReq;
import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.enums.Role;
import com.acrevu.acrevu_backend.enums.UserStatus;
import com.acrevu.acrevu_backend.exception.BadRequestException;
import com.acrevu.acrevu_backend.repository.RegisterRepo;
import com.acrevu.acrevu_backend.repository.UserRepository;
import com.acrevu.acrevu_backend.service.AuthService;
import com.acrevu.acrevu_backend.service.EmailService;
import com.acrevu.acrevu_backend.util.OtpUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RegisterRepo registerRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(UserRepository userRepository,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           RegisterRepo registerRepo) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.registerRepo = registerRepo;
    }


    public UserDTO registerUser(RegisterUser request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered");
        }



        final var role = getRole(request);

        String otp = OtpUtil.generateOtp();

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
        ).build();

        emailService.sendOTP(user.getEmail(), otp);

        return modelMapper.map(registerRepo.save(user), UserDTO.class);
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


        User newUser = modelMapper.map(user , User.class);
        newUser.setId(null);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setStatus(UserStatus.ACTIVE);
        newUser.setRole(Objects.equals(user.getAccountType(), "DEALER") ? Role.DEALER : Role.USER);

        userRepository.save(newUser);
//        registerRepo.delete(user);

        return "OTP verified successfully.";
    }

}
