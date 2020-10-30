package com.ats.monginis_communication.bean;

import java.util.List;

public class NewTrayReport {

    private String id;
    private int tranId;
    private int tranDetailId;

    private String outtrayDate;
    private int outtrayBig;
    private int outtraySmall;
    private int outtrayLead;

    private int intrayId;
    private int tranIntrayId;
    private String intrayDate;
    private int intrayBig;
    private int intrayLead;
    private int intraySmall;

    private int balSmall;
    private int balBig;
    private int balLead;

    private List<NewTrayReport> intrayList = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTranId() {
        return tranId;
    }

    public void setTranId(int tranId) {
        this.tranId = tranId;
    }

    public int getTranDetailId() {
        return tranDetailId;
    }

    public void setTranDetailId(int tranDetailId) {
        this.tranDetailId = tranDetailId;
    }

    public String getOuttrayDate() {
        return outtrayDate;
    }

    public void setOuttrayDate(String outtrayDate) {
        this.outtrayDate = outtrayDate;
    }

    public int getOuttrayBig() {
        return outtrayBig;
    }

    public void setOuttrayBig(int outtrayBig) {
        this.outtrayBig = outtrayBig;
    }

    public int getOuttraySmall() {
        return outtraySmall;
    }

    public void setOuttraySmall(int outtraySmall) {
        this.outtraySmall = outtraySmall;
    }

    public int getOuttrayLead() {
        return outtrayLead;
    }

    public void setOuttrayLead(int outtrayLead) {
        this.outtrayLead = outtrayLead;
    }

    public int getIntrayId() {
        return intrayId;
    }

    public void setIntrayId(int intrayId) {
        this.intrayId = intrayId;
    }

    public int getTranIntrayId() {
        return tranIntrayId;
    }

    public void setTranIntrayId(int tranIntrayId) {
        this.tranIntrayId = tranIntrayId;
    }

    public String getIntrayDate() {
        return intrayDate;
    }

    public void setIntrayDate(String intrayDate) {
        this.intrayDate = intrayDate;
    }

    public int getIntrayBig() {
        return intrayBig;
    }

    public void setIntrayBig(int intrayBig) {
        this.intrayBig = intrayBig;
    }

    public int getIntrayLead() {
        return intrayLead;
    }

    public void setIntrayLead(int intrayLead) {
        this.intrayLead = intrayLead;
    }

    public int getIntraySmall() {
        return intraySmall;
    }

    public void setIntraySmall(int intraySmall) {
        this.intraySmall = intraySmall;
    }

    public int getBalSmall() {
        return balSmall;
    }

    public void setBalSmall(int balSmall) {
        this.balSmall = balSmall;
    }

    public int getBalBig() {
        return balBig;
    }

    public void setBalBig(int balBig) {
        this.balBig = balBig;
    }

    public int getBalLead() {
        return balLead;
    }

    public void setBalLead(int balLead) {
        this.balLead = balLead;
    }

    public List<NewTrayReport> getIntrayyList() {
        return intrayList;
    }

    public void setIntrayyList(List<NewTrayReport> intrayyList) {
        this.intrayList = intrayyList;

    }


    @Override
    public String toString() {
        return "NewTrayReport{" +
                "id='" + id + '\'' +
                ", tranId=" + tranId +
                ", tranDetailId=" + tranDetailId +
                ", outtrayDate='" + outtrayDate + '\'' +
                ", outtrayBig=" + outtrayBig +
                ", outtraySmall=" + outtraySmall +
                ", outtrayLead=" + outtrayLead +
                ", intrayId=" + intrayId +
                ", tranIntrayId=" + tranIntrayId +
                ", intrayDate='" + intrayDate + '\'' +
                ", intrayBig=" + intrayBig +
                ", intrayLead=" + intrayLead +
                ", intraySmall=" + intraySmall +
                ", balSmall=" + balSmall +
                ", balBig=" + balBig +
                ", balLead=" + balLead +
                ", intrayyList=" + intrayList +
                '}';
    }
}
