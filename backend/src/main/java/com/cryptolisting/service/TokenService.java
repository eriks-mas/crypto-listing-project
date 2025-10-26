package com.cryptolisting.service;

import com.cryptolisting.entity.AppUser;

public interface TokenService {

    String generateToken(AppUser user);

    String extractUsername(String token);

    boolean isTokenValid(String token, AppUser user);
}
