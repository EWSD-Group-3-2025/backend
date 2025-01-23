/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 12:00 AM
 */
package org.group3.backend.api.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.group3.backend.api.request.RequestUtils;
import org.group3.backend.api.response.dto.ApiResponse;
import org.group3.backend.api.response.dto.PaginatedResponse;
import org.group3.backend.api.response.utils.ResponseUtil;
import org.group3.backend.api.user.dto.CreateUserRequest;
import org.group3.backend.api.user.service.UserService;
import org.group3.backend.config.utils.PaginationMetaUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/${api.base.path}/users")
@RequiredArgsConstructor
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

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        Object createdUser = userService.createUser(createUserRequest);

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

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        Object paginatedUsers = userService.retrieveUsers(page, limit);

        Map<String, Object> meta = PaginationMetaUtil.buildPaginationMeta(request, page, limit, paginatedUsers);

        Object data = (paginatedUsers instanceof PaginatedResponse)
                ? ((PaginatedResponse<?>) paginatedUsers).getItems()
                : Collections.emptyList();

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(data != null ? data : Collections.emptyList())
                .meta(meta)
                .message("Users retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
