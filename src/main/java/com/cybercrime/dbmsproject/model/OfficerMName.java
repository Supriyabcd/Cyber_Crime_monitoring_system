package com.cybercrime.dbmsproject.model;

public class OfficerMName {
    private String mName;
    private Integer officerId;

    public OfficerMName() {}

    public OfficerMName(String mName, Integer officerId) {
        this.mName = mName;
        this.officerId = officerId;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public Integer getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Integer officerId) {
        this.officerId = officerId;
    }
}
