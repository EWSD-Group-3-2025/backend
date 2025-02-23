package org.teamSmurfs.backend.api.student.dto;

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
     private String name;
	 private String email;
	 private String username;
	 private String roleName;
	 private Long roleId;
	 private Long courseId;
	 private String course;
	 private Boolean status;
	 private LocalDateTime createdAt;
	 private Long allocateTutorId;
}
