package org.teamSmurfs.backend.features.media.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.teamSmurfs.backend.features.media.dto.MediaRecord;

public class MediaRowMapper implements RowMapper<MediaRecord>{

	@Override
	public MediaRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new MediaRecord(
				rs.getLong("id"),
				rs.getLong("userId"),
				rs.getString("userName"),
				rs.getString("fileUrl"),
				rs.getTimestamp("uploadedAt").toString(),
				rs.getInt("entityType"),
				rs.getString("fileType"),
				rs.getString("storedName"),
				rs.getString("storedUUID"),
				rs.getString("title"),
				rs.getString("description"),
				rs.getTimestamp("createdAt").toString(),
				rs.getTimestamp("updatedAt").toString()
				);
	}

}
