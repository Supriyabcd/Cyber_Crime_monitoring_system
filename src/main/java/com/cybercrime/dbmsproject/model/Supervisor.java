package com.cybercrime.dbmsproject.model;

public class Supervisor {

    private int supervisorId;
    private int officerId;

    // No-args constructor
    public Supervisor() {
    }

    // All-args constructor
    public Supervisor(int supervisorId, int officerId) {
        this.supervisorId = supervisorId;
        this.officerId = officerId;
    }

    // Getters and setters
    public int getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    @Override
    public String toString() {
        return "Supervisor{" +
                "supervisorId=" + supervisorId +
                ", officerId=" + officerId +
                '}';
    }
}
