package org.teamSmurfs.backend.api.course.service;

import org.teamSmurfs.backend.api.course.dto.CourseRequest;
import org.teamSmurfs.backend.api.course.dto.UpdateCourseRequest;
import org.teamSmurfs.backend.api.course.dto.CourseDto;

import java.util.List;

public interface CourseService {
    void create(final CourseRequest createCourseRequest);
    List<CourseDto> retrieveAll();
    CourseDto retrieveOne(final Long id);
    CourseDto update(final Long id, final UpdateCourseRequest updateCourseRequest);
    void delete(final Long id);
}