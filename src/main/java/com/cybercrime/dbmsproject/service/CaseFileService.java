

package com.cybercrime.dbmsproject.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import java.util.List;

import com.cybercrime.dbmsproject.dao.AuditLogDAO;
import com.cybercrime.dbmsproject.model.AuditLog;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;


import com.cybercrime.dbmsproject.dao.CaseFileDAO;

import com.cybercrime.dbmsproject.dao.OfficerDAO;
import com.cybercrime.dbmsproject.model.CaseFile;
import com.cybercrime.dbmsproject.model.Detail;
import com.cybercrime.dbmsproject.model.Domain;
import com.cybercrime.dbmsproject.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CaseFileService {

    @Autowired
    private CaseFileDAO caseFileDAO;

    @Autowired
    private DetailService detailService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private UserService userService;

    @Autowired
private AuditLogDAO auditLogDAO;


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<CaseFile> getCasesByOfficerId(int officerId) {
        return caseFileDAO.findByOfficerId(officerId);
    }


    //Might cause error 

//     public Map<String, Object> getFullCaseDetails(int caseId) {
//     Map<String, Object> result = new HashMap<>();

//     // 🧩 1. Case info
//     Optional<CaseFile> caseOpt = caseFileDAO.findById(caseId);
//     if (caseOpt.isEmpty()) {
//         return result; // return empty map if not found
//     }

//     CaseFile caseFile = caseOpt.get();
//     result.put("case", caseFile);

//     // 🧩 2. Domain info
//     Domain domain = (caseFile.getDomainId() != null)
//             ? domainService.getDomainById(caseFile.getDomainId())
//             : null;
//     result.put("domain", domain);

//     // 🧩 3. User info
//     UserDetail user = (caseFile.getUserId() != null)
//             ? userService.getUserById(caseFile.getUserId())
//             : null;
//     result.put("user", user);

//     // 🧩 4. Detail info
//     List<Detail> details = detailService.getDetailsByCaseId(caseId);
//     result.put("details", details);

//     return result;
// }


 //Might cause error 





// public Map<String, Object> getFullCaseDetails(int caseId) {
//     Map<String, Object> result = new HashMap<>();

//     // 1️⃣ Case info
//     Optional<CaseFile> caseOpt = caseFileDAO.findById(caseId);
//     if (caseOpt.isEmpty()) {
//         return result; // empty map if not found
//     }
//     CaseFile caseFile = caseOpt.get();
//     result.put("case", caseFile);

//     // 2️⃣ Domain info
//     Domain domain = (caseFile.getDomainId() != null)
//             ? domainService.getDomainById(caseFile.getDomainId())
//             : null;
//     result.put("domain", domain != null ? domain.getDomainName() : null);

//     // 3️⃣ User info (only id and username)
//     if (caseFile.getUserId() != null) {
//         UserDetail user = userService.getUserById(caseFile.getUserId());
//         if (user != null) {
//             Map<String, Object> userMap = new HashMap<>();
//             userMap.put("userId", user.getUserId());
//             userMap.put("username", user.getUsername());
//             result.put("user", userMap);
//         } else {
//             result.put("user", null);
//         }
//     } else {
//         result.put("user", null);
//     }

//     // 4️⃣ Case details
//     List<Detail> details = detailService.getDetailsByCaseId(caseId);
//     result.put("details", details);

//     return result;
// }

public Map<String, Integer> getOfficerStats(int officerId) {
    Map<String, Integer> stats = new HashMap<>();

    stats.put("assigned", caseFileDAO.countCasesByStatus(officerId, "assigned"));
    stats.put("active", caseFileDAO.countCasesByStatus(officerId, "under review"));
    stats.put("pending", caseFileDAO.countCasesByStatus(officerId, "not resolved"));
    stats.put("closed", caseFileDAO.countCasesByStatus(officerId, "resolved"));

    return stats;
}

public List<CaseFile> getRecentCasesByOfficer(int officerId, int limit) {
    return caseFileDAO.findRecentByOfficerId(officerId, limit);
}

public int countCasesByOfficerAndStatus(int officerId, String status) {
        return caseFileDAO.countCasesByOfficerAndStatus(officerId, status);
    }
public int countCasesByOfficerAndNotStatus(int officerId, String status) {
        return caseFileDAO.countCasesByOfficerAndNotStatus(officerId, status);
    }



public Map<String, Object> getFullCaseDetails(int caseId) {
    Map<String, Object> result = new HashMap<>();

    // 1️⃣ Case info
    Optional<CaseFile> caseOpt = caseFileDAO.findById(caseId);
    if (caseOpt.isEmpty()) {
        return result; // empty map if not found
    }
    CaseFile caseFile = caseOpt.get();
    result.put("case", caseFile);

    // 2️⃣ Domain info
    Domain domain = (caseFile.getDomainId() != null)
            ? domainService.getDomainById(caseFile.getDomainId())
            : null;
    result.put("domain", domain != null ? domain.getDomainName() : null);

    // 3️⃣ User info (only id and username)
    if (caseFile.getUserId() != null) {
        UserDetail user = userService.getUserById(caseFile.getUserId());
        if (user != null) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getUserId());
            userMap.put("username", user.getUsername());
            result.put("user", userMap);
        } else {
            result.put("user", null);
        }
    } else {
        result.put("user", null);
    }

    // 4️⃣ Case details
    List<Detail> details = detailService.getDetailsByCaseId(caseId);
    result.put("details", details);

    return result;
}

