package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.Detail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class DetailDAO {

    private final JdbcTemplate jdbcTemplate;

    public DetailDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Detail> rowMapper = (rs, rowNum) -> {
        Detail d = new Detail();
        int id = rs.getInt("detail_id");
        d.setDetailId(rs.wasNull() ? null : id);

        d.setPlatformName(rs.getString("platform_name"));
        d.setPersonalDetails(rs.getString("personal_details"));
        d.setSuspectDetails(rs.getString("suspect_details"));

        int userId = rs.getInt("user_id");
        d.setUserId(rs.wasNull() ? null : userId);

        int caseId = rs.getInt("case_id");
        d.setCaseId(rs.wasNull() ? null : caseId);

        return d;
    };

    /** ➕ Add detail */
    public int save(Detail d) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Detail(platform_name, personal_details, suspect_details, user_id, case_id) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, d.getPlatformName());
            ps.setString(2, d.getPersonalDetails());
            ps.setString(3, d.getSuspectDetails());
            if (d.getUserId() != null) ps.setInt(4, d.getUserId());
            else ps.setNull(4, Types.INTEGER);
            if (d.getCaseId() != null) ps.setInt(5, d.getCaseId());
            else ps.setNull(5, Types.INTEGER);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            d.setDetailId(key.intValue());
            return key.intValue();
        }
        return -1;
    }

    /** 🔍 Get all details */
    public List<Detail> findAll() {
        return jdbcTemplate.query("SELECT * FROM Detail", rowMapper);
    }

    /** 🔍 Get by ID */
    public Optional<Detail> findById(int detailId) {
        List<Detail> list = jdbcTemplate.query(
                "SELECT * FROM Detail WHERE detail_id=?",
                rowMapper, detailId
        );
        return list.stream().findFirst();
    }

    /** ✏️ Update detail */
    public int update(Detail d) {
        return jdbcTemplate.update(
                "UPDATE Detail SET platform_name=?, personal_details=?, suspect_details=?, user_id=?, case_id=? WHERE detail_id=?",
                d.getPlatformName(), d.getPersonalDetails(), d.getSuspectDetails(),
                d.getUserId(), d.getCaseId(), d.getDetailId()
        );
    }

    /** ❌ Delete detail */
    public int delete(int detailId) {
        return jdbcTemplate.update("DELETE FROM Detail WHERE detail_id=?", detailId);
    }
}


