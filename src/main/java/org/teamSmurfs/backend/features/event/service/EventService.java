package org.teamSmurfs.backend.features.event.service;

import java.util.List;

import org.teamSmurfs.backend.features.event.dto.CreateEventRequest;
import org.teamSmurfs.backend.features.event.dto.EventDto;
import org.teamSmurfs.backend.features.event.dto.UpdateEventRequest;

public interface EventService {

	void createEvent(CreateEventRequest eventrequest) throws Exception;

	List<EventDto> retrieveAll() throws Exception;

	void deleteEvent(final long id);

	EventDto retrieveOne(Long id);

	EventDto update(Long id, UpdateEventRequest updateEventRequest) throws Exception;

	public int getEventCountForToday();
}
