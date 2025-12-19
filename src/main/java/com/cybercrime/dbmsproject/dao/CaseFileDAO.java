package com.cybercrime.dbmsproject.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import java.util.Collections;


import com.cybercrime.dbmsproject.model.CaseFile;
import com.cybercrime.dbmsproject.model.Officer;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CaseFileDAO {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    

    public CaseFileDAO(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<CaseFile> rowMapper = (rs, rowNum) -> {
        CaseFile c = new CaseFile();
        int id = rs.getInt("case_id");
        c.setCaseId(rs.wasNull() ? null : id);

        c.setCaseName(rs.getString("case_name"));

        Date createdOn = rs.getDate("created_on");
        c.setCreatedOn(createdOn != null ? createdOn.toLocalDate() : null);

        c.setCurrentStatus(rs.getString("current_status"));

        int userId = rs.getInt("user_id");
        c.setUserId(rs.wasNull() ? null : userId);

        int officerId = rs.getInt("officer_id");
        c.setOfficerId(rs.wasNull() ? null : officerId);

        int domainId = rs.getInt("domain_id");
        c.setDomainId(rs.wasNull() ? null : domainId);


        c.setDetailedDescription(rs.getString("detailed_description"));

        c.setImagePaths(rs.getString("image_paths"));

        // ✅ New line for officer name
        // c.setOfficerName(rs.getString("officer_name"));


        return c;
    };

    /** ➕ Add new case */
public int save(CaseFile c) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(conn -> {
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO CaseFile(case_name, detailed_description, image_paths, created_on, current_status, user_id, officer_id, domain_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, c.getCaseName());
        ps.setString(2, c.getDetailedDescription());
        ps.setString(3, c.getImagePaths()); // store as JSON string

        if (c.getCreatedOn() != null) ps.setDate(4, Date.valueOf(c.getCreatedOn()));
        else ps.setNull(4, Types.DATE);

        ps.setString(5, c.getCurrentStatus());
        if (c.getUserId() != null) ps.setInt(6, c.getUserId()); else ps.setNull(6, Types.INTEGER);
        if (c.getOfficerId() != null) ps.setInt(7, c.getOfficerId()); else ps.setNull(7, Types.INTEGER);
        if (c.getDomainId() != null) ps.setInt(8, c.getDomainId()); else ps.setNull(8, Types.INTEGER);

        return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key != null) {
        c.setCaseId(key.intValue());
        return key.intValue();
    }
    return -1;
}


    /** 🔍 Get all cases */
    public List<CaseFile> findAll() {
        String sql = "SELECT c.*, CONCAT(o.fname, ' ', o.lname) AS officer_name " +
                    "FROM CaseFile c " +
                    "LEFT JOIN Officer o ON c.officer_id = o.officer_id";
        return jdbcTemplate.query(sql, rowMapper);
    }


    /** 🔍 Get case by ID */
    // public Optional<CaseFile> findById(int caseId) {
    //     List<CaseFile> list = jdbcTemplate.query(
    //             "SELECT * FROM CaseFile WHERE case_id=?",
    //             rowMapper, caseId
    //     );
    //     return list.stream().findFirst();
    // }

    public Optional<CaseFile> findById(int caseId) {
    String sql = """
        SELECT c.*, CONCAT(o.fname, ' ', o.lname) AS officer_name
        FROM CaseFile c
        LEFT JOIN Officer o ON c.officer_id = o.officer_id
        WHERE c.case_id = ?
    """;
    List<CaseFile> list = jdbcTemplate.query(sql, rowMapper, caseId);
    return list.stream().findFirst();
}


    /** ✏️ Update case */
    public int update(CaseFile c) {
        return jdbcTemplate.update(
                "UPDATE CaseFile SET case_name=?, created_on=?, current_status=?, user_id=?, officer_id=?, domain_id=? " +
                        "WHERE case_id=?",
                c.getCaseName(),
                c.getCreatedOn() != null ? Date.valueOf(c.getCreatedOn()) : null,
                c.getCurrentStatus(),
                c.getUserId(), c.getOfficerId(), c.getDomainId(),
                c.getCaseId()
        );
    }

    /** ❌ Delete case */
    public int delete(int caseId) {
        return jdbcTemplate.update("DELETE FROM CaseFile WHERE case_id=?", caseId);
    }

        // Count total cases for a user
        public int countByUserId(int userId) {
            String sql = "SELECT COUNT(*) FROM CaseFile WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        }

        // Count cases by status for a user
        public int countByUserIdAndStatus(int userId, String status) {
            String sql = "SELECT COUNT(*) FROM CaseFile WHERE user_id = ? AND current_status = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, status);
        }
        
        public int countByUserIdAndNotStatus(int userId, String excludedStatus) {
            String sql = "SELECT COUNT(*) FROM CaseFile WHERE user_id = ? AND current_status <> ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, excludedStatus);
        }



        // Get recent N cases

    //     public List<CaseFile> findRecentByUserId(int userId) {
    //     String sql = "SELECT * FROM casefile WHERE user_id = ? ORDER BY created_on DESC LIMIT 5";
    //     return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CaseFile.class), userId);
    // }

    public List<CaseFile> findRecentByUserId(int userId, int limit) {
    String sql = "SELECT * FROM casefile WHERE user_id = ? ORDER BY created_on DESC LIMIT " + limit;
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CaseFile.class), userId);
}




    // public List<CaseFile> findAllByUserId(Integer userId) {
    //     String sql = "SELECT c.*, CONCAT(o.fname, ' ', o.lname) AS officer_name " +
    //                 "FROM CaseFile c " +
    //                 "LEFT JOIN Officer o ON c.officer_id = o.officer_id " +
    //                 "WHERE c.user_id = ? ORDER BY c.created_on DESC";
    //     return jdbcTemplate.query(sql, rowMapper, userId);
    // }

    public List<CaseFile> findAllByUserId(Integer userId) {
    String sql = """
        SELECT 
            c.*, 
            COALESCE(CONCAT(o.fname, ' ', o.lname), 'Unassigned') AS officer_name
        FROM CaseFile c
        LEFT JOIN Officer o ON c.officer_id = o.officer_id
        WHERE c.user_id = ?
        ORDER BY c.created_on DESC
        """;

    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CaseFile.class), userId);
}


    // public List<CaseFile> findByOfficerId(int officerId) {
    //         String sql = "SELECT * FROM CaseFile WHERE officer_id = ?";
    //         return jdbcTemplate.query(sql, rowMapper, officerId);
    //     }

    public List<CaseFile> findByOfficerId(int officerId) {
    String sql = """
        SELECT c.*, CONCAT(o.fname, ' ', o.lname) AS officer_name
        FROM CaseFile c
        LEFT JOIN Officer o ON c.officer_id = o.officer_id
        WHERE c.officer_id = ?
        ORDER BY c.created_on DESC
    """;
    return jdbcTemplate.query(sql, rowMapper, officerId);
}

