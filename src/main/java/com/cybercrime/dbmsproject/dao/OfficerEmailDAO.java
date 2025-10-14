package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.OfficerEmail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OfficerEmailDAO {

    private final JdbcTemplate jdbcTemplate;

    public OfficerEmailDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OfficerEmail> rowMapper = (rs, rowNum) -> {
        OfficerEmail obj = new OfficerEmail();
        obj.setEmail(rs.getString("email"));
        obj.setOfficerId(rs.getInt("officer_id"));
        return obj;
    };

    /** ➕ Add a new email */
    public int add(OfficerEmail email) {
        return jdbcTemplate.update(
                "INSERT INTO OfficerEmail(email, officer_id) VALUES (?, ?)",
                email.getEmail(), email.getOfficerId()
        );
    }

    /** 🔍 Get all emails for an officer */
    public List<OfficerEmail> findByOfficerId(int officerId) {
        return jdbcTemplate.query(
                "SELECT * FROM OfficerEmail WHERE officer_id=?",
                rowMapper, officerId
        );
    }

    /** ❌ Delete one email */
    public int delete(String email, int officerId) {
        return jdbcTemplate.update(
                "DELETE FROM OfficerEmail WHERE email=? AND officer_id=?",
                email, officerId
        );
    }

    /** ❌ Delete all emails for an officer */
    public int deleteAllByOfficer(int officerId) {
        return jdbcTemplate.update(
                "DELETE FROM OfficerEmail WHERE officer_id=?",
                officerId
        );
    }
}
