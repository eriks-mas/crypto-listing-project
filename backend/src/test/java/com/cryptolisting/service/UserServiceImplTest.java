package com.cryptolisting.service;

import com.cryptolisting.dto.UserRegistrationRequest;
import com.cryptolisting.entity.AppUser;
import com.cryptolisting.entity.UserRole;
import com.cryptolisting.exception.DuplicateResourceException;
import com.cryptolisting.exception.ResourceNotFoundException;
import com.cryptolisting.repository.AppUserRepository;
import com.cryptolisting.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequest request;

    @BeforeEach
    void setUp() {
        request = new UserRegistrationRequest();
        request.setUsername("alice");
        request.setPassword("password123");
    }

    @Test
    void registerShouldPersistUser() {
        when(appUserRepository.existsByUsernameIgnoreCase("alice")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(appUserRepository.save(any(AppUser.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AppUser user = userService.register(request);

        assertThat(user.getUsername()).isEqualTo("alice");
        assertThat(user.getPassword()).isEqualTo("encoded");
        assertThat(user.getRoles()).contains(UserRole.ROLE_USER);
        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    void registerShouldFailWhenDuplicate() {
        when(appUserRepository.existsByUsernameIgnoreCase("alice")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void findByUsernameShouldReturnUser() {
        AppUser user = new AppUser("alice", "encoded", Set.of(UserRole.ROLE_USER));
        when(appUserRepository.findByUsernameIgnoreCase("alice")).thenReturn(Optional.of(user));

        AppUser result = userService.findByUsername("alice");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void findByUsernameShouldThrowWhenMissing() {
        when(appUserRepository.findByUsernameIgnoreCase("alice")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("alice"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
