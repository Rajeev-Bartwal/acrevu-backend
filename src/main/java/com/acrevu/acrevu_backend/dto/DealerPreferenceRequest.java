package com.acrevu.acrevu_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerPreferenceRequest {
    private String category;
    private List<String> types;
}
