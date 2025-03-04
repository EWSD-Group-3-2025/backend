package org.teamSmurfs.backend.api.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto {

    private Long id;

    private String authorName;

    private String content;

    private String title;

    private String createdAt;

    private String updatedAt;
}
