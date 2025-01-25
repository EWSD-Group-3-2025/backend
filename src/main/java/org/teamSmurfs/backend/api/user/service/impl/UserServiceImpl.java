/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:41 PM
 */
package org.teamSmurfs.backend.api.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.teamSmurfs.backend.api.response.dto.PaginatedResponse;
import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.api.user.service.UserService;
import org.teamSmurfs.backend.config.utils.DtoUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Object retrieveUsers(int page, int limit) throws Exception {
        try {
            int offset = (page - 1) * limit;
            List<User> users = userRepository.findUsersWithPagination(offset, limit);

            long totalItems = userRepository.countUsers();
            int lastPage = (int) Math.ceil((double) totalItems / limit);

            List<UserDto> userList = DtoUtil.mapList(users, UserDto.class, modelMapper);

            return PaginatedResponse.<UserDto>builder()
                    .items(userList != null ? userList : Collections.emptyList())
                    .totalItems(totalItems)
                    .lastPage(lastPage)
                    .build();
        } catch (Exception e) {
            throw new Exception("Error retrieving users: " + e.getMessage());
        }
    }

    @Override
    public Object createUser(CreateUserRequest createUserRequest) throws Exception {
        try {
            User user = modelMapper.map(createUserRequest, User.class);
            user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
            User savedUser = userRepository.save(user);
            return modelMapper.map(savedUser, UserDto.class);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
