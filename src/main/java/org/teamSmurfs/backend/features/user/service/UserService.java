/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:40 PM
 */
package org.teamSmurfs.backend.features.user.service;

import org.teamSmurfs.backend.features.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.features.user.dto.UpdateUserRequest;

import java.util.List;

public interface UserService {
    List<Object> retrieveUsers(final String role) throws Exception;

    void createUser(CreateUserRequest createUserRequest) throws Exception;

    void updateUser(Long userId, UpdateUserRequest updateUserRequest) throws Exception;

    void changePassword(String oldPassword, String newPassword, String authHeader) throws Exception;

    boolean usernameExists(String username);

    Object retrieveOne(Long id);

	boolean deleteUserById(Long id);

    int retrieveUserNameCount(String name);

    void resetPassword(final Long id);
}
