package com.cryptolisting.service.impl;

import com.cryptolisting.dto.UserRegistrationRequest;
import com.cryptolisting.entity.AppUser;
import com.cryptolisting.entity.UserRole;
import com.cryptolisting.exception.DuplicateResourceException;
import com.cryptolisting.exception.ResourceNotFoundException;
import com.cryptolisting.repository.AppUserRepository;
import com.cryptolisting.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser register(UserRegistrationRequest request) {
        if (appUserRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new DuplicateResourceException("Username %s is already taken".formatted(request.getUsername()));
        }

        AppUser user = new AppUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Set.of(UserRole.ROLE_USER)
        );
        return appUserRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User %s not found".formatted(username)));
    }
}
