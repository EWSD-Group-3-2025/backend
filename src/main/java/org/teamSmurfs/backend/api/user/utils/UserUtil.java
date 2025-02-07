/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 10:58 AM (UTC)
 */
package org.teamSmurfs.backend.api.user.utils;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.utils.DtoUtil;
import org.teamSmurfs.backend.security.service.JwtService;

@Component
@Slf4j
public class UserUtil {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserUtil(JwtService jwtService, UserRepository userRepository, ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto getCurrentUserDto(String authHeader) {
        String email = extractEmailFromToken(authHeader);
        User user = findUserByEmail(email);
        return DtoUtil.map(user, UserDto.class, modelMapper);
    }

    public String extractEmailFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header received");
            throw new SecurityException("Unauthorized: Missing or invalid token");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.validateToken(token);
        return claims.getSubject();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", email);
                    return new SecurityException("User not found");
                });
    }
}
