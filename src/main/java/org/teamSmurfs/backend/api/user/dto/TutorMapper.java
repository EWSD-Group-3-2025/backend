package org.teamSmurfs.backend.api.user.dto;

import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.api.user.model.User;

@Component
public class TutorMapper {
	
	 public TutorDto mapToDto(User user) {
        if (user == null || user.getTutor() == null) {
            return null;
        }

        Tutor tutor = user.getTutor();

        TutorDto tutorDto = TutorDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .status(user.isStatus())
                .createdAt(user.getCreatedAt())
                .gender(user.getGender())
                .build();

        user.getRoles().stream().findFirst().ifPresent(roleEntity -> {
            tutorDto.setRoleName(roleEntity.getName().name().replaceFirst("^ROLE_", ""));
            tutorDto.setRoleId(roleEntity.getId());
        });

         if (tutor.getSpecialization() != null) {
             String specializationName = tutor.getSpecialization().getName();
             Long specializationId = tutor.getSpecialization().getId();

             tutorDto.setSpecializationName(specializationName);
             tutorDto.setSpecializationId(specializationId);
         }

         return tutorDto;

     }
}
