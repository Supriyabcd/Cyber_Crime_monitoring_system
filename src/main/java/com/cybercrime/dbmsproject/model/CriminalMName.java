package com.cybercrime.dbmsproject.model;

public class CriminalMName {

    private String mName;
    private Integer criminalId;
    private Integer caseId;
    private Integer officerId;

    public CriminalMName() {}

    public CriminalMName(String mName, Integer criminalId, Integer caseId, Integer officerId) {
        this.mName = mName;
        this.criminalId = criminalId;
        this.caseId = caseId;
        this.officerId = officerId;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public Integer getCriminalId() {
        return criminalId;
    }

    public void setCriminalId(Integer criminalId) {
        this.criminalId = criminalId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Integer officerId) {
        this.officerId = officerId;
    }
}
