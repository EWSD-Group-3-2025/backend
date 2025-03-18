package org.teamSmurfs.backend.api.media.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.media.model.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {

}
