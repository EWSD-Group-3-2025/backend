package org.teamSmurfs.backend.api.media.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMediaRequest {
	
	@NotNull(message = "User ID is required.")
    @Min(value = 1, message = "User ID must be a positive number.")
	private Long userId;
	
	@NotNull(message = "User Name is required.")
	private String userName;
	
	@NotNull(message="File Url is required.")
	private String fileUrl ;
	
	@NotNull(message = "Entity Type is required.")
	private Integer entityType;
	
	@NotNull(message = "File Type is required.")
	private String fileType;
	
	@NotNull(message = "Store Name is required.")
	private String storedName;
	
	@NotNull(message = "StoreUUID is required.")
	private Long storedUUID;
	
	@NotNull(message = "title is required.")
	private String title;
	
	@NotNull(message = "description is required.")
	private String description;
}
