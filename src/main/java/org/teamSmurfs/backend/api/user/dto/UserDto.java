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
    private String createdAt;
    private String updatedAt;
    private String roleName;

    private String permissions;

    private String department;

    private String specialization;

    private String course;
}
