package org.teamSmurfs.backend.api.department.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDepartmentRequest {
    private String name;
    private Long staffId;
}
