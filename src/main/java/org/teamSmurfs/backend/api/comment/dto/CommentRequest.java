package org.teamSmurfs.backend.api.comment.dto;

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
public class CommentRequest {

    @NotNull(message = "Blog ID is required.")
    @Min(value = 1, message = "Blog ID must be a positive number.")
    private Long blogId;

    @NotBlank(message = "Comment is required.")
    private String commentText;
}
