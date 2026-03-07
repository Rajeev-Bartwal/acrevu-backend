package com.acrevu.acrevu_backend.Specification;

import com.acrevu.acrevu_backend.dto.PropertyFilterDTO;
import com.acrevu.acrevu_backend.entity.Property;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class PropertySpecification {

    public static

    Specification<Property> filter(PropertyFilterDTO f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (f.getCity() != null && !f.getCity().isEmpty())
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + f.getCity().toLowerCase() + "%"));

            if (f.getLocality() != null && !f.getLocality().isEmpty())
                predicates.add(cb.like(cb.lower(root.get("locality")), "%" + f.getLocality().toLowerCase() + "%"));

            if (f.getPropertyType() != null && !f.getPropertyType().isEmpty())
                predicates.add(cb.equal(cb.lower(root.get("propertyType")), f.getPropertyType().toLowerCase()));

            if (f.getListingType() != null && !f.getListingType().isEmpty())
                predicates.add(cb.equal(cb.lower(root.get("listingType")), f.getListingType().toLowerCase()));

            if (f.getCategory() != null && !f.getCategory().isEmpty())
                predicates.add(cb.equal(cb.lower(root.get("category")), f.getCategory().toLowerCase()));

            if (f.getBhk() != null && !f.getBhk().isEmpty())
                predicates.add(cb.equal(root.get("bhk"), f.getBhk()));

            if (f.getFurnishing() != null && !f.getFurnishing().isEmpty())
                predicates.add(cb.equal(cb.lower(root.get("furnishing")), f.getFurnishing().toLowerCase()));

            if (f.getBedrooms() != null && !f.getBedrooms().isEmpty())
                predicates.add(cb.equal(root.get("bedrooms"), f.getBedrooms()));

            if (f.getBathrooms() != null && !f.getBathrooms().isEmpty())
                predicates.add(cb.equal(root.get("bathrooms"), f.getBathrooms()));

            if (f.getMinPrice() != null && !f.getMinPrice().isEmpty())
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("price").as(Long.class), Long.parseLong(f.getMinPrice())));

            if (f.getMaxPrice() != null && !f.getMaxPrice().isEmpty())
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("price").as(Long.class), Long.parseLong(f.getMaxPrice())));

            // Area Range
            if (f.getMinArea() != null && !f.getMinArea().isEmpty())
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("area").as(Double.class), Double.parseDouble(f.getMinArea())));

            if (f.getMaxArea() != null && !f.getMaxArea().isEmpty())
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("area").as(Double.class), Double.parseDouble(f.getMaxArea())));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
