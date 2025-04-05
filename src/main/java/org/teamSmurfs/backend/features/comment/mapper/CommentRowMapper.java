package org.teamSmurfs.backend.features.comment.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.teamSmurfs.backend.features.comment.dto.CommentRecord;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<CommentRecord> {

    @Override
    public CommentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CommentRecord(
                rs.getLong("id"),
                rs.getLong("blog_id"),
                rs.getLong("commenter_id"),
                rs.getString("commenter_name"),
                rs.getString("comment_text"),
                rs.getTimestamp("created_at").toString(),
                rs.getTimestamp("updated_at").toString()
        );
    }
}
