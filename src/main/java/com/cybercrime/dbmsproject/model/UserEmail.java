package com.cybercrime.dbmsproject.model;

public class UserEmail {
    private String email;
    private Integer userId;

    public UserEmail() {}

    public UserEmail(String email, Integer userId) {
        this.email = email;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
