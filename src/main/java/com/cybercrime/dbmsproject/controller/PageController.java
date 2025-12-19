package com.cybercrime.dbmsproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/auditlog")
    public String showAuditLogPage() {
        // This returns auditlog.html from src/main/resources/templates/
        return "auditlog";
    }
}
