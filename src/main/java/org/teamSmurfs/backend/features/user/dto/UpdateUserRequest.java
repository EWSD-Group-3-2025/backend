package org.teamSmurfs.backend.features.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private String name;
    private String email;

    private Long roleId;

    private boolean admin;

    private Long departmentId;

    private Long specializationId;

    private Long courseId;

    private boolean status;
}
