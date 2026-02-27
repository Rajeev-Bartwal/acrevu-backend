package com.acrevu.acrevu_backend.security;

import com.acrevu.acrevu_backend.entity.User;
import com.acrevu.acrevu_backend.enums.UserStatus;
import com.acrevu.acrevu_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        User user;

        // Check if mobile (10 digits)
        if (identifier.matches("\\d{10}")) {
            user = userRepo.findByMobileNumber(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile"));
        }
        // Otherwise treat as email
        else {
            user = userRepo.findByEmail(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email"));
        }
        return user;
    }
}
