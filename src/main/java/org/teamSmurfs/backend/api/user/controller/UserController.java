/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 12:00 AM
 */
package org.teamSmurfs.backend.api.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.dto.PaginatedResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.api.user.service.UserService;
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
}
