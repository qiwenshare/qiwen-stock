package com.qiwenshare.stock.domain;

public class StockKLineObj {
    private String code;
    private String total;
    private String begin;
    private String end;
    private String kline;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getKline() {
        return kline;
    }

    public void setKline(String kline) {
        this.kline = kline;
    }

}
