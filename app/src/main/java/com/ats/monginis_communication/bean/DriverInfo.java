package com.ats.monginis_communication.bean;

public class DriverInfo {

    private Integer tranId;
    private String tranDate;
    private Integer vehId;
    private String vehNo;
    private Integer drvId;
    private String driverName;
    private String mobile1;
    private String mobile2;


    public Integer getTranId() {
        return tranId;
    }

    public void setTranId(Integer tranId) {
        this.tranId = tranId;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public Integer getVehId() {
        return vehId;
    }

    public void setVehId(Integer vehId) {
        this.vehId = vehId;
    }

    public String getVehNo() {
        return vehNo;
    }

    public void setVehNo(String vehNo) {
        this.vehNo = vehNo;
    }

    public Integer getDrvId() {
        return drvId;
    }

    public void setDrvId(Integer drvId) {
        this.drvId = drvId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }


    @Override
    public String toString() {
        return "DriverInfo{" +
                "tranId=" + tranId +
                ", tranDate='" + tranDate + '\'' +
                ", vehId=" + vehId +
                ", vehNo='" + vehNo + '\'' +
                ", drvId=" + drvId +
                ", driverName='" + driverName + '\'' +
                ", mobile1='" + mobile1 + '\'' +
                ", mobile2='" + mobile2 + '\'' +
                '}';
    }
}
