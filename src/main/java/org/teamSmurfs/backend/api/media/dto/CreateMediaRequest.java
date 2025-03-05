package org.teamSmurfs.backend.api.media.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMediaRequest {
	
	@NotNull(message = "User ID is required.")
    @Min(value = 1, message = "User ID must be a positive number.")
	private Long user_id;
	
	@NotNull(message="File Url is required.")
	private String fileUrl ;
	
	@NotNull(message = "Entity Type is required.")
	private Integer entityType;
	
	@NotNull(message = "File Type is required.")
	private String fileType;	
}
