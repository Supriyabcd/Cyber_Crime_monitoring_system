package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.Domain;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DomainDAO {
    private final JdbcTemplate jdbcTemplate;

    public DomainDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 🔹 Fetch all domains
    public List<Domain> findAll() {
        String sql = "SELECT * FROM Domain";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Domain(
                        rs.getInt("domain_id"),
                        rs.getString("domain_name"),
                        rs.getString("created_on"),
                        rs.getBoolean("is_active")
                )
        );
    }

    // 🔹 Insert a new domain
    public int save(Domain domain) {
        String sql = "INSERT INTO Domain (domain_name, created_on, is_active) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, domain.getDomainName(), domain.getCreatedOn(), domain.isActive());
    }

    // 🔹 Fetch domain_id by name (safe Optional version)
    public Optional<Integer> findIdByName(String name) {
    String sql = "SELECT domain_id FROM Domain WHERE domain_name = ?";
    List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("domain_id"), name);
    return ids.stream().findFirst();
}

}
