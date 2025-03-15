/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:00 PM
 */
package org.teamSmurfs.backend.security.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.role.model.Role;
import org.teamSmurfs.backend.api.role.model.RoleName;
import org.teamSmurfs.backend.api.role.repository.RoleRepository;
import org.teamSmurfs.backend.api.token.dto.TokenDto;
import org.teamSmurfs.backend.api.token.model.Token;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.utils.UserUtil;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;
import org.teamSmurfs.backend.api.visit_log.service.VisitLogService;
import org.teamSmurfs.backend.config.exception.TokenExpiredException;
import org.teamSmurfs.backend.config.exception.UnauthorizedException;
import org.teamSmurfs.backend.config.utils.DtoUtil;
import org.teamSmurfs.backend.config.utils.EntityUtil;
import org.teamSmurfs.backend.security.dto.LoginRequest;
import org.teamSmurfs.backend.security.dto.RegisterRequest;
import org.teamSmurfs.backend.security.dto.UpdateUserResponseDto;
import org.teamSmurfs.backend.security.service.AuthService;
import org.teamSmurfs.backend.security.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.security.utils.AuthUtil;
import org.teamSmurfs.backend.security.utils.ClaimsProvider;
import org.teamSmurfs.backend.api.token.repository.TokenRepository;
import org.teamSmurfs.backend.config.service.MailService;
import org.teamSmurfs.backend.security.utils.OtpUtils;
import org.teamSmurfs.backend.security.utils.OtpUtils.OtpData;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;
    private final MailService mailService;
    private final AuthUtil authUtil;
    private final VisitLogService visitLogService;

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private String emailInProcess;

    @Override
    public ApiResponse authenticateUser(LoginRequest loginRequest) {
        String identifier = loginRequest.getEmail();
        log.info("Authenticating user with identifier: {}", identifier);

        Optional<User> userOpt = this.userRepository.findByEmail(identifier)
                .or(() -> this.userRepository.findByUsername(identifier));

        User user = userOpt.orElseThrow(() -> {
            log.warn("User not found with identifier: {}", identifier);
            return new UnauthorizedException("Invalid email/username or password");
        });

        if (!user.isStatus()) {
            log.warn("User is inactive: {}", loginRequest.getEmail());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Your account has been locked. Please contact your administrator.")
                    .build();
        }

        String roleName = user.getRoles().stream()
                .findFirst()
                .map(role -> role.getName().name().replaceFirst("^ROLE_", ""))
                .orElse(null);

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", loginRequest.getEmail());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid email or password")
                    .build();
        }

        log.info("User authenticated successfully: {}", loginRequest.getEmail());

        boolean firstTimeLogin = false;

        if(user.isLoginFirstTime()) {
            firstTimeLogin = true;
            user.setLoginFirstTime(false);
            this.userRepository.save(user);
            log.info("User {} logged in for the first time.", user.getUsername());
        }


        UserDto userDto = DtoUtil.map(user, UserDto.class, modelMapper);

        userDto.setRoleName(roleName);
        userDto.setFirstTimeLogin(firstTimeLogin);

        Token refreshToken;
        Optional<Token> refreshTokenOptional = tokenRepository.findByUserAndExpiredAtAfter(user, Instant.now());
        if (refreshTokenOptional.isPresent()) {
            log.info("Valid refresh token found for user: {}", loginRequest.getEmail());
            refreshToken = refreshTokenOptional.get();
        } else {
            log.info("Refresh token expired or not found for user: {}, generating new token", loginRequest.getEmail());
            Map<String, Object> tokenData = authUtil.generateTokens(user, roleName);
            String newRefreshToken = (String) tokenData.get("refreshToken");

            Instant newExpiryDate = Instant.now().plusSeconds(7 * 24 * 60 * 60);

            refreshToken = Token.builder()
                    .refreshtoken(newRefreshToken)
                    .expiredAt(newExpiryDate)
                    .user(user)
                    .build();

            tokenRepository.save(refreshToken);
        }

        Map<String, Object> tokenData = authUtil.generateTokens(user, roleName);

        assert refreshTokenOptional.isPresent();
        TokenDto tokenDto = DtoUtil.map(refreshToken, TokenDto.class, modelMapper);

        VisitLog visitLog = new VisitLog(
                user,
                "AFTER_LOGGED",
                "something",
                "pageName"
        );

        visitLogService.save(visitLog);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of(
                        "user", userDto,
                        "accessToken", tokenData.get("accessToken"),
                        "refreshToken", tokenDto.getRefreshtoken()))
                .message("You are successfully logged in!")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse registerUser(RegisterRequest registerRequest) {
        log.info("Registering new user with email: {}", registerRequest.getEmail());

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Email already exists: {}", registerRequest.getEmail());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .message("Email is already in use")
                    .build();
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found in database!"));
        log.info("Assigning role: {}", userRole.getName());

        User newUser = User.builder()
                .name(registerRequest.getName())
                .username(userUtil.generateUniqueUsername(registerRequest.getName()))
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(newUser);

        Map<String, Object> tokenData = authUtil.generateTokens(newUser, String.valueOf(userRole.getName()));

        String accessToken = (String) tokenData.get("accessToken");
        String refreshToken = (String) tokenData.get("refreshToken");

        Instant expiredAt = Instant.now().plus(7, ChronoUnit.DAYS);

        Token token = Token.builder()
                .user(newUser)
                .refreshtoken(refreshToken)
                .expiredAt(expiredAt)
                .build();

        tokenRepository.save(token);

        log.info("User registered successfully: {}", registerRequest.getEmail());

        UserDto userDto = DtoUtil.map(newUser, UserDto.class, modelMapper);
        userDto.setRoleName(newUser.getRoles().stream()
                .findFirst()
                .map(role -> role.getName().name().replaceFirst("^ROLE_", ""))
                .orElse(null));

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(Map.of(
                        "user", userDto,
                        "accessToken", accessToken,
                        "refreshToken", refreshToken))
                .message("You have registered successfully.")
                .build();
    }

    @Override
    public void logout(String accessToken) {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String token = accessToken.substring(7);
            Claims claims = jwtService.validateToken(token);
            String userEmail = claims.getSubject();

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UnauthorizedException(
                            "User not found. Cannot proceed with logout."));

            log.debug("Revoking access token for user: {}", user.getEmail());
            jwtService.revokeToken(token);
        }

        log.info("User successfully logged out.");
    }

    @Override
    public ApiResponse refreshToken(String refreshToken) {
        log.info("Validating refresh token");

        Claims claims;
        try {
            claims = jwtService.validateToken(refreshToken);
        } catch (TokenExpiredException ex) {
            log.warn("Invalid refresh token: {}", ex.getMessage());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.FORBIDDEN.value())
                    .data(ex.getMessage())
                    .message("Invalid or expired refresh token")
                    .build();
        }

        String email = claims.getSubject();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.warn("User not found for refresh token: {}", email);
            throw new UnauthorizedException("User not found for refresh token");
        }

        log.info("Generating new access token for user: {}", email);

        Set<Role> roleList = user.getRoles();
        String roleName = roleList.stream()
                .map(role -> role.getName().name())
                .findFirst()
                .orElse("ROLE_USER");

        String newAccessToken = jwtService.generateToken(ClaimsProvider.generateClaims(user), roleName, email,
                15 * 60 * 1000);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("accessToken", newAccessToken))
                .message("Access token refreshed successfully")
                .build();
    }

    @Override
    public ApiResponse getCurrentUser(final String authHeader, final String routeName, final String browserName,
                                      final String pageName) {
        UserDto userDto = userUtil.getCurrentUserDto(authHeader);
        User user = EntityUtil.getEntityById(userRepository, userDto.getId());

        VisitLog visitLog = new VisitLog(
                user,
                routeName,
                browserName,
                pageName
        );

        visitLogService.save(visitLog);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of(
                        "user", userDto))
                .message("User retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse initiatePasswordReset(String email) {
        log.info("Initiating password reset for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("No user found with email: {}", email);
                    return new UnauthorizedException("No user found with this email");
                });

        String otp = OtpUtils.generateOtp();
        otpStore.put(otp, new OtpData(email, Instant.now().plus(30, ChronoUnit.MINUTES)));

        try {
            String emailBody = String.format("""
                    <h1>Password Reset OTP</h1>
                    <p>Your OTP for password reset is: <strong>%s</strong></p>
                    <p>This OTP will expire in 30 minutes.</p>
                    """, otp);

            mailService.sendMail(email, "Password Reset OTP", emailBody);

            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.OK.value())
                    .message("OTP has been sent to your email")
                    .build();
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP");
        }
    }

    @Override
    public ApiResponse verifyOtp(String otp) {
        log.info("Verifying OTP");

        OtpData otpData = otpStore.get(otp);
        if (otpData == null || otpData.isExpired()) {
            log.warn("Invalid or expired OTP");
            throw new UnauthorizedException("Invalid or expired OTP");
        }

        emailInProcess = otpData.getEmail();
        otpStore.remove(otp);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("OTP verified successfully")
                .build();
    }

    @Override
    public ApiResponse resetPassword(String newPassword, String confirmPassword) {
        if (emailInProcess == null) {
            throw new UnauthorizedException("Please verify OTP first");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new UnauthorizedException("Passwords do not match");
        }

        User user = userRepository.findByEmail(emailInProcess)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        emailInProcess = null;

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Password reset successfully")
                .build();
    }

    @Override
    public ApiResponse updateUser(String authHeader, UserDto userDto) {
        Long userId = userUtil.extractUserIdFromToken(authHeader);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for ID: {}", userId);
                    return new UnauthorizedException("User not found");
                });

        log.info("User retrieved: {}", user);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
//        if (userDto.getEmail() != null) {
//            user.setEmail(userDto.getEmail());
//        }
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }

        log.info("Updated user details: {}", user);

        User savedUser = userRepository.save(user);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String createdAtStr = savedUser.getCreatedAt().format(formatter);
        String updatedAtStr = savedUser.getUpdatedAt().format(formatter);

        log.info("User saved successfully: {}", savedUser);

        UpdateUserResponseDto updatedUserDto = UpdateUserResponseDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .createdAt(createdAtStr)
                .updatedAt(updatedAtStr)
                .build();

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("UserInfo updated successfully")
                .data(updatedUserDto)
                .build();
    }

}
