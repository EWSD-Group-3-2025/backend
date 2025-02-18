/*
 * @Author : Thant Htoo Aung
 * @Date : 1/15/2025
 * @Time : 08:48 PM
 */
package org.teamSmurfs.backend.api.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Data Transfer Object for creating a new user.
 */
@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Username is required.")
    private String username;

    @NotNull(message = "Role ID is required.")
    @Min(value = 1, message = "Role ID must be a positive number.")
    private Long roleId;

    private String permissions;

    private String department;

    private String specialization;

    private String course;

    private Long departmentId;
}
