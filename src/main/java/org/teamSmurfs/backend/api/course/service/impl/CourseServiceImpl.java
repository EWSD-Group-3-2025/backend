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
import org.teamSmurfs.backend.api.student_course.model.StudentCourse;
import org.teamSmurfs.backend.api.student_course.repository.StudentCourseRepository;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.StaffRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.exception.EntityDeletionException;
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
    private final StudentCourseRepository studentCourseRepository;
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
        return repository.findAllByOrderByCreatedAtDesc().stream()
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
        Course course = EntityUtil.getEntityById(this.repository, id);

        List<StudentCourse> studentCourses = this.studentCourseRepository.findByCourseId(id);
        if (!studentCourses.isEmpty()) {
            throw new EntityDeletionException(
                    "Cannot delete Student because it is associated with " + studentCourses.size() + " Student(s).");
        }

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
        courseDto.setStaffName(EntityUtil.getEntityById(this.userRepository, course.getCreatedBy()).getName());
        return courseDto;
    }
}
