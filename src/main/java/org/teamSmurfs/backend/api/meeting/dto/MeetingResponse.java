package org.teamSmurfs.backend.api.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingResponse {
    private Long id;
    List<MeetingMember> meetingMembers;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private Integer meetingType;
    private String link;
    private String location;
}
