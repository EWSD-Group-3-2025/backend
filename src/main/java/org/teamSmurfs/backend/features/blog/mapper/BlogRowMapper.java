package org.teamSmurfs.backend.features.blog.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.teamSmurfs.backend.features.blog.dto.BlogRecord;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlogRowMapper implements RowMapper<BlogRecord> {

    @Override
    public BlogRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BlogRecord(
                rs.getLong("id"),
                rs.getLong("author_id"),
                rs.getString("author_name"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toString(),
                rs.getTimestamp("updated_at").toString()
        );
    }
}
