package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.UserMName;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserMNameDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserMNameDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // insert a middle name
    public int save(UserMName userMName) {
        String sql = "INSERT INTO UserMName (user_id, m_name) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userMName.getUserId(), userMName.getMName());
    }
}
