/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:41 PM
 */
package org.teamSmurfs.backend.api.user.service.impl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.teamSmurfs.backend.api.course.model.Course;
import org.teamSmurfs.backend.api.course.repository.CourseRepository;
import org.teamSmurfs.backend.api.department.model.Department;
import org.teamSmurfs.backend.api.department.repository.DepartmentRepository;
import org.teamSmurfs.backend.api.role.model.Role;
import org.teamSmurfs.backend.api.role.model.RoleName;
import org.teamSmurfs.backend.api.role.repository.RoleRepository;
import org.teamSmurfs.backend.api.specialization.model.Specialization;
import org.teamSmurfs.backend.api.specialization.repository.SpecializationRepository;
import org.teamSmurfs.backend.api.student_course.model.StudentCourse;
import org.teamSmurfs.backend.api.student_course.repository.StudentCourseRepository;
import org.teamSmurfs.backend.api.token.model.Token;
import org.teamSmurfs.backend.api.token.repository.TokenRepository;
import org.teamSmurfs.backend.api.user.dto.CreateUserRequest;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.dto.UserMapper;
import org.teamSmurfs.backend.api.user.model.*;
import org.teamSmurfs.backend.api.user.repository.*;
import org.teamSmurfs.backend.api.user.service.UserService;
import org.teamSmurfs.backend.api.user.utils.PasswordGeneratorUtil;
import org.teamSmurfs.backend.api.user.utils.PasswordValidatorUtil;
import org.teamSmurfs.backend.api.user.utils.UserUtil;
import org.teamSmurfs.backend.config.exception.DuplicateEntityException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.config.service.MailService;
import org.teamSmurfs.backend.config.utils.EntityUtil;
import org.teamSmurfs.backend.security.utils.AuthUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUtil userUtil;
    private final TokenRepository tokenRepository;
    private final AuthUtil authUtil;
    private final DepartmentRepository departmentRepository;
    private final SpecializationRepository specializationRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final CourseRepository courseRepository;
    private final MailService mailService;

    @Override
    public List<Object> retrieveUsers(final String role) throws Exception {
        try {
            log.info("Fetching all users from the database with role {}", role);

            List<User> users;

            if (role.equalsIgnoreCase("all")) {
                users = userRepository.findAll();
            } else {
                String roleNameString = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                users = userRepository.findByRoleName(roleNameString.toUpperCase());
            }

            if (users.isEmpty()) {
                log.warn("No users found in the database");
                return Collections.emptyList();
            }

            List<Object> userDtos = users.stream().map(userMapper::mapToDto).collect(Collectors.toList());

            log.info("Successfully retrieved {} users", userDtos.size());
            return userDtos;

        } catch (Exception e) {
            log.error("Error retrieving users", e);
            throw new Exception("Error retrieving users", e);
        }
    }

    @Override
    public Object createUser(CreateUserRequest createUserRequest) throws Exception {
        try {
            log.info("Creating new user with email: {}", createUserRequest.getEmail());

            if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent()) {
                log.warn("Email already exists: {}", createUserRequest.getEmail());
                throw new DuplicateEntityException("Email: " + createUserRequest.getEmail() + " is already in use");
            }

            Role userRole = EntityUtil.getEntityById(roleRepository, createUserRequest.getRoleId());
            log.info("Assigning role: {}", userRole.getName());

            String rawPassword = PasswordGeneratorUtil.generatePassword();

            User newUser = User.builder()
                    .name(createUserRequest.getName())
                    .username(createUserRequest.getUsername())
                    .email(createUserRequest.getEmail())
                    .password(passwordEncoder.encode(rawPassword))
                    .roles(Set.of(userRole))
                    .build();

            userRepository.save(newUser);
            UserDto userDto = modelMapper.map(newUser, UserDto.class);

            if (userRole.getName().equals(RoleName.ROLE_ADMIN)) {
                Staff newStaff = new Staff();
                newStaff.setUser(newUser);
                Department department = EntityUtil.getEntityById(this.departmentRepository, createUserRequest.getDepartmentId());
                newStaff.setDepartment(department);
                newStaff.setAdmin(true);
                staffRepository.save(newStaff);
                userDto.setDepartment(department.getName());

            }
            else if (userRole.getName().equals(RoleName.ROLE_STAFF)) {
                Staff newStaff = new Staff();
                newStaff.setUser(newUser);
                Department department = EntityUtil.getEntityById(this.departmentRepository, createUserRequest.getDepartmentId());
                newStaff.setDepartment(department);
                staffRepository.save(newStaff);
                userDto.setDepartment(department.getName());
            }
            else if (userRole.getName().equals(RoleName.ROLE_TUTOR)) {
                Tutor newTutor = new Tutor();
                newTutor.setUser(newUser);
                Specialization specialization = specializationRepository.findById(createUserRequest.getSpecializationId())
                        .orElseThrow(() -> new RuntimeException("Specialization not found"));

                newTutor.setSpecializations(Set.of(specialization)); // If you want to map the Specialization as an entity.

                tutorRepository.save(newTutor);

                userDto.setSpecialization(specialization.getName());

            }
            else {
                Student newStudent = new Student();
                newStudent.setUser(newUser);
                studentRepository.save(newStudent);

                StudentCourse newStudentCourse = new StudentCourse();
                newStudentCourse.setStudentId(newStudent.getId());
                newStudentCourse.setCourseId(createUserRequest.getCourseId());
                studentCourseRepository.save(newStudentCourse);

                Optional<Course> course = courseRepository.findById(newStudentCourse.getCourseId());
                userDto.setCourse(course.map(Course::getName).orElse("Unknown Course"));
            }

            Map<String, Object> tokenData = authUtil.generateTokens(newUser, String.valueOf(userRole.getName()));

            String refreshToken = (String) tokenData.get("refreshToken");

            Instant expiredAt = Instant.now().plus(7, ChronoUnit.DAYS);

            Token token = Token.builder()
                    .user(newUser)
                    .refreshtoken(refreshToken)
                    .expiredAt(expiredAt)
                    .build();

            tokenRepository.save(token);

            this.mailService.sendUserCredentialsEmail(newUser.getEmail(), rawPassword);

            log.info("User created successfully with ID: {}", newUser.getId());

            userDto.setRoleName(newUser.getRoles().stream()
                    .findFirst()
                    .map(role -> role.getName().name().replaceFirst("^ROLE_", ""))
                    .orElse(null));
            return userDto;
        } catch (DuplicateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

//    @Override
//    public Object updateUser(Long userId, UserDto userDto) throws Exception {
//        try {
//            log.info("Updating user with ID: {}", userId);
//
//            User existingUser = userRepository.findById(userId)
//                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
//
//            // Check if email is being updated and already exists
//            if (!existingUser.getEmail().equals(userDto.getEmail()) && userRepository.findByEmail(userDto.getEmail()).isPresent()) {
//                log.warn("Email already exists: {}", userDto.getEmail());
//                throw new DuplicateEntityException("Email: " + userDto.getEmail() + " is already in use");
//            }
//
//            // Update user properties
//            existingUser.setName(userDto.getName());
//            existingUser.setUsername(userDto.getUsername());
//            existingUser.setEmail(userDto.getEmail());
//
//            Role updatedRole = EntityUtil.getEntityById(roleRepository, userDto.getRoleId());
//            existingUser.setRoles(Set.of(updatedRole));
//
//            userRepository.save(existingUser);
//
//            // Convert updated user to DTO
//            UserDto updatedUserDto = modelMapper.map(existingUser, UserDto.class);
//
//            // Handle role-based updates
//            if (updatedRole.getName().equals(RoleName.ROLE_ADMIN)) {
//                Staff staff = staffRepository.findByUser(existingUser)
//                        .orElseGet(() -> new Staff()); // Create if not exists
//
//                staff.setUser(existingUser);
//                Department department = EntityUtil.getEntityById(departmentRepository, userDto.getDepartmentId());
//                staff.setDepartment(department);
//                staff.setAdmin(true);
//                staffRepository.save(staff);
//                updatedUserDto.setDepartment(department.getName());
//            }
//            else if (updatedRole.getName().equals(RoleName.ROLE_STAFF)) {
//                Staff staff = staffRepository.findByUser(existingUser)
//                        .orElseGet(() -> new Staff());
//
//                staff.setUser(existingUser);
//                Department department = EntityUtil.getEntityById(departmentRepository, userDto.getDepartmentId());
//                staff.setDepartment(department);
//                staff.setAdmin(false);
//                staffRepository.save(staff);
//                updatedUserDto.setDepartment(department.getName());
//            }
//            else if (updatedRole.getName().equals(RoleName.ROLE_TUTOR)) {
//                Tutor tutor = tutorRepository.findByUser(existingUser)
//                        .orElseGet(() -> new Tutor());
//
//                tutor.setUser(existingUser);
//                Specialization specialization = specializationRepository.findById(userDto.getSpecializationId())
//                        .orElseThrow(() -> new RuntimeException("Specialization not found"));
//
//                tutor.setSpecializations(Set.of(specialization));
//                tutorRepository.save(tutor);
//                updatedUserDto.setSpecialization(specialization.getName());
//            }
//            else {
//                Student student = studentRepository.findByUser(existingUser)
//                        .orElseGet(() -> new Student());
//
//                student.setUser(existingUser);
//                studentRepository.save(student);
//
//                StudentCourse studentCourse = studentCourseRepository.findByStudentId(student.getId())
//                        .orElseGet(() -> new StudentCourse());
//
//                studentCourse.setStudentId(student.getId());
//                studentCourse.setCourseId(userDto.getCourseId());
//                studentCourseRepository.save(studentCourse);
//
//                Optional<Course> course = courseRepository.findById(studentCourse.getCourseId());
//                updatedUserDto.setCourse(course.map(Course::getName).orElse("Unknown Course"));
//            }
//
//            log.info("User updated successfully with ID: {}", existingUser.getId());
//
//            updatedUserDto.setRoleName(existingUser.getRoles().stream()
//                    .findFirst()
//                    .map(role -> role.getName().name().replaceFirst("^ROLE_", ""))
//                    .orElse(null));
//
//            return updatedUserDto;
//        } catch (EntityNotFoundException | DuplicateEntityException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }


    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword, String authHeader) throws Exception {
        log.info("Initiating password change for authenticated user.");

        UserDto userDto = userUtil.getCurrentUserDto(authHeader);
        User currentUser = EntityUtil.getEntityById(userRepository, userDto.getId());

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            log.warn("Password change failed: Incorrect old password for user ID {}", currentUser.getId());
            throw new IllegalArgumentException("Incorrect old password.");
        }

        if (!PasswordValidatorUtil.isValid(newPassword)) {
            log.warn("Password change failed: Weak password provided.");
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);

        log.info("Password changed successfully for user ID {}", currentUser.getId());
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.countByUsername(username) > 0;
    }

    @Override
    public Object retrieveOne(Long id) {
        log.info("Fetching user details for ID: {}", id);

        User user = EntityUtil.getEntityById(userRepository, id);

        Object userDto = userMapper.mapToDto(user);

        log.info("Successfully retrieved user with ID: {}", id);

        return userDto;
    }

    public boolean deleteUserById(Long id){
        try {
            log.info("Attempting to delete (deactivate) user with ID: {}", id);

            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("User not found: {}", id);
                        return new RuntimeException("User not found");
                    });
            if(user==null) {
            	log.warn("User with ID: {} does not exist in the system.", id);
                return false;
            }
            user.setStatus(false);
            userRepository.save(user);
            log.info("User with ID: {} status updated to DELETED", id);
            return true;
        } catch (Exception e) {
            log.error("Error updating user status for user ID: {} - {}", id, e.getMessage());
            throw new RuntimeException("Error updating user status: " + e.getMessage());
        }
    }

    @Override
    public int retrieveUserNameCount(String name) {
        return userRepository.countByName(name);
    }
}
