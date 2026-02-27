package com.acrevu.acrevu_backend.repository;

import com.acrevu.acrevu_backend.entity.RegisterUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisterRepo extends JpaRepository<RegisterUser, Integer> {

    Optional<RegisterUser> findByEmail(String email);
}
