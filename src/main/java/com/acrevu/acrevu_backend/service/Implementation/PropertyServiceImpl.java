package com.acrevu.acrevu_backend.service.Implementation;

import com.acrevu.acrevu_backend.Specification.PropertySpecification;
import com.acrevu.acrevu_backend.dto.PageResponse;
import com.acrevu.acrevu_backend.dto.PropertyDTO;
import com.acrevu.acrevu_backend.dto.PropertyFilterDTO;
import com.acrevu.acrevu_backend.entity.Property;
import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.repository.PropertyRepository;
import com.acrevu.acrevu_backend.service.PropertyService;
import jakarta.servlet.ServletOutputStream;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    PropertyServiceImpl(PropertyRepository propertyRepository,
                        ModelMapper modelMapper) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public PropertyDTO addProperty(PropertyDTO propertyDTO, User user) {

        Property property = modelMapper.map(propertyDTO, Property.class);
        property.setUser(user);
        property.setCreatedAt(LocalDateTime.now());
        propertyRepository.save(property);

        return modelMapper.map(property, PropertyDTO.class);
    }

    @Override
    public PageResponse<PropertyDTO> getAllProperties(Pageable pageable) {
        System.out.println("getAllProperties");
        Page<PropertyDTO> page = propertyRepository.findAllPropertyDTO(pageable);
        System.out.println(page);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public List<PropertyDTO> getPropertyByUserId(User user) {
        Long userId = user.getId();

        return propertyRepository.findByUser_Id(userId).stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class))
                .toList();
    }

    public PageResponse<PropertyDTO> filterProperties(PropertyFilterDTO filterDTO
    ,Pageable pageable) {
        Page<Property> page = propertyRepository.findAll(PropertySpecification.filter(filterDTO), pageable);
        List<PropertyDTO> dtos = page.getContent().stream()
                .map(property -> {
                    PropertyDTO dto = modelMapper.map(property, PropertyDTO.class);

                    if (property.getUser() != null && property.getUser().getRole() != null) {
                        dto.setUserType(property.getUser().getRole());
                    }

                    return dto;
                })
                .toList();



         return new PageResponse<>(
                 dtos,
                 page.getNumber(),
                 page.getSize(),
                 page.getTotalElements(),
                 page.getTotalPages(),
                 page.isLast()
         );
    }
}
