package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.model.Criminal;
import com.cybercrime.dbmsproject.model.CaseFile;
import com.cybercrime.dbmsproject.service.CaseFileService;
import com.cybercrime.dbmsproject.service.CriminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officer")
@CrossOrigin(origins = "http://localhost:8080")
public class CriminalController {

    @Autowired
    private CriminalService criminalService;

    @Autowired
    private CaseFileService caseFileService;

    /** ✅ 1. Get all cases assigned to this officer that are active/resolved */
    @GetMapping("/{officerId}/eligible-cases")
    public ResponseEntity<?> getEligibleCases(@PathVariable int officerId) {
        try {
            List<CaseFile> cases = caseFileService.getCasesByOfficerAndStatuses(officerId,
                    List.of("Assigned", "Under Review", "Resolved"));
            return ResponseEntity.ok(cases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching cases: " + e.getMessage());
        }
    }

    /** ✅ 2. Add a new criminal record */
    @PostMapping("/{officerId}/criminals")
    public ResponseEntity<?> addCriminal(@PathVariable int officerId, @RequestBody Criminal criminal) {
        try {
            criminal.setOfficerId(officerId); // ensure officer_id from URL, not frontend
            int id = criminalService.saveCriminal(criminal);
            return ResponseEntity.ok("Criminal added successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding criminal: " + e.getMessage());
        }
    }
}
