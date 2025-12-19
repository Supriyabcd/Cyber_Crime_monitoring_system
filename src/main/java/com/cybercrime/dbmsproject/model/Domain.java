package com.cybercrime.dbmsproject.model;

import java.util.List;

public class Domain {
    private int domainId;
    private String domainName;
    private String createdOn;
    private boolean isActive;

    public Domain() {}

    public Domain(int domainId, String domainName, String createdOn, boolean isActive) {
        this.domainId = domainId;
        this.domainName = domainName;
        this.createdOn = createdOn;
        this.isActive = isActive;
    }

    public int getDomainId() { return domainId; }
    public void setDomainId(int domainId) { this.domainId = domainId; }

    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }

    public String getCreatedOn() { return createdOn; }
    public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }


}
