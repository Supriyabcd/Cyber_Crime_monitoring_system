package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.model.CaseFile;
import com.cybercrime.dbmsproject.model.Criminal;
import com.cybercrime.dbmsproject.model.Domain;
import com.cybercrime.dbmsproject.model.Officer;
import com.cybercrime.dbmsproject.service.CaseFileService;
import com.cybercrime.dbmsproject.service.DomainService;
import com.cybercrime.dbmsproject.service.OfficerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.cybercrime.dbmsproject.dao.CriminalDAO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/officer")
@CrossOrigin(origins = "http://localhost:8080")
public class OfficerDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(OfficerDashboardController.class);

    @Autowired
    private CaseFileService caseFileService;

    @Autowired
    private OfficerService officerService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private CriminalDAO criminalDAO;

    /** ✅ Get all cases assigned to a specific officer */
    @GetMapping("/{officerId}/cases")
    public ResponseEntity<?> getCasesByOfficer(@PathVariable int officerId) {
        Officer officer = officerService.getOfficerById(officerId);
        if (officer == null) {
            return ResponseEntity.status(404).body("Officer not found");
        }

        List<CaseFile> cases = caseFileService.getCasesByOfficerId(officerId);
        return ResponseEntity.ok(cases);
    }

    /** ✅ Get officer profile by ID, including domain name */
    @GetMapping("/{officerId}/profile")
    public ResponseEntity<?> getOfficerProfile(@PathVariable int officerId) {
        Officer officer = officerService.getOfficerById(officerId);
        if (officer == null) {
            return ResponseEntity.status(404).body("Officer not found");
        }

        Domain domain = null;
        if (officer.getDomainId() != null) {
            domain = domainService.getDomainById(officer.getDomainId());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("officer", officer);
        response.put("domain", domain != null ? domain.getDomainName() : null);

        return ResponseEntity.ok(response);
    }

@GetMapping("/{officerId}/stats")
public ResponseEntity<?> getOfficerStats(@PathVariable int officerId) {
    try {
        Officer officer = officerService.getOfficerById(officerId);
        if (officer == null) {
            return ResponseEntity.status(404).body("Officer not found");
        }

        // ✅ Corrected status names according to your DB
        int assignedCases = caseFileService.countCasesByOfficerAndStatus(officerId, "Assigned");
        int activeCases = caseFileService.countCasesByOfficerAndStatus(officerId, "Under Review");

        // ✅ For pending: all cases that are NOT resolved
        int pendingEvidence = caseFileService.countCasesByOfficerAndNotStatus(officerId, "Resolved");

        int closedCases = caseFileService.countCasesByOfficerAndStatus(officerId, "Resolved");

        Map<String, Integer> stats = new HashMap<>();
        stats.put("assignedCases", assignedCases);
        stats.put("activeCases", activeCases);
        stats.put("pendingEvidence", pendingEvidence);
        stats.put("closedCases", closedCases);

        return ResponseEntity.ok(stats);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error fetching stats: " + e.getMessage());
    }
}



    /** ✅ Get recent cases (activity feed) for an officer */
    @GetMapping("/{officerId}/recent")
    public ResponseEntity<?> getRecentCasesByOfficer(@PathVariable int officerId) {
        try {
            List<CaseFile> recentCases = caseFileService.getRecentCasesByOfficer(officerId, 5);
            return ResponseEntity.ok(recentCases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching recent cases: " + e.getMessage());
        }
    }





    /** ✅ Get full details of a specific case */
    @GetMapping("/cases/{caseId}/details")
    public ResponseEntity<?> getCaseDetails(@PathVariable int caseId) {
        try {
            Map<String, Object> details = caseFileService.getFullCaseDetails(caseId);

            if (details == null || !details.containsKey("case") || details.get("case") == null) {
                return ResponseEntity.status(404).body("Case not found");
            }

            return ResponseEntity.ok(details);

        } catch (Exception e) {
            logger.error("Error fetching case details for caseId {}: {}", caseId, e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }



    /** ✅ Update the status of a case (Assigned, Unassigned, Resolved, Under Review) */
@PutMapping("/cases/{caseId}/status")
public ResponseEntity<?> updateCaseStatus(
        @PathVariable int caseId,
        @RequestParam("status") String newStatus) {
    try {
        caseFileService.updateCaseStatus(caseId, newStatus);
        return ResponseEntity.ok("Case status updated successfully");
    } catch (Exception e) {
        logger.error("Error updating case status for caseId {}: {}", caseId, e.getMessage(), e);
        return ResponseEntity.status(500).body("Error updating case status: " + e.getMessage());
    }
}


@GetMapping("/cases/{caseId}/criminals")
public ResponseEntity<?> getCriminalsByCase(@PathVariable int caseId) {
    List<Criminal> criminals = criminalDAO.findByCaseId(caseId);

    if (criminals.isEmpty()) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    return ResponseEntity.ok(criminals);
}



        

    
    
}