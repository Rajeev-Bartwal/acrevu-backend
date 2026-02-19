package com.acrevu.acrevu_backend.controller;


import com.acrevu.acrevu_backend.dto.RegisterUser;
import com.acrevu.acrevu_backend.dto.VerifyOtpReq;
import com.acrevu.acrevu_backend.repository.UserRepository;
import com.acrevu.acrevu_backend.service.AuthService;
import com.acrevu.acrevu_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterUser request) {
        return authService.registerUser(request);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpReq request) {
        return authService.verifyOtp(request);
    }

}
