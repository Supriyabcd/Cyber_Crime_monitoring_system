// package com.cybercrime.dbmsproject.controller;

// import com.cybercrime.dbmsproject.dao.FeedbackDAO;
// import com.cybercrime.dbmsproject.model.Feedback;
// import com.cybercrime.dbmsproject.service.FeedbackService;
// import com.cybercrime.dbmsproject.model.UserDetail;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import jakarta.servlet.http.HttpSession;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Map;

// @Controller
// public class FeedbackController {

//     @Autowired
//     private FeedbackDAO feedbackDAO;

//     /** 🧾 Load feedback page */
//     @GetMapping("/feedback")
//     public String showFeedbackPage(HttpSession session, Model model) {
//         UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
//         if (user == null) {
//             return "redirect:/login";
//         }

//         model.addAttribute("user", user);
//         return "feedback";
//     }

//     /** 🔍 Get user’s cases dynamically (AJAX call) */
//     @GetMapping("/user/cases")
//     @ResponseBody
//     public List<Map<String, Object>> getUserCases(HttpSession session) {
//         UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
//         if (user == null) return List.of();
//         return feedbackDAO.getCasesForUser(user.getUserId());
//     }

//     /** 💾 Save feedback */
//     @PostMapping("/submitFeedback")
//     @ResponseBody
//     public String submitFeedback(
//             @RequestParam int caseId,
//             @RequestParam int officerId,
//             @RequestParam int responsiveness,
//             @RequestParam int confidentiality,
//             @RequestParam int outcomeSatisfaction,
//             @RequestParam int overallExperience,
//             HttpSession session
//     ) {
//         UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
//         if (user == null) {
//             return "User not logged in";
//         }

//         Feedback f = new Feedback();
//         f.setUserId(user.getUserId());
//         f.setOfficerId(officerId);
//         f.setResponsiveness(responsiveness);
//         f.setPrivacyRespected(confidentiality >= 3); // Example mapping (3+ means “yes”)
//         f.setOutcomeSatisfaction(outcomeSatisfaction);
//         f.setOverallExperience(overallExperience);
//         f.setFeedbackDate(LocalDate.now());

//         feedbackDAO.save(f);
//         return "Feedback submitted successfully!";
//     }

//     /** 🔍 Fetch feedback for an officer (used in officer dashboard JS) */
//     @GetMapping("/api/feedback/officer/{officerId}")
//     @ResponseBody
//     public List<Feedback> getFeedbackByOfficer(@PathVariable int officerId) {
//         return feedbackService.getFeedbacksByOfficer(officerId);
//     }

//     /** 🔍 Fetch all feedback (optional for admin view) */
//     @GetMapping("/api/feedback/all")
//     @ResponseBody
//     public List<Feedback> getAllFeedbacks() {
//         return feedbackService.getAllFeedbacks();
//     }



// }



package com.cybercrime.dbmsproject.controller;

import com.cybercrime.dbmsproject.dao.FeedbackDAO;
import com.cybercrime.dbmsproject.model.Feedback;
import com.cybercrime.dbmsproject.model.UserDetail;
import com.cybercrime.dbmsproject.service.FeedbackService;
import com.cybercrime.dbmsproject.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*") // allow frontend to fetch via REST if needed
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    /** 🧾 Load feedback page for user */
    @GetMapping("/feedback")
    public String showFeedbackPage(HttpSession session, Model model) {
        UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
        String sessionToken = (String) session.getAttribute("sessionToken");

        if (user == null || sessionToken == null) {
            return "redirect:/login";
        }

        // verify session token
        String dbToken = userService.getSessionToken(user.getUserId());
        if (dbToken == null || !dbToken.equals(sessionToken)) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "feedback";
    }

    /** 🔍 Get user’s case list dynamically (AJAX) */
    @GetMapping("/user/cases")
    @ResponseBody
    public List<Map<String, Object>> getUserCases(HttpSession session) {
        UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
        String sessionToken = (String) session.getAttribute("sessionToken");

        if (user == null || sessionToken == null ||
                !sessionToken.equals(userService.getSessionToken(user.getUserId()))) {
            return List.of(); // return empty if invalid session
        }

        return feedbackService.getCasesForUser(user.getUserId());
    }

    /** 💾 Submit feedback by user */
    @PostMapping("/submitFeedback")
    @ResponseBody
    public String submitFeedback(
            @RequestParam int caseId,
            @RequestParam int officerId,
            @RequestParam int responsiveness,
            @RequestParam int confidentiality,
            @RequestParam int outcomeSatisfaction,
            @RequestParam int overallExperience,
            HttpSession session
    ) {
        UserDetail user = (UserDetail) session.getAttribute("loggedInUser");
        String sessionToken = (String) session.getAttribute("sessionToken");

        if (user == null || sessionToken == null ||
                !sessionToken.equals(userService.getSessionToken(user.getUserId()))) {
            return "Session expired — please login again.";
        }

        Feedback feedback = new Feedback();
        feedback.setUserId(user.getUserId());
        feedback.setOfficerId(officerId);
        feedback.setResponsiveness(responsiveness);
        feedback.setPrivacyRespected(confidentiality >= 3);
        feedback.setOutcomeSatisfaction(outcomeSatisfaction);
        feedback.setOverallExperience(overallExperience);
        feedback.setFeedbackDate(LocalDate.now());

        feedbackService.addFeedback(feedback);
        return "Feedback submitted successfully!";
    }

    /** 🔍 Fetch feedback for an officer (used in officer dashboard JS) */
    @GetMapping("/api/feedback/officer/{officerId}")
    @ResponseBody
    public List<Feedback> getFeedbackByOfficer(@PathVariable int officerId) {
        return feedbackService.getFeedbacksByOfficer(officerId);
    }

    /** 🔍 Fetch all feedback (optional for admin view) */
    @GetMapping("/api/feedback/all")
    @ResponseBody
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }
}
