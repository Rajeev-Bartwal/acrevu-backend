package com.acrevu.acrevu_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int id;

    @NotEmpty
    @Size(min = 4, message = "Name should be minimum of 4 characters")
    private String name;

    @Email
    @NotEmpty
    private String email;
    private String mobileNumber;
    private String role;
    private String status;

}

