package org.teamSmurfs.backend.api.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
	private Long id;
    private String name;
    private String email;
    private String username;
    private String roleName;
    private Long roleId;
    private String departmentName;
    private Long departmentId;
    private Boolean status;
    private LocalDateTime createdAt;
    private boolean admin;
}
