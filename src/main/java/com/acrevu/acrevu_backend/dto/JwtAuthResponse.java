package com.acrevu.acrevu_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {

    private String accessToken;
    private String refreshToken;
    private UserDTO user;

}