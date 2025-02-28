package org.teamSmurfs.backend.api.event.dto;

import java.time.LocalDateTime;

import org.teamSmurfs.backend.api.user.model.Tutor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateEventRequest {
	
	@NotNull(message = "Tutor ID is required.")
	private Long tutorId;
	
	@NotBlank(message = "Title is required.")
	private String title;
	
	@NotBlank(message = "Description is required.")
	private String description;
	
	@NotNull(message = "Start date is required.")
	private LocalDateTime startdate;
	
	@NotNull(message = "End date is required.")
	private LocalDateTime enddate;
}
