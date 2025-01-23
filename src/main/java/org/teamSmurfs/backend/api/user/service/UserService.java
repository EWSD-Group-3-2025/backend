/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:40 PM
 */
package org.teamSmurfs.backend.api.user.service;

import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;

public interface UserService {
    Object retrieveUsers(int page, int limit) throws Exception;

    Object createUser(CreateUserRequest createUserRequest) throws Exception;
}
