package com.cybercrime.dbmsproject.dao;


// import com.cybercrime.dbmsproject.model.UserEmail;
import com.cybercrime.dbmsproject.model.UserEmail; // Ensure this class exists in the specified package
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserEmailDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserEmailDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserEmail> rowMapper = (rs, rowNum) -> {
        UserEmail ue = new UserEmail();
        ue.setEmail(rs.getString("email"));
        ue.setUserId(rs.getInt("user_id"));
        return ue;
    };

    /** ➕ Add a new email for a user */
    public int add(UserEmail email) {
        String sql = "INSERT INTO UserEmail (email, user_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, email.getEmail(), email.getUserId());
    }

    /** 🔍 Get all emails for a user */
    public List<UserEmail> findByUserId(int userId) {
        String sql = "SELECT * FROM UserEmail WHERE user_id = ?";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    /** ❌ Delete one specific email */
    public int delete(String email, int userId) {
        String sql = "DELETE FROM UserEmail WHERE email = ? AND user_id = ?";
        return jdbcTemplate.update(sql, email, userId);
    }

    /** ❌ Delete all emails for a user (optional) */
    public int deleteAllByUser(int userId) {
        return jdbcTemplate.update("DELETE FROM UserEmail WHERE user_id = ?", userId);
    }
}
