package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.dao.OfficerDAO;
import com.cybercrime.dbmsproject.model.Officer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OfficerController {
    private final OfficerDAO officerDAO;

    public OfficerController(OfficerDAO officerDAO) {
        this.officerDAO = officerDAO;
    }

    @GetMapping("/officers")
    public String listOfficers(Model model) {
        List<Officer> officers = officerDAO.findAll();
        model.addAttribute("officers", officers);
        return "officers"; // officers.html
    }

    @GetMapping("/add-officer")
    public String showAddOfficerForm(Model model) {
        model.addAttribute("officer", new Officer());
        return "add-officer"; // add-officer.html
    }

    @PostMapping("/add-officer")
    public String addOfficer(@ModelAttribute("officer") Officer officer) {
        officerDAO.save(officer);
        return "redirect:/officers";
    }
}
