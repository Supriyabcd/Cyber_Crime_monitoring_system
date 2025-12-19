package com.cybercrime.dbmsproject.model;

import java.time.LocalDate;

public class CaseFile {
    private Integer caseId;
    private String caseName;
    private LocalDate createdOn;
    private String currentStatus;
    private Integer userId;
    private Integer officerId;
    private Integer domainId;
    private String officerName;  // transient field, not in DB


    // 🔹 new fields
    private String detailedDescription;
    private String imagePaths;

    // Getters and setters
    public Integer getCaseId() { return caseId; }
    public void setCaseId(Integer caseId) { this.caseId = caseId; }

    public String getCaseName() { return caseName; }
    public void setCaseName(String caseName) { this.caseName = caseName; }

    public LocalDate getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDate createdOn) { this.createdOn = createdOn; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getOfficerId() { return officerId; }
    public void setOfficerId(Integer officerId) { this.officerId = officerId; }

    public Integer getDomainId() { return domainId; }
    public void setDomainId(Integer domainId) { this.domainId = domainId; }

    public String getDetailedDescription() { return detailedDescription; }
    public void setDetailedDescription(String detailedDescription) { this.detailedDescription = detailedDescription; }

    public String getImagePaths() { return imagePaths; }
    public void setImagePaths(String imagePaths) { this.imagePaths = imagePaths; }

    public String getOfficerName() {
    return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

}
