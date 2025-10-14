package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.AuditLog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class AuditLogDAO {

    private final JdbcTemplate jdbcTemplate;

    public AuditLogDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AuditLog> rowMapper = (rs, rowNum) -> {
        AuditLog log = new AuditLog();
        int id = rs.getInt("log_id");
        log.setLogId(rs.wasNull() ? null : id);

        Timestamp ts = rs.getTimestamp("timestamp");
        log.setTimestamp(ts != null ? ts.toLocalDateTime() : null);

        log.setPreviousStatus(rs.getString("previous_status"));
        log.setCurrentStatus(rs.getString("current_status"));
        log.setCaseId(rs.getInt("case_id"));
        return log;
    };

    /** ➕ Add new log */
    public int save(AuditLog log) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO AuditLog(previous_status, current_status, case_id) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, log.getPreviousStatus());
            ps.setString(2, log.getCurrentStatus());
            ps.setInt(3, log.getCaseId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            log.setLogId(key.intValue());
            return key.intValue();
        }
        return -1;
    }

    /** 🔍 Get all logs */
    public List<AuditLog> findAll() {
        return jdbcTemplate.query("SELECT * FROM AuditLog", rowMapper);
    }

    /** 🔍 Get logs for a case */
    public List<AuditLog> findByCaseId(int caseId) {
        return jdbcTemplate.query("SELECT * FROM AuditLog WHERE case_id=?", rowMapper, caseId);
    }

    /** ❌ Delete log */
    public int delete(int logId) {
        return jdbcTemplate.update("DELETE FROM AuditLog WHERE log_id=?", logId);
    }
}
