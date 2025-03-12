package org.teamSmurfs.backend.report.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.dto.UserMapper;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;
import org.teamSmurfs.backend.api.visit_log.repository.VisitLogRepository;
import org.teamSmurfs.backend.report.dto.BrowserUsageDto;
import org.teamSmurfs.backend.report.dto.RouteUsageDto;
import org.teamSmurfs.backend.report.service.ReportService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final VisitLogRepository visitLogRepository;
    private final UserMapper userMapper;

    @Override
    public List<BrowserUsageDto> getUniqueUserCountByBrowser() {
        return visitLogRepository.findAll().stream()
                .filter(log -> log.getBrowserName() != null && log.getUser() != null) // Ensure non-null values
                .collect(Collectors.groupingBy(
                        VisitLog::getBrowserName,
                        Collectors.mapping(log -> log.getUser().getId(), Collectors.toSet()) // Get unique user IDs per browser
                )).entrySet().stream()
                .map(entry -> new BrowserUsageDto(entry.getKey(), entry.getValue().size())) // Convert to DTO
                .collect(Collectors.toList()); // Convert to List<BrowserUsageDto>
    }


    @Override
    public List<RouteUsageDto> getTop5VisitedRoutes() {
        return visitLogRepository.findAll().stream()
                .filter(log -> log.getRouteName() != null) // Exclude null route names
                .collect(Collectors.groupingBy(
                        VisitLog::getRouteName,
                        Collectors.counting() // Count visits per route
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // Sort by most visited
                .limit(5) // Keep only the top 5 routes
                .map(entry -> new RouteUsageDto(entry.getKey(), entry.getValue())) // Convert to DTO
                .collect(Collectors.toList()); // Convert to List<RouteUsageDto>
    }

    @Override
    public List<Object> getMostVisitedUsers() {
        List<User> users = visitLogRepository.getMostVisitedUsers(); // Fetch top 20 most visited users

        if (users.isEmpty()) {
            log.warn("No most visited users found in visit logs");
            return Collections.emptyList();
        }

        // Map users to DTOs using the existing mapping logic from retrieveUsers()
        List<Object> userDtos = users.stream()
                .map(userMapper::mapToDto) // Use existing mapping logic
                .collect(Collectors.toList());

        log.info("Successfully retrieved {} most visited users", userDtos.size());
        return userDtos;
    }


	@Override
	public List<Object> getMostActiveUsers() {
		List<User> users = visitLogRepository.getMostActiveUsers(PageRequest.of(0,20));
		
		if(users.size() == 0) {
			log.warn("No most active users found in visit logs");
			return Collections.emptyList();
		}
		
		List<Object> userDtos = users.stream()
				.map(userMapper::mapToDto)
				.collect(Collectors.toList());
		log.info("Successfully retrieved {} most active users", userDtos.size());
		return userDtos;
	}


	@Override
	public List<Object> findInactiveUsersBetweenDates() {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today.minusDays(28);
        LocalDateTime endDate = today.minusDays(7);
        List<User> users = visitLogRepository.findInactiveUsersBetweenDates(startDate,endDate);
		
		if(users.size() == 0) {
			log.warn("No inactivity users between 7 to 28 days ago found in visit logs");
			return Collections.emptyList();
		}
		
		List<Object> userDtos = users.stream()
				.map(userMapper::mapToDto)
				.collect(Collectors.toList());
		log.info("Successfully retrieved {} inactivity users between 7 to 28 days ago", userDtos.size());
		return userDtos;
	}


}
