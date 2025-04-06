package org.teamSmurfs.backend.features.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.teamSmurfs.backend.features.comment.dto.CommentDto;
import org.teamSmurfs.backend.features.react.dto.ReactDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto {

    private Long id;

    private Long authorId;

    private String authorName;

    private String content;

    private String title;

    private String createdAt;

    private String updatedAt;

    private List<ReactDto> reactList;

    private List<CommentDto> commentList;
}
