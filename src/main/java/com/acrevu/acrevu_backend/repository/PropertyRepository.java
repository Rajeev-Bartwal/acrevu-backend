package com.acrevu.acrevu_backend.repository;

import com.acrevu.acrevu_backend.dto.PropertyDTO;
import com.acrevu.acrevu_backend.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByUser_Id(Long userId);

    @Query("""
    SELECT new com.acrevu.acrevu_backend.dto.PropertyDTO(
        p.id,
        u.role,
        p.listingType,
        p.propertyType,
        p.category,
        p.bhk,
        p.area,
        p.areaUnit,
        p.city,
        p.locality,
        p.address,
        p.pincode,
        p.floor,
        p.totalFloors,
        p.price,
        p.rent,
        p.deposit,
        p.bedrooms,
        p.bathrooms,
        p.balconies,
        p.furnishing,
        p.contactName,
        p.mobile,
        p.email,
        p.description,
        p.amenities
    )
    FROM Property p
    JOIN p.user u
    """)
    Page<PropertyDTO> findAllPropertyDTO(Pageable pageable);
}
