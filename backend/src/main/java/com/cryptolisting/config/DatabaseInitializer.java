package com.cryptolisting.config;

import com.cryptolisting.entity.AppUser;
import com.cryptolisting.entity.CryptoAsset;
import com.cryptolisting.entity.UserRole;
import com.cryptolisting.repository.AppUserRepository;
import com.cryptolisting.repository.CryptoAssetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner seedData(AppUserRepository userRepository,
                               CryptoAssetRepository cryptoAssetRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                AppUser admin = new AppUser("admin",
                        passwordEncoder.encode("admin123!"),
                        Set.of(UserRole.ROLE_ADMIN, UserRole.ROLE_USER));
                userRepository.save(admin);

                CryptoAsset bitcoin = new CryptoAsset(
                        "BTC",
                        "Bitcoin",
                        "Flagship cryptocurrency",
                        "Layer1",
                        BigDecimal.valueOf(500_000_000_000L),
                        OffsetDateTime.now().minusDays(30),
                        true,
                        admin
                );

                cryptoAssetRepository.save(bitcoin);
            }
        };
    }
}
