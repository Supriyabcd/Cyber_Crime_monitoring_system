package com.cybercrime.dbmsproject.model;

public class OfficerEmail {
    private String email;
    private Integer officerId;

    public OfficerEmail() {}

    public OfficerEmail(String email, Integer officerId) {
        this.email = email;
        this.officerId = officerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Integer officerId) {
        this.officerId = officerId;
    }
}
