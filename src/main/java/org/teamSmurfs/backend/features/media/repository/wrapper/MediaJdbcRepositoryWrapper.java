package org.teamSmurfs.backend.features.media.repository.wrapper;

import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.features.media.dto.MediaRecord;
import org.teamSmurfs.backend.features.media.mapper.MediaRowMapper;
import org.teamSmurfs.backend.features.media.repository.MediaJdbcRepository;

@Repository
public class MediaJdbcRepositoryWrapper implements MediaJdbcRepository{

	private final JdbcTemplate jdbcTemplate;

    public MediaJdbcRepositoryWrapper(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private static final MediaRowMapper MEDIA_ROW_MAPPER = new MediaRowMapper();
    
    private static final String FIND_BY_USER_ID_QUERY = """
    	    SELECT 
    	        id AS id, 
    	        uploaded_by AS userId, 
    	        user_name AS userName, 
    	        file_url AS fileUrl, 
    	        uploaded_at AS uploadedAt, 
    	        entity_type AS entityType, 
    	        file_type AS fileType, 
    	        stored_name AS storedName, 
    	        storeduuid AS storedUUID, 
    	        title AS title, 
    	        description AS description, 
    	        created_at AS createdAt, 
    	        updated_at AS updatedAt 
    	    FROM media 
    	    WHERE uploaded_by = ?
    	""";
    
    private static final String FIND_MEDEAS_FOR_THIS_USER_QUERY = """
            SELECT
                m.id AS id,
                m.uploaded_by AS userId,
                m.user_name AS userName,
                m.file_url AS fileUrl, 
    	        m.uploaded_at AS uploadedAt, 
    	        m.entity_type AS entityType, 
    	        m.file_type AS fileType, 
    	        m.stored_name AS storedName, 
    	        m.storeduuid AS storedUUID, 
    	        m.title AS title, 
    	        m.description AS description, 
    	        m.created_at AS createdAt, 
    	        m.updated_at AS updatedAt 
            FROM
                media m
            LEFT JOIN
                user u ON u.id = m.uploaded_by
            WHERE 
                m.uploaded_by IN (
                    SELECT t.user_id 
                    FROM tutor t
                    JOIN allocation a ON t.id = a.tutor_id
                    JOIN student s ON a.student_id = s.id
                    WHERE s.user_id = ?
                )
                OR
                m.uploaded_by IN (
                    SELECT s.user_id 
                    FROM student s
                    JOIN allocation a ON s.id = a.student_id
                    JOIN tutor t ON a.tutor_id = t.id
                    WHERE t.user_id = ?
                )
                OR
                NOT EXISTS (SELECT 1 FROM student WHERE user_id = ?)
                AND
                NOT EXISTS (SELECT 1 FROM tutor WHERE user_id = ?)
        """;

	@Override
	public List<MediaRecord> findUploadById(Long userId) {
		return jdbcTemplate.query(FIND_BY_USER_ID_QUERY, MEDIA_ROW_MAPPER, userId);
	}

	@Override
	public Collection<MediaRecord> findMediasForThisUser(Long userId) {
		 return jdbcTemplate.query(FIND_MEDEAS_FOR_THIS_USER_QUERY, MEDIA_ROW_MAPPER, userId, userId, userId, userId);
	}
}
