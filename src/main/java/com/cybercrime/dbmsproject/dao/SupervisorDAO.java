package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.Supervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SupervisorDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for Supervisor
    private final RowMapper<Supervisor> rowMapper = new RowMapper<>() {
        @Override
        public Supervisor mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Supervisor(
                    rs.getInt("supervisor_id"),
                    rs.getInt("officer_id")
            );
        }
    };

    // Insert a record
    public int addSupervisor(Supervisor supervisor) {
        String sql = "INSERT INTO Supervisor (supervisor_id, officer_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, supervisor.getSupervisorId(), supervisor.getOfficerId());
    }

    // Get all records
    public List<Supervisor> getAllSupervisors() {
        String sql = "SELECT supervisor_id, officer_id FROM Supervisor";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // Get all officers under a specific supervisor
    public List<Supervisor> getBySupervisorId(int supervisorId) {
        String sql = "SELECT supervisor_id, officer_id FROM Supervisor WHERE supervisor_id = ?";
        return jdbcTemplate.query(sql, rowMapper, supervisorId);
    }

    // Delete a supervisor-officer relation
    public int deleteSupervisor(int supervisorId, int officerId) {
        String sql = "DELETE FROM Supervisor WHERE supervisor_id = ? AND officer_id = ?";
        return jdbcTemplate.update(sql, supervisorId, officerId);
    }
}

