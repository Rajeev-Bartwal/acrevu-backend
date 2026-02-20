package com.acrevu.acrevu_backend.controller;


import com.acrevu.acrevu_backend.dto.*;
import com.acrevu.acrevu_backend.entity.RegisterUser;
import com.acrevu.acrevu_backend.entity.User;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private  AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthService authService,
                          UserDetailsService userDetailsService,
                          JWTService jwtService
                          ,ModelMapper modelMapper) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUser request) {
         return new ResponseEntity<>(authService.registerUser(request) , HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@RequestBody VerifyOtpReq request) {
        String message = authService.verifyOtp(request);

        ApiResponse<Object> response = ApiResponse.builder().success(true).message(message).data(null).build();
        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody JwtAuthRequest jwtAuthRequest) throws Exception{
        try {
            Authentication authentication =
                    authManager
                            .authenticate(new UsernamePasswordAuthenticationToken(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword()));
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
            JwtAuthResponse response = new JwtAuthResponse();
            response.setUser(modelMapper.map((User)userDetails, UserDTO.class));
            String token = "";
            if (authentication.isAuthenticated()) {
                token = jwtService.generateToken(jwtAuthRequest.getUsername());
                response.setToken(token);
                return new ResponseEntity<>(response, HttpStatus.OK);

            }

        }catch (BadCredentialsException ex) {
            throw ex;
        }
        return new ResponseEntity<>(new ApiResponse<>(false,"Bad Credentials",HttpStatus.BAD_REQUEST), HttpStatus.UNAUTHORIZED);
    }

}
