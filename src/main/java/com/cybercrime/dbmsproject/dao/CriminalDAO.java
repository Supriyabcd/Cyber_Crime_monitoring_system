package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.Criminal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class CriminalDAO {

    private final JdbcTemplate jdbcTemplate;

    public CriminalDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Criminal> rowMapper = (rs, rowNum) -> {
        Criminal c = new Criminal();
        int id = rs.getInt("criminal_id");
        c.setCriminalId(rs.wasNull() ? null : id);

        c.setCaseId(rs.getInt("case_id"));
        c.setOfficerId(rs.getInt("officer_id"));
        c.setFname(rs.getString("fname"));
        c.setLname(rs.getString("lname"));

        Date dob = rs.getDate("dob");
        c.setDob(dob != null ? dob.toLocalDate() : null);

        c.setHNo(rs.getString("h_no"));
        c.setStreet(rs.getString("street"));
        c.setCity(rs.getString("city"));
        c.setState(rs.getString("state"));
        c.setMobNo(rs.getString("mob_no"));
        c.setSocialHandle(rs.getString("social_handle"));
        return c;
    };

    /** ➕ Add criminal */
    public int save(Criminal c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Criminal(case_id, officer_id, fname, lname, dob, h_no, street, city, state, mob_no, social_handle) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, c.getCaseId());
            ps.setInt(2, c.getOfficerId());
            ps.setString(3, c.getFname());
            ps.setString(4, c.getLname());
            if (c.getDob() != null) ps.setDate(5, Date.valueOf(c.getDob()));
            else ps.setNull(5, Types.DATE);
            ps.setString(6, c.getHNo());
            ps.setString(7, c.getStreet());
            ps.setString(8, c.getCity());
            ps.setString(9, c.getState());
            ps.setString(10, c.getMobNo());
            ps.setString(11, c.getSocialHandle());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            c.setCriminalId(key.intValue());
            return key.intValue();
        }
        return -1;
    }

    /** 🔍 Get all criminals */
    public List<Criminal> findAll() {
        return jdbcTemplate.query("SELECT * FROM Criminal", rowMapper);
    }

    /** 🔍 Get by composite key */
    public Optional<Criminal> findById(int criminalId, int caseId, int officerId) {
        List<Criminal> list = jdbcTemplate.query(
                "SELECT * FROM Criminal WHERE criminal_id=? AND case_id=? AND officer_id=?",
                rowMapper, criminalId, caseId, officerId
        );
        return list.stream().findFirst();
    }

    /** ✏️ Update criminal */
    public int update(Criminal c) {
        return jdbcTemplate.update(
                "UPDATE Criminal SET fname=?, lname=?, dob=?, h_no=?, street=?, city=?, state=?, mob_no=?, social_handle=? " +
                        "WHERE criminal_id=? AND case_id=? AND officer_id=?",
                c.getFname(), c.getLname(),
                c.getDob() != null ? Date.valueOf(c.getDob()) : null,
                c.getHNo(), c.getStreet(), c.getCity(), c.getState(),
                c.getMobNo(), c.getSocialHandle(),
                c.getCriminalId(), c.getCaseId(), c.getOfficerId()
        );
    }

    /** ❌ Delete criminal */
    public int delete(int criminalId, int caseId, int officerId) {
        return jdbcTemplate.update(
                "DELETE FROM Criminal WHERE criminal_id=? AND case_id=? AND officer_id=?",
                criminalId, caseId, officerId
        );
    }
}
