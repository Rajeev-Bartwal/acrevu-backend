package com.acrevu.acrevu_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class RegisterReq {

    private String name;
    private String email;
    private String password;
    private String mobileNumber;
    private String accountType;
    private String companyName;

    private List<DealerPreferenceRequest> preferences;
}
