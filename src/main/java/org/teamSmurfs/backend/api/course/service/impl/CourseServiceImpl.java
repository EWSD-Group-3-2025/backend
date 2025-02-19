package org.teamSmurfs.backend.api.course.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.course.dto.CourseRequest;
import org.teamSmurfs.backend.api.course.dto.UpdateCourseRequest;
import org.teamSmurfs.backend.api.course.dto.CourseDto;
import org.teamSmurfs.backend.api.course.model.Course;
import org.teamSmurfs.backend.api.course.repository.CourseRepository;
import org.teamSmurfs.backend.api.course.service.CourseService;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.StaffRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(final CourseRequest createCourseRequest) {
        checkUserExists(createCourseRequest.getStaffId());
        List<Course> courses = Arrays.stream(createCourseRequest.getNames())
                .map(name -> new Course(name, createCourseRequest.getStaffId()))
                .toList();
        this.repository.saveAll(courses);
    }

    @Override
    public List<CourseDto> retrieveAll() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public CourseDto retrieveOne(final Long id) {
        return mapToDto(EntityUtil.getEntityById(this.repository, id));
    }

    @Override
    public CourseDto update(final Long id, final UpdateCourseRequest updateCourseRequest) {
        final Course existCourse = EntityUtil.getEntityById(this.repository, id);
        if (updateCourseRequest.getName() != null && !existCourse.getName().equals(updateCourseRequest.getName())) {
        	existCourse.setName(updateCourseRequest.getName());
        }
        if (updateCourseRequest.getStaffId() != null && !existCourse.getCreatedBy().equals(updateCourseRequest.getStaffId())) {
            checkUserExists(updateCourseRequest.getStaffId());
            existCourse.setCreatedBy(id);
        }
        return mapToDto(this.repository.save(existCourse));
    }

    @Override
    public void delete(final Long id) {
        EntityUtil.deleteEntity(this.repository, id, "Course");
    }

    private void checkUserExists(final Long userId) {
        User user = EntityUtil.getEntityById(this.userRepository, userId);
        if (!this.staffRepository.existsByUserId(user.getId())) {
            throw new EntityNotFoundException("Staff not found for User ID: " + user.getId());
        }
    }

    private CourseDto mapToDto(final Course course) {
        CourseDto courseDto = modelMapper.map(course, CourseDto.class);
        courseDto.setCreated_by(EntityUtil.getEntityById(this.userRepository, course.getCreatedBy()).getName());
        return courseDto;
    }
}
