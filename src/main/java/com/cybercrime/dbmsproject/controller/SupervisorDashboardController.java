

package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.model.Officer;
import com.cybercrime.dbmsproject.model.Domain;
import com.cybercrime.dbmsproject.model.CaseFile;
import com.cybercrime.dbmsproject.model.UserDetail;
import com.cybercrime.dbmsproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

// or whatever your frontend port is

@RestController
@RequestMapping("/supervisor")
@CrossOrigin(origins = "http://localhost:8080")
public class SupervisorDashboardController {

    @Autowired
    private OfficerService officerService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private CaseFileService caseFileService;

    @Autowired
    private UserService userService;  // ✅ Added

    /** ✅ Fetch Supervisor Profile Info */
    @GetMapping("/{supervisorId}/profile")
    public ResponseEntity<?> getSupervisorProfile(@PathVariable int supervisorId) {
        Officer supervisor = officerService.getOfficerById(supervisorId);
        if (supervisor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supervisor not found");
        }

        Domain domain = null;
        if (supervisor.getDomainId() != null) {
            domain = domainService.getDomainById(supervisor.getDomainId());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("officerId", supervisor.getOfficerId());
        response.put("fullName", supervisor.getFname() + " " + supervisor.getLname());
        response.put("joinDate", supervisor.getJoinDate());
        response.put("mobNo", supervisor.getMobNo());
        response.put("domain", domain != null ? domain.getDomainName() : "N/A");
        response.put("domainId", supervisor.getDomainId());
        return ResponseEntity.ok(response);
    }

    /** ✅ Fetch all cases visible to this supervisor — with usernames & officer names */
    // @GetMapping("/{supervisorId}/cases")
    // public ResponseEntity<List<Map<String, Object>>> getSupervisorCases(@PathVariable int supervisorId) {
    //     List<CaseFile> cases = supervisorService.getCasesForSupervisor(supervisorId);
    //     List<Map<String, Object>> responseList = new ArrayList<>();

    //     for (CaseFile c : cases) {
    //         Map<String, Object> map = new HashMap<>();
    //         map.put("caseId", c.getCaseId());
    //         map.put("caseName", c.getCaseName());
    //         map.put("currentStatus", c.getCurrentStatus());

    //         // ✅ Add user info
    //         if (c.getUserId() != null) {
    //             UserDetail user = userService.getUserById(c.getUserId());
    //             if (user != null) {
    //                 map.put("reportedBy", user.getUsername());
    //             } else {
    //                 map.put("reportedBy", "Unknown");
    //             }
    //         } else {
    //             map.put("reportedBy", "Unknown");
    //         }

    //         // ✅ Add officer info
    //         if (c.getOfficerId() != null) {
    //             Officer officer = officerService.getOfficerById(c.getOfficerId());
    //             if (officer != null) {
    //                 map.put("assignedOfficer", officer.getFname() + " " + officer.getLname());
    //             } else {
    //                 map.put("assignedOfficer", "Unassigned");
    //             }
    //         } else {
    //             map.put("assignedOfficer", "Unassigned");
    //         }

    //         responseList.add(map);
    //     }

    //     return ResponseEntity.ok(responseList);
    // }

    // GET /supervisor/{supervisorId}/cases
    // @GetMapping("/{supervisorId}/cases")
    // public List<CaseFile> getCasesUnderSupervisor(@PathVariable int supervisorId) {
    //     return caseFileService.getCasesBySupervisor(supervisorId);
    // }

    @GetMapping("/{supervisorId}/cases")
    public ResponseEntity<List<Map<String, Object>>> getSupervisorCases(@PathVariable int supervisorId) {
        List<CaseFile> cases = supervisorService.getCasesForSupervisor(supervisorId);
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (CaseFile c : cases) {
            Map<String, Object> map = new HashMap<>();
            map.put("caseId", c.getCaseId());
            map.put("caseName", c.getCaseName());

            // ✅ Determine current status logically
            String status;
            if (c.getCurrentStatus() != null && !c.getCurrentStatus().isEmpty()) {
                status = c.getCurrentStatus();
            } else if (c.getOfficerId() == null) {
                status = "Unassigned";
            } else {
                status = "Assigned";
            }

            map.put("currentStatus", status);

            // ✅ User info
            if (c.getUserId() != null) {
                UserDetail user = userService.getUserById(c.getUserId());
                map.put("reportedBy", (user != null) ? user.getUsername() : "Unknown");
            } else {
                map.put("reportedBy", "Unknown");
            }

            // ✅ Officer info
            if (c.getOfficerId() != null) {
                Officer officer = officerService.getOfficerById(c.getOfficerId());
                map.put("assignedOfficer", officer != null ? officer.getFname() + " " + officer.getLname() : "Unassigned");
            } else {
                map.put("assignedOfficer", "Unassigned");
            }

            // ✅ Control for frontend (whether Assign button should show)
            map.put("canAssign", status.equalsIgnoreCase("Unassigned"));

            responseList.add(map);
        }

        return ResponseEntity.ok(responseList);
    }

    /** ✅ Fetch single case details (like Officer) */
    @GetMapping("/cases/{caseId}/details")
    public ResponseEntity<?> getCaseDetails(@PathVariable int caseId) {
        Map<String, Object> caseData = caseFileService.getFullCaseDetails(caseId);
        if (caseData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Case not found");
        }
        return ResponseEntity.ok(caseData);
    }

    /** ✅ Fetch all officers */
    @GetMapping("/officers/all")
    public ResponseEntity<List<Officer>> getAllOfficers() {
        List<Officer> officers = officerService.getAllOfficers();
        return ResponseEntity.ok(officers);
    }

    // 1️⃣ Fetch officers by domain
    @GetMapping("/officers/{domainId}")
    public ResponseEntity<List<Officer>> getOfficersByDomain(@PathVariable int domainId) {
        List<Officer> officers = officerService.getOfficersByDomainId(domainId);
        return ResponseEntity.ok(officers);
    }

    // 2️⃣ Assign officer to case
    @PostMapping("/assign")
    public ResponseEntity<String> assignOfficer(@RequestBody Map<String, Integer> request) {
        int caseId = request.get("caseId");
        int officerId = request.get("officerId");

        boolean success = caseFileService.assignOfficer(caseId, officerId);
        if (success) {
            return ResponseEntity.ok("Officer assigned successfully!");
        } else {
            return ResponseEntity.status(500).body("Failed to assign officer");
        }
    }



    /** ✅ Supervisor stats endpoint (merged from your friend’s logic) */
    @GetMapping("/{supervisorId}/stats")
public ResponseEntity<?> getSupervisorStats(@PathVariable int supervisorId) {
    Officer supervisor = officerService.getOfficerById(supervisorId);
    if (supervisor == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supervisor not found");
    }

    Integer domainId = supervisor.getDomainId();
    if (domainId == null) {
        return ResponseEntity.badRequest().body("Supervisor does not have a domain assigned");
    }

    // ✅ Fetch all cases under this supervisor’s domain
    List<CaseFile> cases = caseFileService.getCasesByDomainId(domainId);
    int totalCases = cases.size();

    long unassignedCases = cases.stream()
            .filter(c -> c.getOfficerId() == null)
            .count();

    // ✅ Closure rate based on your logic (Total - Unassigned) / Total
    double closureRate = totalCases > 0
            ? ((double) (totalCases - unassignedCases) / totalCases) * 100
            : 0.0;

    // ✅ Count active officers
    List<Officer> officers = officerService.getOfficersByDomainId(domainId);
    long activeOfficers = officers.stream()
            .filter(Officer::isActiveStatus)
            .count();

    // ✅ Prepare response
    Map<String, Object> response = new HashMap<>();
    response.put("totalCases", totalCases);
    response.put("unassignedCases", unassignedCases);
    response.put("activeOfficers", activeOfficers);
    response.put("closureRate", String.format("%.1f", closureRate));

    return ResponseEntity.ok(response);
}



}