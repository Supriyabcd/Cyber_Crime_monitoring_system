package com.cybercrime.dbmsproject.model;

public class Detail {

    private Integer detailId;
    private String platformName;
    private String personalDetails;
    private String suspectDetails;
    private Integer userId;  // FK to UserDetail
    private Integer caseId;  // FK to CaseFile

    public Detail() {}

    public Detail(Integer detailId, String platformName, String personalDetails, String suspectDetails, Integer userId, Integer caseId) {
        this.detailId = detailId;
        this.platformName = platformName;
        this.personalDetails = personalDetails;
        this.suspectDetails = suspectDetails;
        this.userId = userId;
        this.caseId = caseId;
    }

    // Getters and Setters
    public Integer getDetailId() { return detailId; }
    public void setDetailId(Integer detailId) { this.detailId = detailId; }

    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public String getPersonalDetails() { return personalDetails; }
    public void setPersonalDetails(String personalDetails) { this.personalDetails = personalDetails; }

    public String getSuspectDetails() { return suspectDetails; }
    public void setSuspectDetails(String suspectDetails) { this.suspectDetails = suspectDetails; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getCaseId() { return caseId; }
    public void setCaseId(Integer caseId) { this.caseId = caseId; }
}
