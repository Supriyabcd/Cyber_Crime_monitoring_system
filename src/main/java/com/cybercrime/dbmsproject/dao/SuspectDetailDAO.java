package com.cybercrime.dbmsproject.dao;

import com.cybercrime.dbmsproject.model.SuspectDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class SuspectDetailDAO {

    private final JdbcTemplate jdbcTemplate;

    public SuspectDetailDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper for SuspectDetail
    private final RowMapper<SuspectDetail> rowMapper = new RowMapper<>() {
        @Override
        public SuspectDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            SuspectDetail s = new SuspectDetail();
            int id = rs.getInt("detail_id");
            s.setDetailId(rs.wasNull() ? null : id);
            s.setSuspectDetails(rs.getString("suspect_details"));
            return s;
        }
    };

    // Fetch all rows
    public List<SuspectDetail> findAll() {
        return jdbcTemplate.query("SELECT * FROM SuspectDetail", rowMapper);
    }

    // Fetch all suspect details for a given detail_id
    public List<SuspectDetail> findByDetailId(Integer detailId) {
        String sql = "SELECT * FROM SuspectDetail WHERE detail_id = ?";
        return jdbcTemplate.query(sql, rowMapper, detailId);
    }

    // Insert a new record
    public int save(SuspectDetail s) {
        String sql = "INSERT INTO SuspectDetail(detail_id, suspect_details) VALUES (?, ?)";
        return jdbcTemplate.update(sql, s.getDetailId(), s.getSuspectDetails());
    }

    // Update suspect_details (if needed)
    public int update(SuspectDetail s, String oldSuspectDetails) {
        String sql = "UPDATE SuspectDetail SET suspect_details = ? WHERE detail_id = ? AND suspect_details = ?";
        return jdbcTemplate.update(sql, s.getSuspectDetails(), s.getDetailId(), oldSuspectDetails);
    }

    // Delete one value
    public int delete(Integer detailId, String suspectDetails) {
        String sql = "DELETE FROM SuspectDetail WHERE detail_id = ? AND suspect_details = ?";
        return jdbcTemplate.update(sql, detailId, suspectDetails);
    }

    // Delete all suspect details for a given detail_id
    public int deleteByDetailId(Integer detailId) {
        String sql = "DELETE FROM SuspectDetail WHERE detail_id = ?";
        return jdbcTemplate.update(sql, detailId);
    }

    // Optional: get one record (if you treat [detail_id, suspect_details] as key)
    public Optional<SuspectDetail> findOne(Integer detailId, String suspectDetails) {
        String sql = "SELECT * FROM SuspectDetail WHERE detail_id = ? AND suspect_details = ?";
        List<SuspectDetail> list = jdbcTemplate.query(sql, rowMapper, detailId, suspectDetails);
        return list.stream().findFirst();
    }
}
