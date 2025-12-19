package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.dao.AuditLogDAO;
import com.cybercrime.dbmsproject.model.AuditLog;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/audit")
public class AuditLogController {

    private final AuditLogDAO auditLogDAO;

    public AuditLogController(AuditLogDAO auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }

    /** 🔹 Get all logs */
    @GetMapping("/all")
    public List<AuditLog> getAllLogs() {
        return auditLogDAO.findAll();
    }

    /** 🔹 Add a new log */
    @PostMapping("/add")
    public String addLog(@RequestBody AuditLog log) {
        auditLogDAO.save(log);
        return "Audit log added successfully.";
    }

    /** 🔹 Get logs by case ID */
    @GetMapping("/case/{caseId}")
    public List<AuditLog> getLogsByCase(@PathVariable int caseId) {
        return auditLogDAO.findByCaseId(caseId);
    }
}
