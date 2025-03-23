package org.teamSmurfs.backend.api.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.api.user.service.UserService;
import org.teamSmurfs.backend.config.service.MailService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private MailService mailService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldReturnSuccessResponse_WhenUserIsCreatedSuccessfully() throws Exception {
        // Arrange
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setName("John Doe");
        createUserRequest.setUsername("johndoe");
        createUserRequest.setRoleId(1L);

        when(request.getHeader("X-Request-Start-Time")).thenReturn("1000.0");
        doNothing().when(userService).createUser(any(CreateUserRequest.class));

        // Act
        ResponseEntity<ApiResponse> responseEntity = userController.createUser(createUserRequest, request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(201, responseEntity.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).getSuccess());

        assertEquals("User created successfully", responseEntity.getBody().getMessage());

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUserAlreadyExists() throws Exception {
        // Arrange
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("duplicate@example.com");

        doThrow(new RuntimeException("Email already exists")).when(userService).createUser(any(CreateUserRequest.class));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> 
            userController.createUser(createUserRequest, request)
        );

        assertEquals("Email already exists", exception.getMessage());
        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }
}