// public List<CaseFile> getCasesByOfficerAndStatuses(int officerId, List<String> statuses) {
//     String inSql = String.join(",", statuses.stream().map(s -> "'" + s + "'").toList());
//     String query = "SELECT * FROM CaseFile WHERE officer_id = ? AND status IN (" + inSql + ")";
//     return jdbcTemplate.query(query, new Object[]{officerId}, caseFileRowMapper);
// }

public List<CaseFile> getCasesByOfficerAndStatuses(int officerId, List<String> statuses) {
        return caseFileDAO.findByOfficerIdAndStatuses(officerId, statuses);
    }


public List<CaseFile> getCasesBySupervisor(int supervisorId) {
    String sql = """
        SELECT cf.case_id, cf.case_name, cf.created_on, cf.image_paths,
               cf.current_status, cf.user_id, cf.officer_id, cf.domain_id
        FROM CaseFile cf
        WHERE cf.domain_id = (
            SELECT domain_id
            FROM Officer
            WHERE officer_id = :supervisorId
        )
    """;

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("supervisorId", supervisorId);

    return namedParameterJdbcTemplate.query(
        sql, params, new BeanPropertyRowMapper<>(CaseFile.class)
    );
}


public boolean assignOfficer(int caseId, int officerId) {
    int rows = caseFileDAO.assignOfficerToCase(caseId, officerId);
    return rows > 0;
}




//     public void updateCaseStatus(int caseId, String newStatus) {
//     caseFileDAO.updateCaseStatus(caseId, newStatus);
// }

// Added for the audit log feature

public void updateCaseStatus(int caseId, String newStatus) {
    // 1️⃣ Fetch previous status before updating
    Optional<CaseFile> caseOpt = caseFileDAO.findById(caseId);
    if (caseOpt.isEmpty()) return;

    String previousStatus = caseOpt.get().getCurrentStatus();

    // 2️⃣ Update case status in CaseFile table
    caseFileDAO.updateCaseStatus(caseId, newStatus);

    // 3️⃣ Insert a new row in AuditLog table
    AuditLog log = new AuditLog();
    log.setCaseId(caseId);
    log.setPreviousStatus(previousStatus);
    log.setCurrentStatus(newStatus);
    auditLogDAO.save(log);
}


public List<CaseFile> getCasesByDomainId(int domainId) {
    return caseFileDAO.findByDomainId(domainId);
}










}
