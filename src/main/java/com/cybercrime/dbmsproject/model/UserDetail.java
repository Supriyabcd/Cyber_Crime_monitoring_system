package com.cybercrime.dbmsproject.model;

import java.util.List;

public class UserDetail {
    private int userId;
    private String fname;
    private String lname;
    private String mobNo;
    private String dob;
    private String hNo;
    private String streetNo;
    private String city;
    private String state;
    private String username;
    private String password;
    private List<String> mnames;

    // ✅ Default constructor (needed for forms and frameworks)
    public UserDetail() {
    }

    // ✅ Parameterized constructor (you probably already have this)
    public UserDetail(int userId, String fname, String lname, String mobNo, String dob,
                      String hNo, String streetNo, String city, String state,
                      String username, String password) {
        this.userId = userId;
        this.fname = fname;
        this.lname = lname;
        this.mobNo = mobNo;
        this.dob = dob;
        this.hNo = hNo;
        this.streetNo = streetNo;
        this.city = city;
        this.state = state;
        this.username = username;
        this.password = password;
    }
    public List<String> getMnames() { return mnames;}
    public void setMnames(List<String> mnames) { this.mnames = mnames; }

    // ✅ Getters and Setters (required for form binding)
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getMobNo() { return mobNo; }
    public void setMobNo(String mobNo) { this.mobNo = mobNo; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getHNo() { return hNo; }
    public void setHNo(String hNo) { this.hNo = hNo; }

    public String getStreetNo() { return streetNo; }
    public void setStreetNo(String streetNo) { this.streetNo = streetNo; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}



