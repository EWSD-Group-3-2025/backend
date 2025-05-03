package org.teamSmurfs.backend.features.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryRequest {
    @NotBlank(message = "Book name is required.")
    private String bookName;

    @NotNull(message = "Role ID is required.")
    @Min(value = 1, message = "Role ID must be a positive number.")
    private Integer categoryId;

    @NotBlank(message = "Difficulty Level is required.")
    private String difficultyLevel;

    @NotNull(message = "Role ID is required.")
    @Min(value = 1, message = "Role ID must be a positive number.")
    private Double rating;

    @NotBlank(message = "Organization Name is required.")
    private String organizationName;

    @NotBlank(message = "Organization URL is required.")
    private String organizationUrl;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotBlank(message = "Book URL is required.")
    private String bookUrl;
}
