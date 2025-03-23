package org.teamSmurfs.backend.api.event.service;

import java.util.List;

import org.teamSmurfs.backend.api.event.dto.CreateEventRequest;
import org.teamSmurfs.backend.api.event.dto.EventDto;
import org.teamSmurfs.backend.api.event.dto.UpdateEventRequest;

public interface EventService {

	void createEvent(CreateEventRequest eventrequest) throws Exception;

	List<EventDto> retrieveAll() throws Exception;

	void deleteEvent(final long id);

	EventDto retrieveOne(Long id);

	EventDto update(Long id, UpdateEventRequest updateEventRequest) throws Exception;

	public int getEventCountForToday();
}
