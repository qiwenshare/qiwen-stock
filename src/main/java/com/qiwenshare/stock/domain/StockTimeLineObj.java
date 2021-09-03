package com.qiwenshare.stock.domain;

import java.util.List;

public class StockTimeLineObj {

    private String code;
    private String prev_close;
    private String highest;
    private String lowest;
    private String date;
    private String time;
    private String total;
    private String begin;
    private String end;
    private String line;

    private List<StockTimeInfo> stockTimeInfoList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrev_close() {
        return prev_close;
    }

    public void setPrev_close(String prev_close) {
        this.prev_close = prev_close;
    }

    public String getHighest() {
        return highest;
    }

    public void setHighest(String highest) {
        this.highest = highest;
    }

    public String getLowest() {
        return lowest;
    }

    public void setLowest(String lowest) {
        this.lowest = lowest;
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public List<StockTimeInfo> getStockTimeInfoList() {
        return stockTimeInfoList;
    }

    public void setStockTimeInfoList(List<StockTimeInfo> stockTimeInfoList) {
        this.stockTimeInfoList = stockTimeInfoList;
    }
}
