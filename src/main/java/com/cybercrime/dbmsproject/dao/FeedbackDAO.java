package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.Feedback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class FeedbackDAO {

    private final JdbcTemplate jdbcTemplate;

    public FeedbackDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Feedback> rowMapper = (rs, rowNum) -> {
        Feedback f = new Feedback();
        int id = rs.getInt("feedback_id");
        f.setFeedbackId(rs.wasNull() ? null : id);
        f.setUserId(rs.getInt("user_id"));
        f.setOfficerId(rs.getInt("officer_id"));
        f.setOutcomeSatisfaction(rs.getInt("outcome_satisfaction"));
        f.setOverallExperience(rs.getInt("overall_experience"));
        f.setResponsiveness(rs.getInt("responsiveness"));
        f.setPrivacyRespected(rs.getBoolean("privacy_respected"));

        Date feedbackDate = rs.getDate("feedback_date");
        f.setFeedbackDate(feedbackDate != null ? feedbackDate.toLocalDate() : null);
        return f;
    };

    /** ➕ Add feedback and return generated feedback_id */
    public int save(Feedback f) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Feedback(user_id, officer_id, outcome_satisfaction, overall_experience, responsiveness, privacy_respected, feedback_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, f.getUserId());
            ps.setInt(2, f.getOfficerId());
            ps.setInt(3, f.getOutcomeSatisfaction());
            ps.setInt(4, f.getOverallExperience());
            ps.setInt(5, f.getResponsiveness());
            if (f.getPrivacyRespected() != null) ps.setBoolean(6, f.getPrivacyRespected());
            else ps.setNull(6, Types.BOOLEAN);
            if (f.getFeedbackDate() != null) ps.setDate(7, Date.valueOf(f.getFeedbackDate()));
            else ps.setNull(7, Types.DATE);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            f.setFeedbackId(key.intValue());
            return key.intValue();
        }
        return -1;
    }

    /** 🔍 Get all feedbacks */
    public List<Feedback> findAll() {
        return jdbcTemplate.query("SELECT * FROM Feedback", rowMapper);
    }

    /** 🔍 Get feedbacks by user */
    public List<Feedback> findByUserId(int userId) {
        return jdbcTemplate.query("SELECT * FROM Feedback WHERE user_id=?", rowMapper, userId);
    }

    /** 🔍 Get feedbacks for an officer */
    public List<Feedback> findByOfficerId(int officerId) {
        return jdbcTemplate.query("SELECT * FROM Feedback WHERE officer_id=?", rowMapper, officerId);
    }

    /** ❌ Delete feedback */
    public int delete(int feedbackId, int userId, int officerId) {
        return jdbcTemplate.update(
                "DELETE FROM Feedback WHERE feedback_id=? AND user_id=? AND officer_id=?",
                feedbackId, userId, officerId
        );
    }

    /** ✏️ Update feedback */
    public int update(Feedback f) {
        return jdbcTemplate.update(
                "UPDATE Feedback SET outcome_satisfaction=?, overall_experience=?, responsiveness=?, privacy_respected=?, feedback_date=? " +
                        "WHERE feedback_id=? AND user_id=? AND officer_id=?",
                f.getOutcomeSatisfaction(), f.getOverallExperience(), f.getResponsiveness(),
                f.getPrivacyRespected(), f.getFeedbackDate() != null ? Date.valueOf(f.getFeedbackDate()) : null,
                f.getFeedbackId(), f.getUserId(), f.getOfficerId()
        );
    }
}
