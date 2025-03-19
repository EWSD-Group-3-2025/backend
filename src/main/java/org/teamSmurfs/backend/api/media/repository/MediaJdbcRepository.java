package org.teamSmurfs.backend.api.media.repository;

import java.util.Collection;
import java.util.List;

import org.teamSmurfs.backend.api.media.dto.MediaRecord;

public interface MediaJdbcRepository {

	List<MediaRecord> findUploadById(Long id);

	Collection<MediaRecord> findMediasForThisUser(Long id);

}
