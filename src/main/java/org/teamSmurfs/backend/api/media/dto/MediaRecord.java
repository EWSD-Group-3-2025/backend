package org.teamSmurfs.backend.api.media.dto;

public record MediaRecord( 
		
Long id,
Long userId,
String userName,
String fileUrl,
String uploadedAt,
Integer entityType,
String fileType,
String storedName,
String storedUUID,
String title,
String description,
String createdAt,
String updatedAt) {}
