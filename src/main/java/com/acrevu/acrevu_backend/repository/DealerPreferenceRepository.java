package com.acrevu.acrevu_backend.repository;

import com.acrevu.acrevu_backend.entity.DealerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerPreferenceRepository
        extends JpaRepository<DealerPreference, Long> {
}
