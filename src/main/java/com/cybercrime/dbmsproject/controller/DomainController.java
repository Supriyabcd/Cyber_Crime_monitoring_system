package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.dao.DomainDAO;
import com.cybercrime.dbmsproject.model.Domain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DomainController {
    private final DomainDAO domainDAO;

    public DomainController(DomainDAO domainDAO) {
        this.domainDAO = domainDAO;
    }

    @GetMapping("/domains")
    public String listDomains(Model model) {
        List<Domain> domains = domainDAO.findAll();
        model.addAttribute("domains", domains);
        return "domains"; // domains.html
    }

    @GetMapping("/add-domain")
    public String showAddDomainForm(Model model) {
        model.addAttribute("domain", new Domain());
        return "add-domain"; // add-domain.html
    }

    @PostMapping("/add-domain")
    public String addDomain(@ModelAttribute("domain") Domain domain) {
        domainDAO.save(domain);
        return "redirect:/domains";
    }
}
