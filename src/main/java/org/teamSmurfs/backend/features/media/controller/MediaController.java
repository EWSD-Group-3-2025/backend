package org.teamSmurfs.backend.features.media.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.teamSmurfs.backend.features.media.dto.CreateMediaRequest;
import org.teamSmurfs.backend.features.media.dto.MediaDto;
import org.teamSmurfs.backend.features.media.dto.UpdateMediaRequest;
import org.teamSmurfs.backend.features.media.service.MediaService;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/media")
@RequiredArgsConstructor
@Slf4j
public class MediaController {
	
	private final MediaService mediaService;
	
	@PostMapping
	public ResponseEntity<ApiResponse> uploadMedia(@Validated @RequestBody CreateMediaRequest createMediaRequest,
			HttpServletRequest request){
		log.warn("Create Media with User ID: {}" , createMediaRequest.getUserId());
		
		double requestStartTime = RequestUtils.extractRequestStartTime(request);	
		mediaService.uploadMedia(createMediaRequest);
		ApiResponse response = ApiResponse.builder()
				.success(1)
				.code(HttpStatus.CREATED.value())
                .data(true)
                .message("Media uploaded successfully")
                .build();
		return ResponseUtil.buildResponse(request, response, requestStartTime);
		
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse> retrieveAllMedia(
			@RequestHeader(value = "Authorization", required = false) final String authHeader,
            @RequestParam(value = "shared", defaultValue = "false") final boolean shared,
			HttpServletRequest request) throws Exception{
		List<MediaDto> mediaList;
		log.info("Retrieving all media");
		 double requestStartTime = RequestUtils.extractRequestStartTime(request);
		 
		 String maskedAuthHeader = authHeader != null ? authHeader.substring(0, Math.min(10, authHeader.length())) + "***" : "N/A";
	      log.info("Processing reset password for Authorization: {}", maskedAuthHeader);
		 
		 if(shared) {
			 mediaList = this.mediaService.retrieveMediasForThisUser(authHeader);
		 }else {
		     mediaList = this.mediaService.retrieveMediasByThisUser(authHeader);
		 }
		 
		 ApiResponse successResponse = ApiResponse.builder()
	                .success(1) 
	                .code(HttpStatus.OK.value())
	                .data(mediaList !=null ? mediaList : Collections.emptyList())
	                .message("All Media retrieved successfully")
	                .build();
		return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
		
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveMedia(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Retrieving Media with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        MediaDto mediaDto = mediaService.retrieveOne(id);

        log.info("Retrieved Media successfully: {}", mediaDto);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(mediaDto)
                .message("Media retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
	
	@PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMedia(
            @PathVariable(value = "id") final Long id,
            @RequestBody final UpdateMediaRequest updateMediaRequest,
            final HttpServletRequest request

    ) throws Exception {
        log.info("Updating Media with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        MediaDto updatedMedia = mediaService.updateMedia(id, updateMediaRequest);

        log.info("Updated Media successfully: {}", updatedMedia.getId());

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Media updated successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMedia(@PathVariable(value = "id") final long id,
		HttpServletRequest request){
	log.info("Deleting Media with ID: {}" , id);
    double requestStartTime = RequestUtils.extractRequestStartTime(request);
	mediaService.deleteMedia(id);
	 ApiResponse successResponse = ApiResponse.builder()
     .success(1)
     .code(HttpStatus.NO_CONTENT.value())
     .data(true)
     .message("Media deleted successfully")
     .build();

     return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
}
	
}
