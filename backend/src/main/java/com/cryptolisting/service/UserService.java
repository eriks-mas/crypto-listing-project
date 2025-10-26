package com.cryptolisting.service;

import com.cryptolisting.dto.UserRegistrationRequest;
import com.cryptolisting.entity.AppUser;

public interface UserService {

    AppUser register(UserRegistrationRequest request);

    AppUser findByUsername(String username);
}
