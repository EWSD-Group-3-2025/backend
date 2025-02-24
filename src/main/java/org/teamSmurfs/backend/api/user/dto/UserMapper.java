package org.teamSmurfs.backend.api.user.dto;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.user.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
    
    private final StaffMapper staffMapper;
    private final StudentMapper studentMapper;
    private final TutorMapper tutorMapper;
    private final ModelMapper modelMapper;

    public Object mapToDto(User user) {
        if (user.getRoles().stream().anyMatch(r -> r.getName().name().equalsIgnoreCase("ROLE_STAFF"))) {
            return staffMapper.mapToDto(user);
        }

        if (user.getRoles().stream().anyMatch(r -> r.getName().name().equalsIgnoreCase("ROLE_STUDENT"))) {
            return studentMapper.mapToDto(user);
        }

        if (user.getRoles().stream().anyMatch(r -> r.getName().name().equalsIgnoreCase("ROLE_TUTOR"))) {
            return tutorMapper.mapToDto(user);
        }

        //admin fixed for later
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setRoleName(user.getRoles().stream()
                .findFirst()
                .map(role -> role.getName().name().replaceFirst("^ROLE_", ""))
                .orElse(null));

        return userDto;
    }
}
