/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:41 PM
 */
package org.teamSmurfs.backend.api.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.teamSmurfs.backend.api.response.dto.PaginatedResponse;
import org.teamSmurfs.backend.api.role.model.Role;
import org.teamSmurfs.backend.api.role.model.RoleName;
import org.teamSmurfs.backend.api.role.repository.RoleRepository;
import org.teamSmurfs.backend.api.token.model.Token;
import org.teamSmurfs.backend.api.token.repository.TokenRepository;
import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.api.user.service.UserService;
import org.teamSmurfs.backend.api.user.utils.PasswordValidatorUtil;
import org.teamSmurfs.backend.api.user.utils.UserUtil;
import org.teamSmurfs.backend.config.exception.DuplicateEntityException;
import org.teamSmurfs.backend.config.utils.DtoUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.config.utils.EntityUtil;
import org.teamSmurfs.backend.security.service.JwtService;
import org.teamSmurfs.backend.security.utils.AuthUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUtil userUtil;
    private final TokenRepository tokenRepository;
    private final AuthUtil authUtil;

    @Override
    public Object retrieveUsers(int page, int limit) throws Exception {
        try {
            log.info("Fetching users from database with page: {}, limit: {}", page, limit);

            int offset = (page - 1) * limit;
            List<User> users = userRepository.findUsersWithPagination(offset, limit);

            long totalItems = userRepository.countUsers();
            int lastPage = (int) Math.ceil((double) totalItems / limit);

            List<UserDto> userList = (users == null) ? Collections.emptyList() : users.stream()
                    .map(user -> {
                        UserDto userDto = modelMapper.map(user, UserDto.class);
                        userDto.setRoleName(user.getRoles().stream()
                                .findFirst()
                                .map(role -> role.getName().name().replaceFirst("^ROLE_", ""))
                                .orElse(null));
                        return userDto;
                    })
                    .collect(Collectors.toList());

            log.info("Fetched {} users, total users in system: {}", users.size(), totalItems);

            return PaginatedResponse.<UserDto>builder()
                    .items(userList)
                    .totalItems(totalItems)
                    .lastPage(lastPage)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving users: {}", e.getMessage());
            throw new Exception("Error retrieving users: " + e.getMessage());
        }
    }

    @Override
    public Object createUser(CreateUserRequest createUserRequest) throws Exception {
        try {
            log.info("Creating new user with email: {}", createUserRequest.getEmail());

            if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent()) {
                log.warn("Email already exists: {}", createUserRequest.getEmail());
                throw new DuplicateEntityException("Email: " + createUserRequest.getEmail() + " is already in use");
            }

            Role userRole = EntityUtil.getEntityById(roleRepository, createUserRequest.getRoleId());
            log.info("Assigning role: {}", userRole.getName());

            User newUser = User.builder()
                    .name(createUserRequest.getName())
                    .username(userUtil.generateUniqueUsername(createUserRequest.getName()))
                    .email(createUserRequest.getEmail())
                    .password(passwordEncoder.encode(createUserRequest.getPassword()))
                    .roles(Set.of(userRole))
                    .build();

            userRepository.save(newUser);

            Map<String, Object> tokenData = authUtil.generateTokens(newUser, String.valueOf(userRole.getName()));

            String refreshToken = (String) tokenData.get("refreshToken");

            Instant expiredAt = Instant.now().plus(7, ChronoUnit.DAYS);

            Token token = Token.builder()
                    .user(newUser)
                    .refreshtoken(refreshToken)
                    .expiredAt(expiredAt)
                    .build();

            tokenRepository.save(token);

            log.info("User created successfully with ID: {}", newUser.getId());

            UserDto userDto = modelMapper.map(newUser, UserDto.class);
            userDto.setRoleName(newUser.getRoles().stream()
                    .findFirst()
                    .map(role -> role.getName().name().replaceFirst("^ROLE_", ""))
                    .orElse(null));
            return userDto;
        } catch (DuplicateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword, String authHeader) throws Exception {
        log.info("Initiating password change for authenticated user.");

        UserDto userDto = userUtil.getCurrentUserDto(authHeader);
        User currentUser = EntityUtil.getEntityById(userRepository, userDto.getId());

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            log.warn("Password change failed: Incorrect old password for user ID {}", currentUser.getId());
            throw new IllegalArgumentException("Incorrect old password.");
        }

        if (!PasswordValidatorUtil.isValid(newPassword)) {
            log.warn("Password change failed: Weak password provided.");
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);

        log.info("Password changed successfully for user ID {}", currentUser.getId());
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.countByUsername(username) > 0;
    }

    @Override
    public UserDto retrieveOne(Long id) {
        log.info("Fetching user details for ID: {}", id);

        User user = EntityUtil.getEntityById(userRepository, id);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        userDto.setRoleName(user.getRoles().stream()
                .findFirst()
                .map(role -> role.getName().name().replaceFirst("^ROLE_", ""))
                .orElse(null));

        log.info("Successfully retrieved user with ID: {}", id);

        return userDto;
    }

}
