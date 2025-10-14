package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.CaseFile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CaseFileDAO {

    private final JdbcTemplate jdbcTemplate;

    public CaseFileDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CaseFile> rowMapper = (rs, rowNum) -> {
        CaseFile c = new CaseFile();
        int id = rs.getInt("case_id");
        c.setCaseId(rs.wasNull() ? null : id);

        c.setCaseName(rs.getString("case_name"));

        Date createdOn = rs.getDate("created_on");
        c.setCreatedOn(createdOn != null ? createdOn.toLocalDate() : null);

        c.setCurrentStatus(rs.getString("current_status"));

        int userId = rs.getInt("user_id");
        c.setUserId(rs.wasNull() ? null : userId);

        int officerId = rs.getInt("officer_id");
        c.setOfficerId(rs.wasNull() ? null : officerId);

        int domainId = rs.getInt("domain_id");
        c.setDomainId(rs.wasNull() ? null : domainId);


        c.setDetailedDescription(rs.getString("detailed_description"));

        c.setImagePaths(rs.getString("image_paths"));


        return c;
    };

    /** ➕ Add new case */
public int save(CaseFile c) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(conn -> {
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO CaseFile(case_name, detailed_description, image_paths, created_on, current_status, user_id, officer_id, domain_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, c.getCaseName());
        ps.setString(2, c.getDetailedDescription());
        ps.setString(3, c.getImagePaths()); // store as JSON string

        if (c.getCreatedOn() != null) ps.setDate(4, Date.valueOf(c.getCreatedOn()));
        else ps.setNull(4, Types.DATE);

        ps.setString(5, c.getCurrentStatus());
        if (c.getUserId() != null) ps.setInt(6, c.getUserId()); else ps.setNull(6, Types.INTEGER);
        if (c.getOfficerId() != null) ps.setInt(7, c.getOfficerId()); else ps.setNull(7, Types.INTEGER);
        if (c.getDomainId() != null) ps.setInt(8, c.getDomainId()); else ps.setNull(8, Types.INTEGER);

        return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key != null) {
        c.setCaseId(key.intValue());
        return key.intValue();
    }
    return -1;
}


    /** 🔍 Get all cases */
    public List<CaseFile> findAll() {
        return jdbcTemplate.query("SELECT * FROM CaseFile", rowMapper);
    }

    /** 🔍 Get case by ID */
    public Optional<CaseFile> findById(int caseId) {
        List<CaseFile> list = jdbcTemplate.query(
                "SELECT * FROM CaseFile WHERE case_id=?",
                rowMapper, caseId
        );
        return list.stream().findFirst();
    }

    /** ✏️ Update case */
    public int update(CaseFile c) {
        return jdbcTemplate.update(
                "UPDATE CaseFile SET case_name=?, created_on=?, current_status=?, user_id=?, officer_id=?, domain_id=? " +
                        "WHERE case_id=?",
                c.getCaseName(),
                c.getCreatedOn() != null ? Date.valueOf(c.getCreatedOn()) : null,
                c.getCurrentStatus(),
                c.getUserId(), c.getOfficerId(), c.getDomainId(),
                c.getCaseId()
        );
    }

    /** ❌ Delete case */
    public int delete(int caseId) {
        return jdbcTemplate.update("DELETE FROM CaseFile WHERE case_id=?", caseId);
    }

        // Count total cases for a user
        public int countByUserId(int userId) {
            String sql = "SELECT COUNT(*) FROM CaseFile WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        }

        // Count cases by status for a user
        public int countByUserIdAndStatus(int userId, String status) {
            String sql = "SELECT COUNT(*) FROM CaseFile WHERE user_id = ? AND current_status = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, status);
        }

        // Get recent N cases
        public List<CaseFile> findRecentByUserId(int userId, int limit) {
            String sql = "SELECT * FROM CaseFile WHERE user_id = ? ORDER BY created_on DESC LIMIT ?";
            return jdbcTemplate.query(sql, rowMapper, userId, limit);
        }



}
