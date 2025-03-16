package org.teamSmurfs.backend.api.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDto {
    private Long id;
    private Long hostId;
    private Set<Long> participantIds;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private Integer meetingType;
    private String link;
    private String location;

    // Getters and Setters
}
