package com.qiwenshare.stock.domain;

public class StockTimeInfo {
    private int timeinfoid;
    private int stockid;
    private String stockcode;
    private double amount;
    private double price;
    private double avgprice;
    private String date;
    private String time;
    private double volume;
    /**
     * 涨跌幅
     */
    private double updownrange;
    private String stockTableName;

    public int getTimeinfoid() {
        return timeinfoid;
    }

    public void setTimeinfoid(int timeinfoid) {
        this.timeinfoid = timeinfoid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAvgprice() {
        return avgprice;
    }

    public void setAvgprice(double avgprice) {
        this.avgprice = avgprice;
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

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getStockid() {
        return stockid;
    }

    public void setStockid(int stockid) {
        this.stockid = stockid;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public String getStockTableName() {
        return stockTableName;
    }

    public void setStockTableName(String stockTableName) {
        this.stockTableName = stockTableName;
    }

    public double getUpdownrange() {
        return updownrange;
    }

    public void setUpdownrange(double updownrange) {
        this.updownrange = updownrange;
    }
}
