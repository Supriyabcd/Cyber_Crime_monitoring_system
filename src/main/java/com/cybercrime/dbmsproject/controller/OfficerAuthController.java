package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.model.Officer;
import com.cybercrime.dbmsproject.security.JwtUtil;
import com.cybercrime.dbmsproject.service.OfficerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/officer")
@CrossOrigin(origins = "http://localhost:8080") // allow frontend access
public class OfficerAuthController {

    @Autowired
    private OfficerService officerService;

    @Autowired
    private JwtUtil jwtUtil;

    // 1️⃣ Serve officer login page
    @GetMapping("/officerlogin")
    public String showLoginPage() {
        return "officerlogin"; // officerlogin.html in templates folder
    }

    @Controller
@RequestMapping("/officer")
public class OfficerDashboardController {

    @GetMapping("/officerdashboard")
    public String showOfficerDashboard() {
        return "officerdashboard"; // templates/officerdashboard.html
    }

    @GetMapping("/supervisordashboard")
    public String showSupervisorDashboard() {
        return "supervisordashboard"; // templates/officerdashboard.html
    }
}


    // 2️⃣ Handle login POST for JWT
//     @PostMapping("/api/login")
// @ResponseBody
// public ResponseEntity<?> officerLogin(@RequestBody Map<String, String> loginRequest) {
//     try {
//         int officerId = Integer.parseInt(loginRequest.get("officer_id"));
//         String password = loginRequest.get("password");

//         Officer officer = officerService.getOfficerById(officerId);

//         if (officer == null || !officer.getPassword().equals(password)) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//         }

//         // ✅ Determine role by checking Supervisor table
//         boolean supervisor = officerService.isSupervisor(officerId);
//         String role = supervisor ? "supervisor" : "officer";

//         String token = jwtUtil.generateToken(String.valueOf(officerId), role);

//         return ResponseEntity.ok(Map.of(
//             "token", token,
//             "role", role,
//             "officer_id", officerId,
//             "name", officer.getFname() + " " + officer.getLname()
//         ));

//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request format");
//     }
// }


// 2️⃣ Handle login POST for JWT
    @PostMapping("/api/login")
@ResponseBody
public ResponseEntity<?> officerLogin(@RequestBody Map<String, String> loginRequest) {
    try {
        int officerId = Integer.parseInt(loginRequest.get("officer_id"));
        String password = loginRequest.get("password");

        Officer officer = officerService.getOfficerById(officerId);

        if (officer == null || !officer.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // ✅ Determine role by checking Supervisor table
        boolean supervisor = officerService.isSupervisor(officerId);
        String role = supervisor ? "supervisor" : "officer";

        String token = jwtUtil.generateToken(String.valueOf(officerId), role);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "role", role,
            "officer_id", officerId,
            "name", officer.getFname() + " " + officer.getLname()
        ));

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request format");
    }
}





}