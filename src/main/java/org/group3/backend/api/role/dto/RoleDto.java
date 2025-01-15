/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2024
 * @Time : 11:46 PM
 */
package org.group3.backend.api.role.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private Long id;
    private String name;
}
