/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:40 PM
 */
package org.teamSmurfs.backend.api.user.service;

import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.api.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> retrieveUsers() throws Exception;

    Object createUser(CreateUserRequest createUserRequest) throws Exception;

    void changePassword(String oldPassword, String newPassword, String authHeader) throws Exception;

    boolean usernameExists(String username);

    UserDto retrieveOne(Long id);

	boolean deleteUserById(Long id);
}
