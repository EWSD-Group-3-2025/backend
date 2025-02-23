package org.teamSmurfs.backend.api.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
