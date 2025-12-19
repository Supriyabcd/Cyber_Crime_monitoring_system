

package com.cybercrime.dbmsproject.service;

import com.cybercrime.dbmsproject.dao.FeedbackDAO;
import com.cybercrime.dbmsproject.model.Feedback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {

    private final FeedbackDAO feedbackDAO;

    public FeedbackService(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    public int addFeedback(Feedback feedback) {
        return feedbackDAO.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackDAO.findAll();
    }

    public List<Feedback> getFeedbacksByOfficer(int officerId) {
        return feedbackDAO.findByOfficerId(officerId);
    }

    public List<Feedback> getFeedbacksByUser(int userId) {
        return feedbackDAO.findByUserId(userId);
    }
    public List<Map<String, Object>> getCasesForUser(int userId) {
    return feedbackDAO.getCasesForUser(userId);
}

public List<Map<String, Object>> getFeedbacksByOfficerWithUserName(int officerId) {
    return feedbackDAO.findByOfficerIdWithUserName(officerId);
}



} 
