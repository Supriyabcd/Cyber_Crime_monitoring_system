// package com.cybercrime.dbmsproject.controller;

// import com.cybercrime.dbmsproject.dao.UserDetailDAO;
// import com.cybercrime.dbmsproject.model.UserDetail;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;



// @Controller   // ab ye Thymeleaf views return karega
// public class HomeController {
//     private final UserDetailDAO userDetailDAO;

//     public HomeController(UserDetailDAO userDetailDAO) {
//         this.userDetailDAO = userDetailDAO;
//     }

//     // Show all users
//     @GetMapping("/")
//     public String index(Model model) {
//         List<UserDetail> users = userDetailDAO.findAll();
//         model.addAttribute("users", users);
//         return "index"; // index.html
//     }

//     // Show insert form
//     @GetMapping("/add-user")
//     public String showAddUserForm(Model model) {
//         model.addAttribute("user", new UserDetail());
//         return "add-user"; // add-user.html
//     }

//     // Handle form submit
//     @PostMapping("/add-user")
//     public String addUser(@ModelAttribute("user") UserDetail user) {
//         userDetailDAO.save(user);
//         return "redirect:/";  // insert hone ke baad list page pe wapas bhej do
//     }
// }



package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.dao.UserDetailDAO;
import com.cybercrime.dbmsproject.dao.OfficerDAO;
import com.cybercrime.dbmsproject.dao.CaseFileDAO;
import com.cybercrime.dbmsproject.model.UserDetail;
import com.cybercrime.dbmsproject.model.Officer;
import com.cybercrime.dbmsproject.model.CaseFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



@Controller
public class HomeController {

    private final com.cybercrime.dbmsproject.dao.UserDetailDAO userDetailDao;
    private final com.cybercrime.dbmsproject.dao.OfficerDAO officerDao;
    private final com.cybercrime.dbmsproject.dao.CaseFileDAO caseFileDao;

    public HomeController(com.cybercrime.dbmsproject.dao.UserDetailDAO userDetailDao,
                         com.cybercrime.dbmsproject.dao.OfficerDAO officerDao,
                         com.cybercrime.dbmsproject.dao.CaseFileDAO caseFileDao) {
        this.userDetailDao = userDetailDao;
        this.officerDao = officerDao;
        this.caseFileDao = caseFileDao;
    }

    @GetMapping({"/", "/home"})
    public String homePage(Model model) {
        List<com.cybercrime.dbmsproject.model.UserDetail> users = userDetailDao.findAll();
        List<com.cybercrime.dbmsproject.model.Officer> officers = officerDao.findAll();
        List<com.cybercrime.dbmsproject.model.CaseFile> cases = caseFileDao.findAll();

        model.addAttribute("users", users);
        model.addAttribute("officers", officers);
        model.addAttribute("cases", cases);

        return "home";
    }

    @GetMapping("/register1")
    public String loginPage() {
        return "register1"; // templates/register1.html
    }

    @GetMapping("/trackcase")
    public String trackCasePage() {
        return "trackcase"; // templates/trackcase.html
    }
    // @GetMapping("/userdashboard")
    // public String userDashboardPage() {
    //     return "userdashboard"; // templates/trackcase.html
    // }

    @GetMapping("/resources")
    public String resourcesPage() {
        return "resources"; // templates/resources.html
    }

        



}