public int countCasesByStatus(int officerId, String status) {
    String sql = "SELECT COUNT(*) FROM CaseFile WHERE officer_id = ? AND current_status = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, officerId, status);
    return count != null ? count : 0;
}

public List<CaseFile> findRecentByOfficerId(int officerId, int limit) {
    String sql = "SELECT c.*, CONCAT(o.fname, ' ', o.lname) AS officer_name " +
                 "FROM CaseFile c " +
                 "LEFT JOIN Officer o ON c.officer_id = o.officer_id " +
                 "WHERE c.officer_id = ? " +
                 "ORDER BY c.created_on DESC " +
                 "LIMIT ?";
    return jdbcTemplate.query(sql, rowMapper, officerId, limit);
}

public int countCasesByOfficerAndStatus(int officerId, String status) {
    String sql = "SELECT COUNT(*) FROM CaseFile WHERE officer_id = ? AND current_status = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, officerId, status);
    return count != null ? count : 0;
}

public int countCasesByOfficerAndNotStatus(int officerId, String excludedStatus) {
    String sql = "SELECT COUNT(*) FROM CaseFile WHERE officer_id = ? AND current_status <> ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, officerId, excludedStatus);
    return count != null ? count : 0;
}

public void updateCaseStatus(int caseId, String newStatus) {
    String sql = "UPDATE casefile SET current_status = ? WHERE case_id = ?";
    jdbcTemplate.update(sql, newStatus, caseId);
}

// Get all cases in a domain
public List<CaseFile> findByDomainId(int domainId) {
    String sql = "SELECT * FROM CaseFile WHERE domain_id = ?";
    return jdbcTemplate.query(sql, rowMapper, domainId);
}


/**
 * Fetch cases assigned to an officer that have specific statuses (e.g., "Investigation Completed", "Charge Sheet Filed").
 */
public List<CaseFile> findByOfficerIdAndStatuses(int officerId, List<String> statuses) {
    if (statuses == null || statuses.isEmpty()) {
        return Collections.emptyList();
    }

    String sql = """
        SELECT c.*, CONCAT(o.fname, ' ', o.lname) AS officer_name
        FROM CaseFile c
        LEFT JOIN Officer o ON c.officer_id = o.officer_id
        WHERE c.officer_id = :officerId
          AND c.current_status IN (:statuses)
        ORDER BY c.created_on DESC
    """;

    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("officerId", officerId)
            .addValue("statuses", statuses);

    return namedParameterJdbcTemplate.query(sql, params, rowMapper);
}

public int assignOfficerToCase(int caseId, int officerId) {
    String sql = "UPDATE CaseFile SET officer_id = ?, current_status = 'Assigned' WHERE case_id = ?";
    return jdbcTemplate.update(sql, officerId, caseId);
}


public int getCaseCountByDomain(int domainID) {
       String sql = "SELECT COUNT(*) FROM CaseFile WHERE domain_id = ?";
       Integer count = jdbcTemplate.queryForObject(sql, Integer.class, domainID);
       return (count != null) ? count : 0;
     }
    
     public int getActiveCaseCount(int domainId) {
    String sql = "SELECT COUNT(*) FROM CaseFile WHERE domain_id = ? AND current_status <> 'completed'";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, domainId);
    return (count != null) ? count : 0;
    }
     
    public double getCaseClosureRate(int domainId) {
    // Count total assigned cases (exclude unassigned)
    String totalSql = "SELECT COUNT(*) FROM CaseFile WHERE domain_id = ? AND current_status <> 'unassigned'";
    Integer totalCases = jdbcTemplate.queryForObject(totalSql, Integer.class, domainId);

    // Count completed cases
    String completedSql = "SELECT COUNT(*) FROM CaseFile WHERE domain_id = ? AND current_status = 'completed'";
    Integer completedCases = jdbcTemplate.queryForObject(completedSql, Integer.class, domainId);

    // Prevent division by zero
    if (totalCases == null || totalCases == 0) {
        return 0.0;
    }

    // Calculate closure rate percentage
    double rate = (completedCases != null ? completedCases : 0) * 100.0 / totalCases;

    return Math.round(rate * 100.0) / 100.0; // round to 2 decimal places
   }

    public int getUnassignedCaseCount(int domainId) {
    String sql = "SELECT COUNT(*) FROM CaseFile WHERE domain_id = ? AND current_status = 'unassigned'";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, domainId);
    return (count != null) ? count : 0;
    }  






        



}




