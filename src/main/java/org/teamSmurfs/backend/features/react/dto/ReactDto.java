package org.teamSmurfs.backend.features.react.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactDto {
    private Long authorId;

    private String authorName;

    private String react;

    private Long entityId;

    private Integer entityType;

    private String createdAt;

    private String updatedAt;
}
