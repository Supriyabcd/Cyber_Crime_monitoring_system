package com.cybercrime.dbmsproject.model;

public class Officer {
    private int officerId;
    private String fname;
    private String lname;
    private String password;
    private String mobNo;
    private boolean activeStatus;
    private String joinDate;   // you can also use java.sql.Date
    private Integer domainId;  // FK (nullable)

    public Officer() {}

    public Officer(int officerId, String fname, String lname, String password,
                   String mobNo, boolean activeStatus, String joinDate, Integer domainId) {
        this.officerId = officerId;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.mobNo = mobNo;
        this.activeStatus = activeStatus;
        this.joinDate = joinDate;
        this.domainId = domainId;
    }

    public int getOfficerId() { return officerId; }
    public void setOfficerId(int officerId) { this.officerId = officerId; }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getMobNo() { return mobNo; }
    public void setMobNo(String mobNo) { this.mobNo = mobNo; }

    public boolean isActiveStatus() { return activeStatus; }
    public void setActiveStatus(boolean activeStatus) { this.activeStatus = activeStatus; }

    public String getJoinDate() { return joinDate; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }

    public Integer getDomainId() { return domainId; }
    public void setDomainId(Integer domainId) { this.domainId = domainId; }
}
