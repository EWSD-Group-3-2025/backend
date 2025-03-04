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
import org.teamSmurfs.backend.api.user.repository.UserRepository;
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
		log.info("Uploading media with User ID: {}", createMediaRequest.getUser_id());
		
		User user=EntityUtil.getEntityById(this.userRepository, createMediaRequest.getUser_id());
		
		MediaType mediaType = MediaType.fromInt(createMediaRequest.getEntityType());
		
		if (mediaType == MediaType.INVALID) {
            throw new IllegalArgumentException("Invalid mediaType value provided.");
        }
		
		Media media = buildMediaEntity(user, createMediaRequest , mediaType.getValue());	
		mediaRepository.save(media);
		
		log.info("Media uploaded successfully with ID: {}", createMediaRequest.getUser_id());
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
			
			if (existMedia.getUploadedBy().getId() != null && !existMedia.getUploadedBy().getId().equals(updateMediaRequest.getUser_id())) {    	
				 User existUser = EntityUtil.getEntityById(this.userRepository, updateMediaRequest.getUser_id());			 
				 existMedia.setUploadedBy(existUser);
	        }
			
			if (existMedia.getEntityType() != null && !existMedia.getEntityType().equals(updateMediaRequest.getEntityType())) {    	
				MediaType mediaType = MediaType.fromInt(updateMediaRequest.getEntityType());
				
				if (mediaType == MediaType.INVALID) {
		            throw new IllegalArgumentException("Invalid mediaType value provided.");
		        }
				 existMedia.setEntityType(mediaType.getValue());
				
	        }
			   existMedia.setEntityId(updateMediaRequest.getEntityId());
			   existMedia.setFileType(updateMediaRequest.getFileType());
			   existMedia.setFileUrl(updateMediaRequest.getFileUrl());
			   existMedia.setUploadedAt(LocalDateTime.now());
			  
			    
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
            mediaDto.setUser_id(users.getId());
        }
        return mediaDto;
    }
	
	private Media buildMediaEntity(User user, CreateMediaRequest request , Integer mediaTypeValue) {
        return new Media(
            user,
            request.getFileUrl(),
            LocalDateTime.now(),
            request.getEntityId(),
            mediaTypeValue,
            request.getFileType()
        );
    }
}
