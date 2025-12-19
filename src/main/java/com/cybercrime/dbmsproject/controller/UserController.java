package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.model.CaseFile;
import com.cybercrime.dbmsproject.model.UserDetail;
import com.cybercrime.dbmsproject.dao.UserDetailDAO;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;



// @Controller
// @RequestMapping("/user")
// public class UserController {

//     private final UserService userService;

//     public UserController(UserService userService) {
//         this.userService = userService;
//     }

//     // Show add user form (Thymeleaf page)
//     @GetMapping("/add")
//     public String showAddForm() {
//         return "adduser"; // corresponds to addUser.html
//     }

//     // Handle form submission
//     @PostMapping("/add")
//     public String adduser(
//             @RequestParam String fname,
//             @RequestParam String lname,
//             @RequestParam String mobNo,
//             @RequestParam String dob,
//             @RequestParam String hNo,
//             @RequestParam String streetNo,
//             @RequestParam String city,
//             @RequestParam String state,
//             @RequestParam String username,
//             @RequestParam String password,
//             @RequestParam List<String> middleNames, // multiple values from form
//             Model model) {

//         UserDetail user = new UserDetail();
//         user.setFname(fname);
//         user.setLname(lname);
//         user.setMobNo(mobNo);
//         user.setDob(dob);
//         user.setHNo(hNo);
//         user.setStreetNo(streetNo);
//         user.setCity(city);
//         user.setState(state);
//         user.setUsername(username);
//         user.setPassword(password);

//         int userId = userService.addUserWithMNames(user, middleNames);
//         model.addAttribute("message", "User added with ID: " + userId);
//         return "adduser";
//     }

//     @GetMapping("/adduser")
//         public String addUserPage(Model model) {
//             model.addAttribute("userDetail", new UserDetail()); 
//             return "adduser";
//         }

// }




@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Show add user form at /adduser
    @GetMapping("/adduser")
    public String showAddForm(Model model) {
        model.addAttribute("userDetail", new UserDetail());
        return "adduser"; // renders adduser.html
    }

    // Handle form submission at /adduser
    @PostMapping("/adduser")
public String addUser(@ModelAttribute UserDetail userDetail,
                      @RequestParam(required = false) List<String> middleNames,
                      RedirectAttributes redirectAttributes) {

    try {
        int userId = userService.addUserWithMNames(userDetail, middleNames);
        // Add flash attribute for success message
        redirectAttributes.addFlashAttribute("successMessage", "User registered successfully!");
        // Redirect to home page ("/")
        return "redirect:/";
    } catch (DuplicateKeyException e) {
        // Username already exists
        redirectAttributes.addFlashAttribute("errorMessage", "Username already exists!");
        return "redirect:/adduser"; // Redirect back to form
    }
}

 @Autowired
    private UserDetailDAO userDetailDAO;

    @GetMapping("/showusers")
    public String showUsers(Model model) {
        List<UserDetail> users = userDetailDAO.findAll();
        model.addAttribute("users", users);
        return "showusers";
    }

    @GetMapping("/declaration")
    public String declarationPage() {
        return "declaration"; 
    }

}
