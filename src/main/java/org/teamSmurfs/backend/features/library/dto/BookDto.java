package org.teamSmurfs.backend.features.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String bookName;
    private Integer categoryId;
    private String categoryName;
    private String difficultyLevel;
    private Integer rating;
    private String organizationName;
    private String organizationUrl;
    private String description;
    private String bookUrl;
    private Long uploaderId;
    private String uploaderName;
    private String createdAt;
    private String updatedAt;
}
