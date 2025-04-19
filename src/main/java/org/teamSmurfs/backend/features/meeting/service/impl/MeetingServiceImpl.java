package org.teamSmurfs.backend.features.meeting.service.impl;

import lombok.RequiredArgsConstructor;
import org.teamSmurfs.backend.features.meeting.dto.MeetingDto;
import org.teamSmurfs.backend.features.meeting.dto.MeetingMember;
import org.teamSmurfs.backend.features.meeting.dto.MeetingResponse;
import org.teamSmurfs.backend.features.meeting.model.Meeting;
import org.teamSmurfs.backend.features.meeting.repository.MeetingRepository;
import org.teamSmurfs.backend.features.meeting.service.MeetingService;
import org.teamSmurfs.backend.features.role.model.RoleName;
import org.teamSmurfs.backend.features.user.dto.UserDto;
import org.teamSmurfs.backend.features.user.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.features.user.repository.UserRepository;
import org.teamSmurfs.backend.features.user.utils.UserUtil;
import org.teamSmurfs.backend.features.dashboard.dto.DashboardTodayMeeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;


    @Override
    public MeetingResponse create(MeetingDto meetingDto) {
        User host = userRepository.findById(meetingDto.getHostId())
                .orElseThrow(() -> new RuntimeException("Host not found"));

        // Validate participant roles (only ROLE_STUDENT allowed)
        Set<User> participants = meetingDto.getParticipantIds() != null
                ? meetingDto.getParticipantIds().stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Participant not found: " + id)))
                .collect(Collectors.toSet())
                : Set.of();

        boolean allStudents = participants.stream()
                .allMatch(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(RoleName.ROLE_STUDENT)));

        if (!allStudents) {
            throw new RuntimeException("All participants must have the ROLE_STUDENT role.");
        }

        // Map and save meeting
        Meeting meeting = modelMapper.map(meetingDto, Meeting.class);
        meeting.setHost(host);
        meeting.setParticipants(participants);
        meetingRepository.save(meeting);

        // Create MeetingResponse
        List<MeetingMember> meetingMembers = participants.stream()
                .map(user -> MeetingMember.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .roleName(user.getRoles().iterator().next().getName().name())
                        .build())
                .collect(Collectors.toList());

        meetingMembers.add(MeetingMember.builder()
                .userId(host.getId())
                .name(host.getName())
                .email(host.getEmail())
                .roleName(host.getRoles().iterator().next().getName().name())
                .build());

        return MeetingResponse.builder()
                .id(meeting.getId())
                .meetingMembers(meetingMembers)
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .description(meeting.getDescription())
                .meetingType(meeting.getMeetingType())
                .link(meeting.getLink())
                .location(meeting.getLocation())
                .build();
    }


    @Override
    public MeetingResponse update(Long id, MeetingDto meetingDto) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        User host = userRepository.findById(meetingDto.getHostId())
                .orElseThrow(() -> new RuntimeException("Host not found"));

        // Fetch and validate participants
        Set<User> participants = meetingDto.getParticipantIds() != null
                ? meetingDto.getParticipantIds().stream()
                .map(participantId -> userRepository.findById(participantId)
                        .orElseThrow(() -> new RuntimeException("Participant not found: " + participantId)))
                .collect(Collectors.toSet())
                : meeting.getParticipants();

        boolean allStudents = participants.stream()
                .allMatch(user -> user.getRoles().stream()
                        .anyMatch(role -> "ROLE_STUDENT".equals(role.getName().name())));

        if (!allStudents) {
            throw new RuntimeException("All participants must have the ROLE_STUDENT role");
        }

        meeting.setHost(host);
        meeting.setParticipants(participants);
        meeting.setStartTime(meetingDto.getStartTime());
        meeting.setEndTime(meetingDto.getEndTime());
        meeting.setDescription(meetingDto.getDescription());
        meeting.setMeetingType(meetingDto.getMeetingType());
        meeting.setLink(meetingDto.getLink());
        meeting.setLocation(meetingDto.getLocation());

        Meeting updatedMeeting = meetingRepository.save(meeting);

        // Build and return MeetingResponse
        List<MeetingMember> meetingMembers = Stream.concat(
                Stream.of(updatedMeeting.getHost()),
                updatedMeeting.getParticipants().stream()
        ).map(user -> MeetingMember.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roleName(user.getRoles().stream()
                        .findFirst()
                        .map(role -> role.getName().name())
                        .orElse("UNKNOWN"))
                .build()).toList();

        return MeetingResponse.builder()
                .id(updatedMeeting.getId())
                .meetingMembers(meetingMembers)
                .startTime(updatedMeeting.getStartTime())
                .endTime(updatedMeeting.getEndTime())
                .description(updatedMeeting.getDescription())
                .meetingType(updatedMeeting.getMeetingType())
                .link(updatedMeeting.getLink())
                .location(updatedMeeting.getLocation())
                .build();
    }



    @Override
    public void delete(Long id) {
        meetingRepository.deleteById(id);
    }

    @Override
    public MeetingResponse getById(Long id, String authHeader) {
        // Get the current user
        UserDto currentUser = userUtil.getCurrentUserDto(authHeader);

        // Fetch meeting
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        // Check if the user is either the tutor (host) or a participant (student)
        boolean isHost = meeting.getHost().getId().equals(currentUser.getId());
        boolean isParticipant = meeting.getParticipants().stream()
                .anyMatch(p -> p.getId().equals(currentUser.getId()));

        if (!isHost && !isParticipant) {
            throw new RuntimeException("You are not authorized to view this meeting.");
        }

        return convertToMeetingResponse(meeting);
    }


    @Override
    public List<MeetingResponse> getAll(String authHeader) {
        // Get the current user
        UserDto currentUser = userUtil.getCurrentUserDto(authHeader);

        // Fetch only meetings where the user is either the host or a participant
        List<Meeting> meetings = meetingRepository.findAll().stream()
                .filter(meeting -> meeting.getHost().getId().equals(currentUser.getId()) ||
                        meeting.getParticipants().stream().anyMatch(p -> p.getId().equals(currentUser.getId())) ||
                        currentUser.getRoleName().equalsIgnoreCase("tutor") ||
                        currentUser.getRoleName().equalsIgnoreCase("admin")
                )
                .filter(meeting -> !meeting.isDone())
                .toList();

        return meetings.stream()
                .map(this::convertToMeetingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DashboardTodayMeeting> getTodayMeetingsForTutor(Long tutorId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<Meeting> meetings = meetingRepository.findByStartTimeBetweenAndHostId(startOfDay, endOfDay, tutorId);

        return meetings.stream().map(meeting -> new DashboardTodayMeeting(
                meeting.getId(),
                meeting.getHost().getName(),
                meeting.getStartTime(),
                meeting.getEndTime(),
                meeting.getDescription(),
                meeting.getMeetingType(),
                meeting.getLink(),
                meeting.getLocation(),
                meeting.getCreatedAt(),
                meeting.isDone()
        )).collect(Collectors.toList());
    }

    @Override
    public List<DashboardTodayMeeting> getTodayMeetingsForStudent(final Long studentId) {
        final LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        final LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        List<Meeting> meetings = meetingRepository.findByStartTimeBetweenAndParticipantsId(startOfDay, endOfDay, studentId);

        return meetings.stream().map(meeting -> new DashboardTodayMeeting(
                meeting.getId(),
                meeting.getHost().getName(),
                meeting.getStartTime(),
                meeting.getEndTime(),
                meeting.getDescription(),
                meeting.getMeetingType(),
                meeting.getLink(),
                meeting.getLocation(),
                meeting.getCreatedAt(),
                meeting.isDone()
        )).collect(Collectors.toList());
    }


    private MeetingResponse convertToMeetingResponse(Meeting meeting) {
        List<MeetingMember> meetingMembers = new ArrayList<>();

        // Add host to meeting members
        meetingMembers.add(MeetingMember.builder()
                .userId(meeting.getHost().getId())
                .email(meeting.getHost().getEmail())
                .name(meeting.getHost().getName())
                .roleName(meeting.getHost().getRoles().stream()
                        .findFirst()
                        .map(role -> role.getName().name())
                        .orElse("UNKNOWN"))
                .build());

        // Add participants to meeting members
        meeting.getParticipants().forEach(participant -> {
            meetingMembers.add(MeetingMember.builder()
                    .userId(participant.getId())
                    .email(participant.getEmail())
                    .name(participant.getName())
                    .roleName(participant.getRoles().stream()
                            .findFirst()
                            .map(role -> role.getName().name())
                            .orElse("UNKNOWN"))
                    .build());
        });

        return MeetingResponse.builder()
                .id(meeting.getId())
                .meetingMembers(meetingMembers)
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .description(meeting.getDescription())
                .meetingType(meeting.getMeetingType())
                .link(meeting.getLink())
                .location(meeting.getLocation())
                .isDone(meeting.isDone())
                .build();
    }

    @Override
    public void markMeetingAsDone(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        meeting.setDone(true);
        meetingRepository.save(meeting);
    }


}
