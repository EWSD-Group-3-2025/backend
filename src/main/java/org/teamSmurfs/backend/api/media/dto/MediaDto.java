package org.teamSmurfs.backend.api.media.dto;

import java.time.LocalDateTime;

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

	    private Long user_id;

	    private String fileUrl;

	    private LocalDateTime uploadedAt;

	    private Long entityId;

	    private Integer entityType;

	    private String fileType;

	    private LocalDateTime createdAt;

	    private LocalDateTime updatedAt;
}
