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
public class StudentDto {
	 private Long id;
	 private Long userId;
     private String name;
	 private String email;
	 private String username;
	 private String roleName;
	 private Long roleId;
	 private Long courseId;
	 private String courseName;
	 private Boolean status;
	 private LocalDateTime createdAt;
	 private Long allocateTutorId;
	 private Integer gender;
}
