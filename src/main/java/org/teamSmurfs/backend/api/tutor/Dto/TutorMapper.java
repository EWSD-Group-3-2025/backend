package org.teamSmurfs.backend.api.tutor.Dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.specialization.model.Specialization;
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
                .build();

        user.getRoles().stream().findFirst().ifPresent(roleEntity -> {
            tutorDto.setRoleName(roleEntity.getName().name().replaceFirst("^ROLE_", ""));
            tutorDto.setRoleId(roleEntity.getId());
        });

        if (tutor.getSpecializations() != null && !tutor.getSpecializations().isEmpty()) {
            List<String> specializationNames = tutor.getSpecializations().stream()
                    .map(Specialization::getName)
                    .collect(Collectors.toList());

            List<Long> specializationIds = tutor.getSpecializations().stream()
                    .map(Specialization::getId)
                    .collect(Collectors.toList());

            tutorDto.setSpecialization(String.join(", ", specializationNames));
            tutorDto.setSpecializationId(specializationIds.isEmpty() ? null : specializationIds.get(0));
        }

        return tutorDto;
    }
}
