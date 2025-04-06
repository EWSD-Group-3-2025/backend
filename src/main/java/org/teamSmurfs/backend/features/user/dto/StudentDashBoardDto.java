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
public class StudentDashBoardDto {
	 private Long id;
	 private Long studentId;
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
	 private boolean inactive;
	 private long inactiveDays;
}
