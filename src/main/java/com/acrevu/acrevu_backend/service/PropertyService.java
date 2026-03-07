package com.acrevu.acrevu_backend.service;

import com.acrevu.acrevu_backend.dto.PageResponse;
import com.acrevu.acrevu_backend.dto.PropertyDTO;
import com.acrevu.acrevu_backend.dto.PropertyFilterDTO;
import com.acrevu.acrevu_backend.entity.User;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PropertyService {

    PropertyDTO addProperty(PropertyDTO propertyDTO, User user);

    PageResponse<PropertyDTO> getAllProperties(Pageable pageble);

    List<PropertyDTO> getPropertyByUserId(User user);

    PageResponse<PropertyDTO> filterProperties(PropertyFilterDTO filterDTO, Pageable pageable);
}
