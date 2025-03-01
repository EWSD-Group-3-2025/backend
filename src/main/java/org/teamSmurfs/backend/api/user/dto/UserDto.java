/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:39 PM
 */
package org.teamSmurfs.backend.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String username;

    private String roleName;
    private Long roleId;

    private String department;
    private Long departmentId;

    private String specialization;
    private Long specializationId;

    private Long courseId;
    private String course;

    private boolean status;
    private boolean firstTimeLogin;
}
