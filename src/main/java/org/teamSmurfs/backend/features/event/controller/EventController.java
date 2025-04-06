package org.teamSmurfs.backend.features.event.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamSmurfs.backend.features.event.dto.CreateEventRequest;
import org.teamSmurfs.backend.features.event.dto.EventDto;
import org.teamSmurfs.backend.features.event.dto.UpdateEventRequest;
import org.teamSmurfs.backend.features.event.service.EventService;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {
	
	private final EventService eventService;
	
	@PostMapping
	public ResponseEntity<ApiResponse> createEvent(@Validated @RequestBody CreateEventRequest eventrequest,
			HttpServletRequest request) throws Exception{
		
		log.info("Create Event with Tutor ID: { }",eventrequest.getTutorId());
		 double requestStartTime = RequestUtils.extractRequestStartTime(request);
		 eventService.createEvent(eventrequest);
		 ApiResponse successResponse = ApiResponse.builder()
	                .success(1)
	                .code(HttpStatus.CREATED.value())
	                .data(true)
	                .message("Event created successfully")
	                .build();
		return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse> retrieveAllEvent(HttpServletRequest request) throws Exception{
		
		log.info("Retrieving all events");
		 double requestStartTime = RequestUtils.extractRequestStartTime(request);
		 
		 List<EventDto> eventList = eventService.retrieveAll();
		 
		 ApiResponse successResponse = ApiResponse.builder()
	                .success(1) 
	                .code(HttpStatus.OK.value())
	                .data(eventList !=null ? eventList : Collections.emptyList())
	                .message("Event retrieved successfully")
	                .build();
		return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
		
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveEvent(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Retrieving Event with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        EventDto eventDto = eventService.retrieveOne(id);

        log.info("Retrieved Event successfully: {}", eventDto);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(eventDto)
                .message("Event retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
	
	 @PatchMapping("/{id}")
	    public ResponseEntity<ApiResponse> update(
	            @PathVariable(value = "id") final Long id,
	            @RequestBody final UpdateEventRequest updateEventRequest,
	            final HttpServletRequest request

	    ) throws Exception {
	        log.info("Updating Event with id {}", id);

	        double requestStartTime = RequestUtils.extractRequestStartTime(request);

	        EventDto updatedEvent = eventService.update(id, updateEventRequest);

	        log.info("Updated Event successfully: {}", updatedEvent.getTitle());

	        ApiResponse successResponse = ApiResponse.builder()
	                .success(1)
	                .code(HttpStatus.NO_CONTENT.value())
	                .data(true)
	                .message("Event updated successfully")
	                .build();

	        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
	    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteEvent(@PathVariable(value = "id") final long id,
			HttpServletRequest request){
		log.info("Deleting Event with ID: {}" , id);
	    double requestStartTime = RequestUtils.extractRequestStartTime(request);
		eventService.deleteEvent(id);
		 ApiResponse successResponse = ApiResponse.builder()
         .success(1)
         .code(HttpStatus.NO_CONTENT.value())
         .data(true)
         .message("Event deleted successfully")
         .build();

      return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
	}
	

}
