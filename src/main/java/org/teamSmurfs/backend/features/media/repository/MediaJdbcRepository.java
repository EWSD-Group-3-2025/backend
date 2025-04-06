package org.teamSmurfs.backend.features.media.repository;

import java.util.Collection;
import java.util.List;

import org.teamSmurfs.backend.features.media.dto.MediaRecord;

public interface MediaJdbcRepository {

	List<MediaRecord> findUploadById(Long id);

	Collection<MediaRecord> findMediasForThisUser(Long id);

}
