package com.cybercrime.dbmsproject.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


import com.cybercrime.dbmsproject.model.CaseFile;
import com.cybercrime.dbmsproject.model.Detail;
import com.cybercrime.dbmsproject.model.Domain;
import com.cybercrime.dbmsproject.model.SuspectDetail;
import com.cybercrime.dbmsproject.model.UserDetail;
import com.cybercrime.dbmsproject.dao.UserDetailDAO;
import com.cybercrime.dbmsproject.dao.CaseFileDAO;
import com.cybercrime.dbmsproject.dao.DomainDAO;
import com.cybercrime.dbmsproject.dao.DetailDAO;
import com.cybercrime.dbmsproject.dao.SuspectDetailDAO;
import com.cybercrime.dbmsproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;



@Controller
public class AuthController {

    private final UserService userService;
    private final DomainDAO domainDAO;
    private final CaseFileDAO caseFileDAO;
    private final SuspectDetailDAO suspectDetailDAO;
    private final DetailDAO detailDAO;

    public AuthController(
            UserService userService,
            DomainDAO domainDAO,
            CaseFileDAO caseFileDAO,
            SuspectDetailDAO suspectDetailDAO,
            DetailDAO detailDAO) {
        this.userService = userService;
        this.domainDAO = domainDAO;
        this.caseFileDAO = caseFileDAO;
        this.suspectDetailDAO = suspectDetailDAO;
        this.detailDAO = detailDAO;
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "login";  // login.html (we’ll create this)
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String mobNo,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {

        UserDetail user = userService.login(username, mobNo, password);

        if (user != null) {
            // Save user in session
            session.setAttribute("loggedInUser", user);

            redirectAttributes.addFlashAttribute("successMessage", "Welcome " + user.getFname() + "!");
            return "redirect:/userdashboard";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid credentials!");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/filecomplaint")
public String showFileComplaintForm(HttpSession session) {
    UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/login";
    }
    return "filecomplaint"; // corresponds to filecomplaint.html in templates folder
}


//     @PostMapping("/filecomplaint")
// public String fileComplaint(@RequestParam String caseTitle,
//                             @RequestParam String incidentType,
//                             @RequestParam String description,
//                             @RequestParam(value = "evidence", required = false) MultipartFile[] evidenceFiles,
//                             HttpSession session) throws Exception {

//     UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
//     if (user == null) {
//         return "redirect:/login";
//     }

//     // 1. Get domain_id from DomainDAO
//     Integer domainId = domainDAO.findDomainIdByName(incidentType);

//     // 2. Save uploaded files
//     List<String> filePaths = new ArrayList<>();
//     if (evidenceFiles != null) {
//         for (MultipartFile file : evidenceFiles) {
//             if (!file.isEmpty()) {
//                 String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//                 Path path = Paths.get("src/main/resources/static/uploads/" + fileName);
//                 Files.write(path, file.getBytes());
//                 filePaths.add("/uploads/" + fileName);
//             }
//         }
//     }

//     // 3. Build CaseFile model
//     CaseFile caseFile = new CaseFile();
//     caseFile.setCaseName(caseTitle);
//     caseFile.setDetailedDescription(description);
//     caseFile.setImagePaths(new ObjectMapper().writeValueAsString(filePaths)); // JSON
//     caseFile.setCreatedOn(LocalDate.now());
//     caseFile.setCurrentStatus("Not Assigned");
//     caseFile.setUserId(user.getUserId());
//     caseFile.setOfficerId(null); // initially not assigned
//     caseFile.setDomainId(domainId);

//     // 4. Save via DAO
//     caseFileDAO.save(caseFile);

//     return "redirect:/userdashboard";
// }

@PostMapping("/filecomplaint")
public String fileComplaintSubmit(
        @RequestParam("caseTitle") String caseTitle,
        @RequestParam("description") String description,
        @RequestParam("platformName") String platformName,
        @RequestParam("personalDetails") String personalDetails,
        // @RequestParam("suspectDetails") String suspectDetails,
        @RequestParam String suspectDetails,
        @RequestParam("incidentType") String incidentType,
        @RequestParam(value = "evidence", required = false) MultipartFile evidence,
        HttpSession session,
        Model model
) {
    try {
        UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "Please log in first.");
            return "login";
        }
        Integer userId = user.getUserId();

        Optional<Integer> domainIdOpt = domainDAO.findIdByName(incidentType);
        Integer domainId = domainIdOpt.orElse(null);
        if (domainId == null) {
            model.addAttribute("error", "Invalid domain selected.");
            return "fileComplaint";
        }


        String uploadPath = null;
        if (evidence != null && !evidence.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            uploadPath = uploadDir + System.currentTimeMillis() + "_" + evidence.getOriginalFilename();
            evidence.transferTo(new File(uploadPath));
        }

        CaseFile caseFile = new CaseFile();
        caseFile.setCaseName(caseTitle);
        caseFile.setDetailedDescription(description);
        caseFile.setImagePaths(uploadPath);
        caseFile.setUserId(userId);
        caseFile.setDomainId(domainId);
        caseFile.setCreatedOn(LocalDate.now());
        caseFile.setCurrentStatus("Not Assigned");

        int caseId = caseFileDAO.save(caseFile);

        Detail detail = new Detail();
        detail.setPlatformName(platformName);
        detail.setPersonalDetails(personalDetails);
        detail.setSuspectDetails(suspectDetails);
        detail.setUserId(userId);
        detail.setCaseId(caseId);
        int detailId = detailDAO.save(detail);

        // if (suspectDetails != null && !suspectDetails.trim().isEmpty()) {
        //     SuspectDetail suspect = new SuspectDetail();
        //     suspect.setDetailId(detailId);
        //     suspect.setSuspectDetails(suspectDetails);
        //     suspectDetailDAO.save(suspect);
        // }

           String suspectDetailsStr = detail.getSuspectDetails(); // e.g. "John, David, Unknown person"
    if (suspectDetailsStr != null && !suspectDetailsStr.trim().isEmpty()) {
        String[] suspectArray = suspectDetailsStr.split(",");

        for (String suspect : suspectArray) {
            SuspectDetail s = new SuspectDetail();
            s.setDetailId(detailId);
            s.setSuspectDetails(suspect.trim());
            suspectDetailDAO.save(s);
        }
    }


        model.addAttribute("message", "Complaint filed successfully!");
        return "redirect:/userdashboard";

    } catch (Exception e) {
        model.addAttribute("error", "Something went wrong: " + e.getMessage());
        e.printStackTrace();
        return "fileComplaint";
    }
}

@GetMapping("/userdashboard")
public String userDashboard(HttpSession session, Model model) {
    UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/login";
    }

    Integer userId = user.getUserId();

    // Stats
    int totalCases = caseFileDAO.countByUserId(userId);
    int pendingCases = caseFileDAO.countByUserIdAndStatus(userId, "Not Assigned");
    int resolvedCases = caseFileDAO.countByUserIdAndStatus(userId, "Resolved");

    // Recent activity
    List<CaseFile> recentCases = caseFileDAO.findRecentByUserId(userId, 5);

    // Add all attributes to model
    model.addAttribute("user", user);
    model.addAttribute("totalCases", totalCases);
    model.addAttribute("pendingCases", pendingCases);
    model.addAttribute("resolvedCases", resolvedCases);
    model.addAttribute("recentCases", recentCases);

    return "userdashboard";
}


    



}