package org.teamSmurfs.backend.features.react.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.teamSmurfs.backend.features.react.dto.ReactRecord;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReactRowMapper implements RowMapper<ReactRecord> {

    @Override
    public ReactRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReactRecord(
                rs.getLong("id"),
                rs.getLong("author_id"),
                rs.getString("react"),
                rs.getLong("entity_id"),
                rs.getInt("entity_type"),
                rs.getTimestamp("created_at").toString(),
                rs.getTimestamp("updated_at").toString()
        );
    }
}
