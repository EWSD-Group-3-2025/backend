package org.teamSmurfs.backend.api.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  CourseRequest {
    private String[] names;
    private Long staffId;
}
