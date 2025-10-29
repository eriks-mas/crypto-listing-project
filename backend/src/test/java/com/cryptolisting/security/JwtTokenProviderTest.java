package com.cryptolisting.security;

import com.cryptolisting.entity.AppUser;
import com.cryptolisting.entity.UserRole;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private static final String SECRET = "VEVTVF9TRUNSRVRfVEhBVF9JUl9UT19LRUVQX0lUX1NFQ1JFVA==";

    private final JwtTokenProvider tokenProvider = new JwtTokenProvider(SECRET, 3600);

    @Test
    void shouldGenerateAndValidateToken() {
        AppUser user = new AppUser("alice", "password", Set.of(UserRole.ROLE_USER));

        String token = tokenProvider.generateToken(user);

        assertThat(token).isNotBlank();
        assertThat(tokenProvider.extractUsername(token)).isEqualTo("alice");
        assertThat(tokenProvider.isTokenValid(token, user)).isTrue();
    }

    @Test
    void shouldInvalidateTokenForDifferentUser() {
        AppUser alice = new AppUser("alice", "password", Set.of(UserRole.ROLE_USER));
        AppUser bob = new AppUser("bob", "password", Set.of(UserRole.ROLE_USER));

        String token = tokenProvider.generateToken(alice);

        assertThat(tokenProvider.isTokenValid(token, bob)).isFalse();
    }
}
