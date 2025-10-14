package com.cybercrime.dbmsproject.model;

import java.time.LocalDate;

public class Feedback {

    private Integer feedbackId;
    private Integer userId;
    private Integer officerId;
    private Integer outcomeSatisfaction;
    private Integer overallExperience;
    private Integer responsiveness;
    private Boolean privacyRespected;
    private LocalDate feedbackDate;

    public Feedback() {}

    public Feedback(Integer feedbackId, Integer userId, Integer officerId,
                    Integer outcomeSatisfaction, Integer overallExperience,
                    Integer responsiveness, Boolean privacyRespected, LocalDate feedbackDate) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.officerId = officerId;
        this.outcomeSatisfaction = outcomeSatisfaction;
        this.overallExperience = overallExperience;
        this.responsiveness = responsiveness;
        this.privacyRespected = privacyRespected;
        this.feedbackDate = feedbackDate;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Integer officerId) {
        this.officerId = officerId;
    }

    public Integer getOutcomeSatisfaction() {
        return outcomeSatisfaction;
    }

    public void setOutcomeSatisfaction(Integer outcomeSatisfaction) {
        this.outcomeSatisfaction = outcomeSatisfaction;
    }

    public Integer getOverallExperience() {
        return overallExperience;
    }

    public void setOverallExperience(Integer overallExperience) {
        this.overallExperience = overallExperience;
    }

    public Integer getResponsiveness() {
        return responsiveness;
    }

    public void setResponsiveness(Integer responsiveness) {
        this.responsiveness = responsiveness;
    }

    public Boolean getPrivacyRespected() {
        return privacyRespected;
    }

    public void setPrivacyRespected(Boolean privacyRespected) {
        this.privacyRespected = privacyRespected;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}
