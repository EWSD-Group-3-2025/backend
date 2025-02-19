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
import org.teamSmurfs.backend.api.user.model.User;;
import org.teamSmurfs.backend.api.user.repository.UserRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing default users...");

        Role staffRole = this.roleRepository.findByName(RoleName.ROLE_STAFF)
                .orElseThrow(() -> new RuntimeException("Role STAFF not found."));

        if (this.userRepository.findByUsername("staff1").isEmpty()) {
            User staffUser = User.builder()
                    .name("Admin Staff")
                    .username("staff1")
                    .email("teamsmurfs@gmail.com")
                    .password(passwordEncoder.encode("password123"))
                    .roles(Set.of(staffRole))
                    .build();
            this.userRepository.save(staffUser);
            log.info("Inserted default staff user: {}", staffUser.getUsername());
        }

        log.info("User initialization completed.");
    }
}
