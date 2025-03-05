package org.teamSmurfs.backend.api.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMediaRequest {

    private String fileUrl;

    private Integer entityType;

    private String fileType;
}
