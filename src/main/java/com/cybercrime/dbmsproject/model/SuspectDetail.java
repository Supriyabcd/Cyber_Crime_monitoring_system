package com.cybercrime.dbmsproject.model;

public class SuspectDetail {

    private Integer detailId;
    private String suspectDetails;

    public SuspectDetail() {
    }

    public SuspectDetail(Integer detailId, String suspectDetails) {
        this.detailId = detailId;
        this.suspectDetails = suspectDetails;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public String getSuspectDetails() {
        return suspectDetails;
    }

    public void setSuspectDetails(String suspectDetails) {
        this.suspectDetails = suspectDetails;
    }

    @Override
    public String toString() {
        return "SuspectDetail{" +
                "detailId=" + detailId +
                ", suspectDetails='" + suspectDetails + '\'' +
                '}';
    }
}
