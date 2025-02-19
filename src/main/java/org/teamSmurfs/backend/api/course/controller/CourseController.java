package org.teamSmurfs.backend.api.course.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.api.course.dto.CourseDto;
import org.teamSmurfs.backend.api.course.dto.CourseRequest;
import org.teamSmurfs.backend.api.course.dto.UpdateCourseRequest;
import org.teamSmurfs.backend.api.course.service.CourseService;
import org.teamSmurfs.backend.api.request.RequestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/${api.base.path}/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;
    
    @PostMapping
    public ResponseEntity<ApiResponse> createCourse(
            @Validated @RequestBody CourseRequest courseRequest,
            HttpServletRequest request) throws Exception {

    	log.info("Creating course with names: {}", Arrays.toString(courseRequest.getNames()));
        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        courseService.create(courseRequest);
        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("Course created successfully")
                .build();
        return ResponseUtil.buildResponse(request, successResponse ,requestStartTime);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> retrieveAllCourse(HttpServletRequest request) throws Exception {
        log.info("Retrieving all courses");
        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        List<CourseDto> courses = courseService.retrieveAll();
        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(courses != null ? courses : Collections.emptyList())
                .message("Courses retrieved successfully")
                .build();
        return ResponseUtil.buildResponse(request, successResponse , requestStartTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveCourse( @PathVariable(value = "id") final Long id,
    		HttpServletRequest request) throws Exception {
        log.info("Retrieving course with ID: {}", id);
        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        CourseDto courseDto = courseService.retrieveOne(id);
        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(courseDto)
                .message("Course retrieved successfully")
                .build();
        return ResponseUtil.buildResponse(request, successResponse ,requestStartTime);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(
    		@PathVariable(value = "id") final Long id,
    		@RequestBody UpdateCourseRequest updateCourseRequest,
            HttpServletRequest request) throws Exception {

    	log.info("Updating course with ID: {}", id);

        double requestStartTime = System.currentTimeMillis();
        CourseDto updatedCourse = courseService.update(id,updateCourseRequest);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(updatedCourse)
                .message("Course updated successfully")
                .build();
        return ResponseUtil.buildResponse(request, successResponse ,requestStartTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable(value = "id") final Long id, 
    		HttpServletRequest request) {
        log.info("Deleting course with ID: {}", id);
        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        courseService.delete(id);
        
        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Course deleted successfully")
                .build();
        return ResponseUtil.buildResponse(request, successResponse,requestStartTime);
    }
}
