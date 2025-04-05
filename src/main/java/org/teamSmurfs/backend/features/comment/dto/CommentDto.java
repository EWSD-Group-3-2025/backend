package org.teamSmurfs.backend.features.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    private long commenterId;

    private String commenterName;

    private String commentText;

    private String createdAt;

    private String updatedAt;
}
