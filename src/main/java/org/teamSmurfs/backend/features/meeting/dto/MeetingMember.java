package org.teamSmurfs.backend.features.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingMember {

    private long userId;
    private String email;
    private String name;
    private String roleName;
}
