package com.acrevu.acrevu_backend.service;

import com.acrevu.acrevu_backend.dto.PropertyDTO;
import com.acrevu.acrevu_backend.entity.User;

import java.util.List;

public interface PropertyService {

    PropertyDTO addProperty(PropertyDTO propertyDTO, User user);

    List<PropertyDTO> getAllProperties();
}
