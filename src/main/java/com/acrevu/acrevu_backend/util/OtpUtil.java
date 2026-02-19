package com.acrevu.acrevu_backend.util;


import java.util.Random;

public class OtpUtil {

    public static String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}