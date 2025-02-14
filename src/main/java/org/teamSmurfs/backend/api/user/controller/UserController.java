/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 12:00 AM
 */
package org.teamSmurfs.backend.api.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.dto.PaginatedResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.api.user.dto.ChangePasswordRequest;
import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.service.UserService;
import org.teamSmurfs.backend.api.user.utils.PasswordValidatorUtil;
import org.teamSmurfs.backend.config.utils.PaginationMetaUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${api.base.path}/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user with the provided details.
     *
     * @param createUserRequest the request payload containing user details.
     * @param request           the HTTP servlet request for additional context.
     * @return a ResponseEntity containing the result of the user creation process.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(
            @Validated @RequestBody CreateUserRequest createUserRequest,
            HttpServletRequest request
    ) throws Exception {

        log.info("Creating new user with email: {}", createUserRequest.getEmail());

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        Object createdUser = userService.createUser(createUserRequest);

        log.info("User created successfully: {}", createUserRequest.getEmail());

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(createdUser)
                .message("User created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    /**
     * Retrieves all users.
     *
     * @param request the HTTP servlet request for additional context.
     * @param page    the current page number (default is 1).
     * @param limit   the number of items per page (default is 10).
     * @return a ResponseEntity containing the list of users.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> retrieveUsers(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) throws Exception {

        log.info("Retrieving users - Page: {}, Limit: {}", page, limit);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        Object paginatedUsers = userService.retrieveUsers(page, limit);

        Map<String, Object> meta = PaginationMetaUtil.buildPaginationMeta(request, page, limit, paginatedUsers);

        Object data = (paginatedUsers instanceof PaginatedResponse)
                ? ((PaginatedResponse<?>) paginatedUsers).getItems()
                : Collections.emptyList();

        log.info("Retrieved {} users successfully", (data != null) ? ((List<?>) data).size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(data != null ? data : Collections.emptyList())
                .meta(meta)
                .message("Users retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request,
            @RequestHeader("Authorization") String authHeader) throws Exception {

        log.info("Password change request received for authenticated user.");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        if (!PasswordValidatorUtil.isValid(changePasswordRequest.getNewPassword())) {
            log.warn("Password change failed: Weak password attempt.");
            return ResponseUtil.buildResponse(request, ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.BAD_REQUEST.value())
                    .data(false)
                    .message("New password must be at least 8 characters long and include uppercase, lowercase, a number, and a special character.")
                    .build(), requestStartTime);
        }

        userService.changePassword(changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword(), authHeader);

        log.info("Password changed successfully.");

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Password changed successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse> checkUsernameExists(
            @RequestParam("username") String username, HttpServletRequest request) {

        log.info("Checking existence of username: {}", username);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        boolean exists = userService.usernameExists(username);

        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("username", username, "exists", exists))
                .message(exists ? "Username already taken" : "Username available")
                .build();

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveUserById(
            @PathVariable("id") final Long id, HttpServletRequest request) {

        log.info("Retrieving user with id: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        UserDto userDto = userService.retrieveOne(id);

        log.info("User retrieved successfully: {}", userDto.getUsername());

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(userDto)
                .message("User retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(
    		@PathVariable Long id ,HttpServletRequest request) {

        	double requestStartTime = RequestUtils.extractRequestStartTime(request);
            boolean statusUpdated = userService.deleteUserById(id);
            
            if (statusUpdated) {
            	ApiResponse response = ApiResponse.builder()
                        .success(1)
                        .code(HttpStatus.OK.value())
                        .data(true)
                        .message("User Status Updated Successfully")
                        .build();
            	return ResponseUtil.buildResponse(request, response, requestStartTime);
            } else {
            	ApiResponse response = ApiResponse.builder()
                        .success(0)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .data(false)
                        .message("User Status Update Fail")
                        .build();
            	return ResponseUtil.buildResponse(request, response, requestStartTime);
            }
        }
}
