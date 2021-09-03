package com.qiwenshare.stock.domain;

public class StockBidObj {
    private String code;
    private String date;
    private String time;
    private String snap;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSnap() {
        return snap;
    }

    public void setSnap(String snap) {
        this.snap = snap;
    }
}
