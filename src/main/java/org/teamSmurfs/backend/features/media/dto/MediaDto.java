package org.teamSmurfs.backend.features.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
	
	    private Long id;
	    
	    private Long userId;
	    
	    private String userName;

	    private String fileUrl;

	    private String uploadedAt;

	    private Integer entityType;

	    private String fileType;
	    
	    private String storedName; 
	    
	    private String storedUUID; 
	    
	    private String title;
	    
	    private String description;

	    private String createdAt;

	    private String updatedAt;
}
