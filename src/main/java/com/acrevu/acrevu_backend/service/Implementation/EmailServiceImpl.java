package com.acrevu.acrevu_backend.service.Implementation;

import com.acrevu.acrevu_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOTP(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("AcreVu Email Verification OTP");
        message.setText(
                "Your OTP is: " + otp +
                        "\n\nThis OTP is valid for 5 minutes." +
                        "\n\n- Team AcreVu"
        );

        mailSender.send(message);
    }
}
