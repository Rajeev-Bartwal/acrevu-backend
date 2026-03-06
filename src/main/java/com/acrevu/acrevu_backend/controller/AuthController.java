package com.acrevu.acrevu_backend.controller;


import com.acrevu.acrevu_backend.dto.*;
import com.acrevu.acrevu_backend.entity.RegisterUser;
import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.exception.BadRequestException;
import com.acrevu.acrevu_backend.exception.ResourceNotFoundException;
import com.acrevu.acrevu_backend.repository.UserRepository;
import com.acrevu.acrevu_backend.security.JWTService;
import com.acrevu.acrevu_backend.service.AuthService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private  AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService,
                          UserDetailsService userDetailsService,
                          JWTService jwtService
                          ,ModelMapper modelMapper,
                          UserRepository userRepository) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterReq request) {

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Opt Sent successFully")
                .success(true)
                .data(authService.registerUser(request))
                .build();
         return new ResponseEntity<>( apiResponse , HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@RequestBody VerifyOtpReq request) {
        String message = authService.verifyOtp(request);

        ApiResponse<Object> response = ApiResponse.builder().success(true).message(message).data(null).build();
        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody JwtAuthRequest jwtAuthRequest) throws Exception {
        try {

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtAuthRequest.getIdentifier(),
                            jwtAuthRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {

                User user = (User) userDetailsService
                        .loadUserByUsername(jwtAuthRequest.getIdentifier());

                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                JwtAuthResponse response = new JwtAuthResponse(accessToken, refreshToken, userDTO);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (BadCredentialsException ex) {
            throw new BadRequestException(ex.getMessage());
        }

        return new ResponseEntity<>(
                new ApiResponse<>(false, "Bad Credentials", HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        try {
            String email = jwtService.getUserName(refreshToken);

            if (!jwtService.isTokenExpired(refreshToken)) {

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "User not found", email));

                String newAccessToken = jwtService.generateAccessToken(user);

                UserDTO userDTO = modelMapper.map(user, UserDTO.class);

                JwtAuthResponse response =
                        new JwtAuthResponse(newAccessToken, refreshToken, userDTO);

                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token expired or invalid");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
