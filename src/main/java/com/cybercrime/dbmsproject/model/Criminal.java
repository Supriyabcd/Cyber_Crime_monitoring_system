package com.cybercrime.dbmsproject.model;

import java.time.LocalDate;

public class Criminal {

    private Integer criminalId;
    private Integer caseId;
    private Integer officerId;
    private String fname;
    private String lname;
    private LocalDate dob;
    private String hNo;
    private String street;
    private String city;
    private String state;
    private String mobNo;
    private String socialHandle;

    public Criminal() {}

    public Criminal(Integer criminalId, Integer caseId, Integer officerId, String fname, String lname,
                    LocalDate dob, String hNo, String street, String city, String state,
                    String mobNo, String socialHandle) {
        this.criminalId = criminalId;
        this.caseId = caseId;
        this.officerId = officerId;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.hNo = hNo;
        this.street = street;
        this.city = city;
        this.state = state;
        this.mobNo = mobNo;
        this.socialHandle = socialHandle;
    }

    // Getters and Setters
    public Integer getCriminalId() { return criminalId; }
    public void setCriminalId(Integer criminalId) { this.criminalId = criminalId; }

    public Integer getCaseId() { return caseId; }
    public void setCaseId(Integer caseId) { this.caseId = caseId; }

    public Integer getOfficerId() { return officerId; }
    public void setOfficerId(Integer officerId) { this.officerId = officerId; }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getHNo() { return hNo; }
    public void setHNo(String hNo) { this.hNo = hNo; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getMobNo() { return mobNo; }
    public void setMobNo(String mobNo) { this.mobNo = mobNo; }

    public String getSocialHandle() { return socialHandle; }
    public void setSocialHandle(String socialHandle) { this.socialHandle = socialHandle; }
}
