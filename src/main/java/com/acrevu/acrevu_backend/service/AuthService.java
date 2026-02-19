package com.acrevu.acrevu_backend.service;

import com.acrevu.acrevu_backend.dto.RegisterUser;
import com.acrevu.acrevu_backend.dto.VerifyOtpReq;

public interface AuthService {

    String registerUser(RegisterUser  registerUser);

    String verifyOtp(VerifyOtpReq request);
}
