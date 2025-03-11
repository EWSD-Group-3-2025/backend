package org.teamSmurfs.backend.api.media.service;

import java.util.List;

import org.teamSmurfs.backend.api.media.dto.CreateMediaRequest;
import org.teamSmurfs.backend.api.media.dto.MediaDto;
import org.teamSmurfs.backend.api.media.dto.UpdateMediaRequest;

public interface MediaService {

	void uploadMedia(CreateMediaRequest createMediaRequest);

	List<MediaDto> retrieveAll();

	MediaDto retrieveOne(Long id);

	MediaDto updateMedia(Long id, UpdateMediaRequest updateMediaRequest) throws Exception;

	void deleteMedia(long id);

}
