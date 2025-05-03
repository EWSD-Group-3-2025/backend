package org.teamSmurfs.backend.features.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorDto {
	private Long id;
    private Long tutorId;
    private String name;
    private String email;
    private String username;
    private String roleName;
    private Long roleId;
    private String specializationName;
    private Long specializationId;
    private Boolean status;
    private LocalDateTime createdAt;
    private Integer gender;
    private String genderName;
}
