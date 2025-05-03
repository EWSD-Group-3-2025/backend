package org.teamSmurfs.backend.features.user.dto;

import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.features.user.model.Gender;
import org.teamSmurfs.backend.features.user.model.Staff;
import org.teamSmurfs.backend.features.user.model.User;

@Component
public class StaffMapper {
	
	    public StaffDto mapToDto(User user) {
	        if (user == null || user.getStaff() == null) {
	            return null;
	        }

	        Staff staff = user.getStaff();
	        
	        StaffDto staffDto = StaffDto.builder()
	                .id(user.getId())
	                .name(user.getName())
	                .email(user.getEmail())
	                .username(user.getUsername())
	                .createdAt(user.getCreatedAt())
	                .status(user.isStatus())
	                .roleName(user.getRoles().stream()
	                        .findFirst()
	                        .map(roleEntity -> roleEntity.getName().name().replaceFirst("^ROLE_", ""))
	                        .orElse(null))
	                .roleId(user.getRoles().stream()
	                        .findFirst()
	                        .map(roleEntity -> roleEntity.getId())
	                        .orElse(null))
	                .departmentName(staff.getDepartment() != null ? staff.getDepartment().getName() : null)
	                .departmentId(staff.getDepartment() != null ? staff.getDepartment().getId() : null)
					.admin(staff.isAdmin())
					.gender(user.getGender())
					.genderName(Gender.fromInt(user.getGender()).getCode())
	                .build();
	        
	        return staffDto;
	    }
}
