package org.teamSmurfs.backend.api.react.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactRequest {

    @NotNull(message = "Entity ID is required.")
    @Min(value = 1, message = "Entity ID must be a positive number.")
    private Long entityId;

    @NotNull(message = "Entity Type is required.")
    @Min(value = 1, message = "Entity Type must be a positive number.")
    private Integer entityType;

    private String react;
}
