package com.cryptolisting.controller;

import com.cryptolisting.dto.AuthRequest;
import com.cryptolisting.dto.AuthResponse;
import com.cryptolisting.dto.UserRegistrationRequest;
import com.cryptolisting.entity.AppUser;
import com.cryptolisting.service.TokenService;
import com.cryptolisting.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser user = userService.findByUsername(request.getUsername());
        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        AppUser user = userService.register(request);
        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())));
    }
}
