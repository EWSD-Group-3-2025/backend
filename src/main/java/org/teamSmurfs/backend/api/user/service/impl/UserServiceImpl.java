/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:41 PM
 */
package org.teamSmurfs.backend.api.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.teamSmurfs.backend.api.response.dto.PaginatedResponse;
import org.teamSmurfs.backend.api.role.model.Role;
import org.teamSmurfs.backend.api.role.model.RoleName;
import org.teamSmurfs.backend.api.role.repository.RoleRepository;
import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.api.user.service.UserService;
import org.teamSmurfs.backend.config.exception.DuplicateEntityException;
import org.teamSmurfs.backend.config.utils.DtoUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Object retrieveUsers(int page, int limit) throws Exception {
        try {
            log.info("Fetching users from database with page: {}, limit: {}", page, limit);

            int offset = (page - 1) * limit;
            List<User> users = userRepository.findUsersWithPagination(offset, limit);

            long totalItems = userRepository.countUsers();
            int lastPage = (int) Math.ceil((double) totalItems / limit);

            List<UserDto> userList = DtoUtil.mapList(users, UserDto.class, modelMapper);

            log.info("Fetched {} users, total users in system: {}", users.size(), totalItems);

            return PaginatedResponse.<UserDto>builder()
                    .items(userList != null ? userList : Collections.emptyList())
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
                throw new DuplicateEntityException("Email already exists: " + createUserRequest.getEmail());
            }

            User user = modelMapper.map(createUserRequest, User.class);
            user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found in database!"));
            log.info("Assigning role: {}", userRole.getName());
            user.setRoles(Set.of(userRole));

            User savedUser = userRepository.save(user);

            log.info("User created successfully with ID: {}", savedUser.getId());

            return modelMapper.map(savedUser, UserDto.class);
        } catch (DuplicateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
