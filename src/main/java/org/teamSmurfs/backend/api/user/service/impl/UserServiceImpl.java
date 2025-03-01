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
import org.teamSmurfs.backend.api.user.dto.UpdateUserRequest;
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
                users = userRepository.findAllByOrderByCreatedAtDesc();
            } else if (role.equalsIgnoreCase("admin")) {
                users = userRepository.findUsersWithAdminRole();

                users.forEach(user -> {
                    Set<Role> roles = new HashSet<>(roleRepository.findRolesByUserId(user.getId()));
                    user.setRoles(roles);
                });
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
    public void createUser(CreateUserRequest createUserRequest) throws Exception {
        try {
            log.info("Creating new user with email: {}", createUserRequest.getEmail());

            if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent()) {
                log.warn("Email already exists: {}", createUserRequest.getEmail());
                throw new DuplicateEntityException("Email: " + createUserRequest.getEmail() + " is already in use");
            }

            Role userRole = EntityUtil.getEntityById(roleRepository, createUserRequest.getRoleId());
            log.info("Assigning role: {}", userRole.getName());

            String rawPassword = PasswordGeneratorUtil.generatePassword();

            Gender gender = Gender.fromInt(createUserRequest.getGender());
            if (gender == Gender.INVALID) {
                throw new IllegalArgumentException("Invalid gender value provided.");
            }

            User newUser = User.builder()
                    .name(createUserRequest.getName())
                    .username(createUserRequest.getUsername())
                    .email(createUserRequest.getEmail())
                    .password(passwordEncoder.encode(rawPassword))
                    .roles(Set.of(userRole))
                    .gender(gender.getValue())
                    .build();

            userRepository.save(newUser);

            log.info("User created successfully with ID: {}", newUser.getId());

            if (userRole.getName().equals(RoleName.ROLE_ADMIN)) {
                Staff newStaff = new Staff();
                newStaff.setUser(newUser);
                Department department = EntityUtil.getEntityById(this.departmentRepository, createUserRequest.getDepartmentId());
                newStaff.setDepartment(department);
                newStaff.setAdmin(true);
                staffRepository.save(newStaff);
                log.info("Admin assigned to department: {}", department.getName());

            } else if (userRole.getName().equals(RoleName.ROLE_STAFF)) {
                Staff newStaff = new Staff();
                newStaff.setUser(newUser);
                Department department = EntityUtil.getEntityById(this.departmentRepository, createUserRequest.getDepartmentId());
                newStaff.setDepartment(department);
                staffRepository.save(newStaff);
                log.info("Staff assigned to department: {}", department.getName());
            } else if (userRole.getName().equals(RoleName.ROLE_TUTOR)) {
                Tutor newTutor = new Tutor();
                newTutor.setUser(newUser);
                Specialization specialization = specializationRepository.findById(createUserRequest.getSpecializationId())
                        .orElseThrow(() -> new RuntimeException("Specialization not found"));

                newTutor.setSpecialization(specialization);
                tutorRepository.save(newTutor);

                log.info("Tutor assigned to specialization: {}", specialization.getName());
            } else {
                Student newStudent = new Student();
                newStudent.setUser(newUser);
                studentRepository.save(newStudent);

                StudentCourse newStudentCourse = new StudentCourse();
                newStudentCourse.setStudentId(newStudent.getId());
                newStudentCourse.setCourseId(createUserRequest.getCourseId());
                studentCourseRepository.save(newStudentCourse);

                Optional<Course> course = courseRepository.findById(newStudentCourse.getCourseId());
                log.info("Student enrolled in course: {}", course.map(Course::getName).orElse("Unknown Course"));
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

            log.info("User credentials sent to email.");

        } catch (DuplicateEntityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating user: ", e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void updateUser(Long userId, UpdateUserRequest updateUserRequest) throws Exception {
        try {
            log.info("Updating user with ID: {}", userId);

            // Fetch the existing user by ID
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if email is being updated and is already taken
            if (!existingUser.getEmail().equals(updateUserRequest.getEmail()) &&
                    userRepository.findByEmail(updateUserRequest.getEmail()).isPresent()) {
                log.warn("Email already exists: {}", updateUserRequest.getEmail());
                throw new DuplicateEntityException("Email: " + updateUserRequest.getEmail() + " is already in use");
            }

            // Update user fields
            existingUser.setName(updateUserRequest.getName());
            existingUser.setEmail(updateUserRequest.getEmail());

            // Save the updated user
            userRepository.save(existingUser);
            log.info("User with ID: {} updated successfully.", existingUser.getId());

            // Handling role and other associations
            Role newRole = EntityUtil.getEntityById(roleRepository, updateUserRequest.getRoleId());
            if (!existingUser.getRoles().contains(newRole)) {
                existingUser.setRoles(Set.of(newRole));
                userRepository.save(existingUser);
                log.info("User role updated to: {}", newRole.getName());
            }

            // Handle additional entity associations (e.g., Staff, Tutor, Student)
            if (newRole.getName().equals(RoleName.ROLE_ADMIN) || newRole.getName().equals(RoleName.ROLE_STAFF)) {
                // Update department if changed
                Department department = EntityUtil.getEntityById(this.departmentRepository, updateUserRequest.getDepartmentId());
                existingUser.getStaff().setDepartment(department);  // Assuming a one-to-one relation between Staff and User
                existingUser.getStaff().setAdmin(updateUserRequest.isAdmin());
                staffRepository.save(existingUser.getStaff());
                log.info("User's department updated to: {}", department.getName());
            }

            if (newRole.getName().equals(RoleName.ROLE_TUTOR)) {
                // Update specialization if needed
                Specialization specialization = specializationRepository.findById(updateUserRequest.getSpecializationId())
                        .orElseThrow(() -> new RuntimeException("Specialization not found"));
                Tutor tutor = existingUser.getTutor();
                tutor.setSpecialization(specialization);
                tutorRepository.save(tutor);
                log.info("User's specialization updated to: {}", specialization.getName());
            }

            // Handle Student and their course associations
            if (newRole.getName().equals(RoleName.ROLE_STUDENT)) {
                Student student = existingUser.getStudent();
                if (student != null) {
                    StudentCourse studentCourse = studentCourseRepository.findByStudentId(student.getId())
                            .orElseThrow(() -> new RuntimeException("StudentCourse not found for student ID: " + student.getId()));

                    studentCourse.setCourseId(updateUserRequest.getCourseId());
                    studentCourseRepository.save(studentCourse);

                    Optional<Course> course = courseRepository.findById(updateUserRequest.getCourseId());
                    log.info("Student's course updated to: {}", course.map(Course::getName).orElse("Unknown Course"));
                }
            }

        } catch (RuntimeException e) {
            log.error("Error updating user with ID: {}", userId, e);
            throw new Exception("User update failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while updating user with ID: {}", userId, e);
            throw new Exception("Unexpected error: " + e.getMessage());
        }
    }


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

    @Override
    public void resetPassword(final String authHeader) {
        log.info("Initiating password reset for authenticated user.");

        UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);
        User currentUser = EntityUtil.getEntityById(this.userRepository, userDto.getId());

        log.info("Generating new password for user ID {}", currentUser.getId());

        String randomPassword = PasswordGeneratorUtil.generatePassword();

        currentUser.setPassword(this.passwordEncoder.encode(randomPassword));
        currentUser.setLoginFirstTime(true);

        this.userRepository.save(currentUser);

        log.info("Password reset successfully for email {}. Sending reset email.", currentUser.getEmail());

        this.mailService.sendEmailForResetPassword(currentUser.getEmail(), randomPassword);

        log.info("Password reset confirmation email sent to {}", currentUser.getEmail());
    }
}
