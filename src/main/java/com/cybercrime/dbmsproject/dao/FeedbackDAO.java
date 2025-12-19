package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.Feedback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;

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
    Integer maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(feedback_id), 0) FROM Feedback", Integer.class);
    f.setFeedbackId(maxId + 1);

    return jdbcTemplate.update(
        "INSERT INTO Feedback(feedback_id, user_id, officer_id, outcome_satisfaction, overall_experience, responsiveness, privacy_respected, feedback_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        f.getFeedbackId(), f.getUserId(), f.getOfficerId(),
        f.getOutcomeSatisfaction(), f.getOverallExperience(),
        f.getResponsiveness(), f.getPrivacyRespected(),
        f.getFeedbackDate() != null ? Date.valueOf(f.getFeedbackDate()) : null
    );
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

    /** ✏ Update feedback */
    public int update(Feedback f) {
        return jdbcTemplate.update(
                "UPDATE Feedback SET outcome_satisfaction=?, overall_experience=?, responsiveness=?, privacy_respected=?, feedback_date=? " +
                        "WHERE feedback_id=? AND user_id=? AND officer_id=?",
                f.getOutcomeSatisfaction(), f.getOverallExperience(), f.getResponsiveness(),
                f.getPrivacyRespected(), f.getFeedbackDate() != null ? Date.valueOf(f.getFeedbackDate()) : null,
                f.getFeedbackId(), f.getUserId(), f.getOfficerId()
        );
    }

    public List<Map<String, Object>> getCasesForUser(int userId) {
    String sql = """
        SELECT c.case_id, 
               c.case_name, 
               o.officer_id, 
               CONCAT(o.fname, ' ', o.lname) AS officer_name
        FROM CaseFile c
        JOIN Officer o ON c.officer_id = o.officer_id
        WHERE c.user_id = ?
    """;

    return jdbcTemplate.queryForList(sql, userId);
    }


    /** 🔍 Get feedbacks for an officer with username */
public List<Map<String, Object>> findByOfficerIdWithUserName(int officerId) {
    String sql = """
        SELECT 
            f.feedback_id,
            f.user_id,
            CONCAT(u.fname, ' ', u.lname) AS user_name,
            f.officer_id,
            f.outcome_satisfaction,
            f.overall_experience,
            f.responsiveness,
            f.privacy_respected,
            f.feedback_date
        FROM Feedback f
        JOIN User u ON f.user_id = u.user_id
        WHERE f.officer_id = ?
        ORDER BY f.feedback_date DESC
    """;
    return jdbcTemplate.queryForList(sql, officerId);
}

}