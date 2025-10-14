package com.cybercrime.dbmsproject.model;

public class UserMName {
    private String mName;
    private Integer userId;

    public UserMName() {}

    public UserMName(String mName, Integer userId) {
        this.mName = mName;
        this.userId = userId;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
