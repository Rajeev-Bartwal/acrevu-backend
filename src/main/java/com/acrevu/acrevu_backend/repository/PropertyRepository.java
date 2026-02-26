package com.acrevu.acrevu_backend.repository;

import com.acrevu.acrevu_backend.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
}
