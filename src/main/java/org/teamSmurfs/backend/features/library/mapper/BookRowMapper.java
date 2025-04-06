package org.teamSmurfs.backend.features.library.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.teamSmurfs.backend.features.library.dto.BookRecord;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<BookRecord> {

    @Override
    public BookRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BookRecord(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("url"),
                rs.getInt("category_id"),
                rs.getString("difficulty_level"),
                rs.getInt("rating"),
                rs.getString("organization_name"),
                rs.getString("organization_url"),
                rs.getString("description"),
                rs.getLong("uploader_id"),
                rs.getString("uploader_name"),
                rs.getString("created_at"),
                rs.getString("updated_at")
        );
    }
}
