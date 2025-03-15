package org.teamSmurfs.backend.api.event.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.allocation.repository.AllocationRepository;
import org.teamSmurfs.backend.api.event.dto.CreateEventRequest;
import org.teamSmurfs.backend.api.event.dto.EventDto;
import org.teamSmurfs.backend.api.event.dto.UpdateEventRequest;
import org.teamSmurfs.backend.api.event.model.Event;
import org.teamSmurfs.backend.api.event.repository.EventRepository;
import org.teamSmurfs.backend.api.event.service.EventService;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.TutorRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.service.MailService;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService{
	
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final TutorRepository tutorRepository;
	private final AllocationRepository allocationRepository;
	private final ModelMapper modelMapper;
	private final MailService mailService;
	
	@Override
	public void createEvent(CreateEventRequest eventRequest) throws Exception {
	    try{
	    	log.info("Creating Event with Tutor ID: {}", eventRequest.getTutorId());
	    
	    User user = EntityUtil.getEntityById(this.userRepository, eventRequest.getTutorId());
	    
	    Tutor tutor = tutorRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Tutor not found for User ID: " +user.getId()));
	    
	    Optional.ofNullable(eventRequest)
	            .map(event -> new Event(
	            		tutor,
	            		event.getTitle(),
	            		event.getDescription(),
	            		event.getStartdate(),
	            		event.getEnddate()
	            ))
	            .map(eventRepository::save)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid Event request"));	    
	    
	    List<Allocation> allocations = allocationRepository.findByTutorId(tutor.getId());
	    
	    sendEventEmails(allocations, tutor);
	    }catch(Exception e){
	    	log.error("Error Creating Event: ", e);
            throw new RuntimeException(e.getMessage());
	    }
	}

	@Override
    public List<EventDto> retrieveAll() {
        return eventRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
	
	@Override
	public EventDto retrieveOne(Long id) {	
		return mapToDto(EntityUtil.getEntityById(eventRepository, id));
	}
	
	@Override
	public EventDto update(Long id, UpdateEventRequest updateEventRequest) throws Exception {
		try {
	    log.info("Updating Event with ID: {}", id);
	    
		final Event existEvent = EntityUtil.getEntityById(eventRepository, id);
		
		if (updateEventRequest.getTutorId() != null && !existEvent.getOrganizer().getId().equals(updateEventRequest.getTutorId())) {    	
			 User user = EntityUtil.getEntityById(this.userRepository, updateEventRequest.getTutorId());			 
			 Tutor existTutor = tutorRepository.findByUser(user)
		                .orElseThrow(() -> new EntityNotFoundException("Tutor not found for User ID: " +user.getId()));			
			existEvent.setOrganizer(existTutor);
        }
		    existEvent.setTitle(updateEventRequest.getTitle());
		    existEvent.setDescription(updateEventRequest.getDescription());
		    existEvent.setStartDate(updateEventRequest.getStartdate());
		    existEvent.setEndDate(updateEventRequest.getEnddate());	
		    
		    return mapToDto(eventRepository.save(existEvent));
		 }catch (RuntimeException e) {
	            log.error("Error updating event with ID: {}", id, e);
	            throw new Exception("Event update failed: " + e.getMessage());
	        } catch (Exception e) {
	            log.error("Unexpected error while updating event with ID: {}", id, e);
	            throw new Exception("Unexpected error: " + e.getMessage());
	        }
	}

	@Override
	public void deleteEvent(long id) {
	try {
		log.info("Deleting Event with Tutor ID: {}", id);
	    Event event = EntityUtil.getEntityById(eventRepository, id);
	 
	 if(event !=null ) {
		 eventRepository.deleteById(id);
	 }
		}catch(Exception e) {
			log.error("Deleting Event for Tutor ID: {} - {}", id, e);
            throw new RuntimeException("Error Deleting Event :" + e.getMessage());
		}	
	}
	
	private void sendEventEmails(List<Allocation> allocations, Tutor tutor) {
        String tutorName = tutor.getUser().getName();

        String studentNames = allocations.stream()
                .map(allocation -> allocation.getStudent().getUser().getName())
                .collect(Collectors.joining(", "));

        mailService.sendEventEmail(tutor.getUser().getEmail(), "TUTOR", tutorName, studentNames);

        for (Allocation allocation : allocations) {
            Student student = allocation.getStudent();
            String studentName = student.getUser().getName();
            mailService.sendEventEmail(student.getUser().getEmail(), "STUDENT", tutorName, studentName);
        }
    }
	
	private EventDto mapToDto(final Event event) {
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        if (event.getOrganizer() != null) {
            User tutor = userRepository.findById(event.getOrganizer().getUser().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Tutor not found for Event ID: " + eventDto.getTutorId()));
            eventDto.setTutorId(tutor.getId());
            eventDto.setTutorName(tutor.getName());
        }
        return eventDto;
    }

}
