package org.teamSmurfs.backend.api.media.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.media.dto.CreateMediaRequest;
import org.teamSmurfs.backend.api.media.dto.MediaDto;
import org.teamSmurfs.backend.api.media.dto.UpdateMediaRequest;
import org.teamSmurfs.backend.api.media.model.Media;
import org.teamSmurfs.backend.api.media.model.MediaType;
import org.teamSmurfs.backend.api.media.repository.MediaRepository;
import org.teamSmurfs.backend.api.media.service.MediaService;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService{
	
	private final MediaRepository mediaRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	public void uploadMedia(CreateMediaRequest createMediaRequest) {
	try {	
		log.info("Uploading media with User ID: {}", createMediaRequest.getUserId());
		
		User user=EntityUtil.getEntityById(this.userRepository, createMediaRequest.getUserId());
		
		MediaType mediaType = MediaType.fromInt(createMediaRequest.getEntityType());
		
		if (mediaType == MediaType.INVALID) {
            throw new IllegalArgumentException("Invalid mediaType value provided.");
        }
		
		Media media = buildMediaEntity(user, createMediaRequest, mediaType.getValue(), -1L);	
		media=mediaRepository.save(media);
		media.setEntityId(media.getId());
		mediaRepository.save(media);
		
		log.info("Media uploaded successfully with ID: {}", createMediaRequest.getUserId());
	}catch(Exception e) {
		log.error("Error Uploading Media: ", e);
        throw new RuntimeException(e.getMessage());
	    }
	}

	@Override
	public List<MediaDto> retrieveAll() {
		return mediaRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
	}

	@Override
	public MediaDto retrieveOne(Long id) {
		return mapToDto(EntityUtil.getEntityById(this.mediaRepository, id));
	}

	@Override
	public MediaDto updateMedia(Long id, UpdateMediaRequest updateMediaRequest) throws Exception {
		
		try {
		    log.info("Updating Media with ID: {}", id);
		    
			final Media existMedia = EntityUtil.getEntityById(this.mediaRepository, id);
			
			if (existMedia.getEntityType() != null && !existMedia.getEntityType().equals(updateMediaRequest.getEntityType())) {    	
				MediaType mediaType = MediaType.fromInt(updateMediaRequest.getEntityType());
				
				if (mediaType == MediaType.INVALID) {
		            throw new IllegalArgumentException("Invalid mediaType value provided.");
		        }
				 existMedia.setEntityType(mediaType.getValue());				
	        } 
			   existMedia.setUserName(updateMediaRequest.getUserName());
			   existMedia.setFileType(updateMediaRequest.getFileType());
			   existMedia.setFileUrl(updateMediaRequest.getFileUrl());
			   existMedia.setStoredName(updateMediaRequest.getStoredName());
			   existMedia.setStoredUUID(updateMediaRequest.getStoredUUID());
			   existMedia.setTitle(updateMediaRequest.getTitle());	
			   existMedia.setDescription(updateMediaRequest.getDescription());
			    return mapToDto(mediaRepository.save(existMedia));
			 }catch (Exception e) {
		            log.error("Unexpected error while updating media with ID: {}", id, e);
		            throw new RuntimeException(e.getMessage());
		        }
		}

	@Override
	public void deleteMedia(long id) {
		try {
			log.info("Deleting Media with ID: {}", id);
		    Media media = EntityUtil.getEntityById(this.mediaRepository, id);
		 
		 if(media !=null ) {
			 this.mediaRepository.deleteById(id);
			 log.info("Media Deleted with ID: {} successfully!", id);
		 }
			}catch(Exception e) {
				log.error("Error Deleting Media for ID: {} - {}", id, e);
	            throw new RuntimeException("Error Deleting Media :" + e.getMessage());
			}	
		}
	
	private MediaDto mapToDto(final Media media) {
        MediaDto mediaDto = modelMapper.map(media, MediaDto.class);
        if (media.getId() != null) {
            User users = userRepository.findById(media.getUploadedBy().getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found for Media ID: " + media.getId()));
            mediaDto.setUserId(users.getId());
        }
        return mediaDto;
    }
	
	private Media buildMediaEntity(User user, CreateMediaRequest request , Integer mediaTypeValue , Long initialEntityId) {
        return new Media(
            user,
            request.getUserName(),
            request.getFileUrl(),
            LocalDateTime.now(),
            initialEntityId,
            mediaTypeValue,
            request.getFileType(),
            request.getStoredName(),
            request.getStoredUUID(),
            request.getTitle(),
            request.getDescription()
        );
    }
}
