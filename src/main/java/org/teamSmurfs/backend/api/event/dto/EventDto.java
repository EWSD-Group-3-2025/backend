package org.teamSmurfs.backend.api.event.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
	
	private Long id;
	
	private Long tutorId;
	
	private String tutorName;
	
	private String title;
	
	private String description;
	
	private LocalDateTime startdate;
	
	private LocalDateTime enddate;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
