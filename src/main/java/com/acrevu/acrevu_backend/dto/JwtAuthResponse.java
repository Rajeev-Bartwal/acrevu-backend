package com.acrevu.acrevu_backend.dto;

import com.acrevu.acrevu_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {

    private String token;
    private UserDTO user;
}
