package com.cybercrime.dbmsproject.model;

import java.time.LocalDateTime;

public class AuditLog {

    private Integer logId;
    private LocalDateTime timestamp;
    private String previousStatus;
    private String currentStatus;
    private Integer caseId;

    public AuditLog() {}

    public AuditLog(Integer logId, LocalDateTime timestamp, String previousStatus, String currentStatus, Integer caseId) {
        this.logId = logId;
        this.timestamp = timestamp;
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
        this.caseId = caseId;
    }

    // Getters and setters
    public Integer getLogId() { return logId; }
    public void setLogId(Integer logId) { this.logId = logId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(String previousStatus) { this.previousStatus = previousStatus; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

    public Integer getCaseId() { return caseId; }
    public void setCaseId(Integer caseId) { this.caseId = caseId; }
}
