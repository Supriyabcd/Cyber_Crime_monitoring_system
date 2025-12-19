package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.Officer;
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

    // Check if an officer is a supervisor
// public boolean isSupervisor(int officerId) {
//     String sql = "SELECT COUNT(*) FROM Supervisor WHERE supervisor_id = ?";
//     Integer count = jdbcTemplate.queryForObject(sql, new Object[]{officerId}, Integer.class);
//     return count != null && count > 0;
// }



public List<Officer> getBySupervisorDomain(int supervisorId) {
    // Step 1: Find supervisor’s domain_id
    String domainSql = "SELECT domain_id FROM Officer WHERE officer_id = ?";
    Integer domainId = jdbcTemplate.queryForObject(domainSql, Integer.class, supervisorId);

    // Step 2: Fetch officers in same domain (excluding supervisor)
    String officerSql = """
        SELECT officer_id, fname, lname, domain_id 
        FROM Officer 
        WHERE domain_id = ? AND officer_id <> ?
    """;
    return jdbcTemplate.query(officerSql, (rs, rowNum) -> {
        Officer officer = new Officer();
        officer.setOfficerId(rs.getInt("officer_id"));
        officer.setFname(rs.getString("fname"));
        officer.setLname(rs.getString("lname"));
        officer.setDomainId(rs.getInt("domain_id"));
        return officer;
    }, domainId, supervisorId);
}

// ✅ Count active officers in supervisor's domain
public int getActiveOfficerCount(int supervisorId) {
    String domainSql = "SELECT domain_id FROM Officer WHERE officer_id = ?";
    Integer domainId = jdbcTemplate.queryForObject(domainSql, Integer.class, supervisorId);

    String countSql = "SELECT COUNT(*) FROM Officer WHERE domain_id = ? AND active_status = true AND officer_id <> ?";
    Integer count = jdbcTemplate.queryForObject(countSql, Integer.class, domainId, supervisorId);
    return (count != null) ? count : 0;
}


public boolean isSupervisor(int officerId) {
    String sql = "SELECT COUNT(*) FROM Supervisor WHERE supervisor_id = ?";
    Integer count = jdbcTemplate.queryForObject(sql, new Object[]{officerId}, Integer.class);
    return count != null && count > 0;
}



}