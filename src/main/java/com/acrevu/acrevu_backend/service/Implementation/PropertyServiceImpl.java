package com.acrevu.acrevu_backend.service.Implementation;

import com.acrevu.acrevu_backend.dto.PropertyDTO;
import com.acrevu.acrevu_backend.entity.Property;
import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.repository.PropertyRepository;
import com.acrevu.acrevu_backend.service.PropertyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        System.out.println("addProperty");
        Property property = modelMapper.map(propertyDTO, Property.class);
        property.setUser(user);
        property.setCreatedAt(LocalDateTime.now());
        propertyRepository.save(property);

        return modelMapper.map(property, PropertyDTO.class);
    }

    @Override
    public List<PropertyDTO> getAllProperties() {
        System.out.println("getAllProperties");
        List<Property> properties =  propertyRepository.findAll();
        List<PropertyDTO> propertyDTOS = new ArrayList<>();

        for(Property property : properties) {
            propertyDTOS.add(modelMapper.map(property, PropertyDTO.class));
        }

        return propertyDTOS;
    }


}
