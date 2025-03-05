package org.teamSmurfs.backend.api.react.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactDto {
    private String authorName;

    private String react;

    private Long entityId;

    private Integer entityType;

    private String createdAt;

    private String updatedAt;
}
