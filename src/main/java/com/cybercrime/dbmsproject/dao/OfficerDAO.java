package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.Officer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OfficerDAO {
    private final JdbcTemplate jdbcTemplate;

    public OfficerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Fetch all officers
    public List<Officer> findAll() {
        String sql = "SELECT * FROM Officer";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Officer(
                        rs.getInt("officer_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("password"),
                        rs.getString("mob_no"),
                        rs.getBoolean("active_status"),
                        rs.getString("join_date"),
                        rs.getObject("domain_id") != null ? rs.getInt("domain_id") : null
                )
        );
    }

    // Insert officer
    public int save(Officer officer) {
        String sql = "INSERT INTO Officer (fname, lname, password, mob_no, active_status, join_date, domain_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                officer.getFname(),
                officer.getLname(),
                officer.getPassword(),
                officer.getMobNo(),
                officer.isActiveStatus(),
                officer.getJoinDate(),
                officer.getDomainId()
        );
    }

    // Add this method below your save() method
    public Officer findByOfficerId(int officerId) {
        String sql = "SELECT * FROM Officer WHERE officer_id = ?";
        List<Officer> officers = jdbcTemplate.query(sql, new Object[]{officerId}, (rs, rowNum) ->
                new Officer(
                        rs.getInt("officer_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("password"),
                        rs.getString("mob_no"),
                        rs.getBoolean("active_status"),
                        rs.getString("join_date"),
                        rs.getObject("domain_id") != null ? rs.getInt("domain_id") : null
                )
        );
        return officers.isEmpty() ? null : officers.get(0);
    }

    // Get officers by domain
public List<Officer> findByDomainId(int domainId) {
    String sql = "SELECT * FROM Officer WHERE domain_id = ?";
    return jdbcTemplate.query(sql, (rs, rowNum) ->
        new Officer(
            rs.getInt("officer_id"),
            rs.getString("fname"),
            rs.getString("lname"),
            rs.getString("password"),
            rs.getString("mob_no"),
            rs.getBoolean("active_status"),
            rs.getString("join_date"),
            rs.getObject("domain_id") != null ? rs.getInt("domain_id") : null
        )
    , domainId);
}


}