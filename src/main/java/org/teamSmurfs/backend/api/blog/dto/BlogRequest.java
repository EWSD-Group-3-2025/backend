package org.teamSmurfs.backend.api.blog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {

    @NotNull(message = "Author ID is required.")
    @Min(value = 1, message = "Author ID must be a positive number.")
    private Long authorId;

    @NotBlank(message = "Content is required.")
    private String content;

    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters.")
    private String title;
}
