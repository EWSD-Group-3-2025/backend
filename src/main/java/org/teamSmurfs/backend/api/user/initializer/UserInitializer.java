package org.teamSmurfs.backend.api.user.initializer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.role.model.Role;
import org.teamSmurfs.backend.api.role.model.RoleName;
import org.teamSmurfs.backend.api.role.repository.RoleRepository;
import org.teamSmurfs.backend.api.token.model.Token;
import org.teamSmurfs.backend.api.token.repository.TokenRepository;
import org.teamSmurfs.backend.api.user.model.Gender;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.utils.EntityUtil;
import org.teamSmurfs.backend.security.utils.AuthUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing default users...");

        Role adminRole = this.roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found."));

        if (this.userRepository.findByUsername("staff-1").isEmpty()) {
            User staffUser = User.builder()
                    .name("Admin Staff")
                    .username("staff-1")
                    .email("teamsmurfs@gmail.com")
                    .password(passwordEncoder.encode("password123"))
                    .gender(Gender.MALE.getValue())
                    .roles(Set.of(adminRole))
                    .build();
            this.userRepository.save(staffUser);
            log.info("Inserted default staff user: {}", staffUser.getUsername());

            Role userRole = EntityUtil.getEntityById(roleRepository, 2L);

            Map<String, Object> tokenData = this.authUtil.generateTokens(staffUser, String.valueOf(userRole.getName()));

            String refreshToken = (String) tokenData.get("refreshToken");

            Instant expiredAt = Instant.now().plus(7, ChronoUnit.DAYS);

            Token token = Token.builder()
                    .user(staffUser)
                    .refreshtoken(refreshToken)
                    .expiredAt(expiredAt)
                    .build();

            tokenRepository.save(token);
        }

        log.info("User initialization completed.");
    }
}
