package com.acrevu.acrevu_backend.service;

import com.acrevu.acrevu_backend.entity.RegisterUser;
import com.acrevu.acrevu_backend.dto.UserDTO;
import com.acrevu.acrevu_backend.dto.VerifyOtpReq;

public interface AuthService {

    UserDTO registerUser(RegisterUser  registerUser);

    String verifyOtp(VerifyOtpReq request);
}
