package com.qiwenshare.stock.domain;

import java.sql.Date;

public class StockDayInfo {
    private String dayinfoid;

    private long stockid;

    /**
     * 股票代码
     */
    private String stockcode;

    /**
     * 日期
     */
    private Date date;
    /**
     * 今开
     */
    private double open;
    /**
     * 最高
     */
    private double high;
    /**
     * 最低
     */
    private double low;
    /**
     * 今收
     */
    private double close;
    /**
     * 成交量
     */
    private double volume;
    /**
     * 成交额
     */
    private double amount;
    private double ccl;
    /**
     * 昨收
     */
    private double preClose;
    /**
     * 涨幅
     */
    private double netChangeRatio;
    //指标
    private double k;
    private double d;
    private double j;
    private double kline;
    private double ma5;
    private double ma10;
    private double ma20;
    private double ma30;
    private double ma60;
    private double ma120;
    private double ma200;
    private double ma250;
    private double volume120;
    private double dif;
    private double dea;
    private double macd;
    private double rsi6;
    private double rsi12;
    private double rsi24;
    private double wr10;
    private double wr6;


    public StockDayInfo() {
        super();
    }

    public StockDayInfo(String stockcode, Date date, double open, double high, double low, double close,
                        double volume, double amount) {
        super();
        this.stockcode = stockcode;
        if (date == null) {
            this.date = null;
        } else {
            this.date = (Date) date.clone();
        }
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.amount = amount;
    }

    public String getDayinfoid() {
        return dayinfoid;
    }

    public void setDayinfoid(String dayinfoid) {
        this.dayinfoid = dayinfoid;
    }

    public long getStockid() {
        return stockid;
    }

    public void setStockid(long stockid) {
        this.stockid = stockid;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public Date getDate() {
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    public void setDate(Date date) {
        if (date == null) {
            this.date = null;
        } else {
            this.date = (Date) date.clone();
        }

    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getVolume120() {
        return volume120;
    }

    public void setVolume120(double volume120) {
        this.volume120 = volume120;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCcl() {
        return ccl;
    }

    public void setCcl(double ccl) {
        this.ccl = ccl;
    }

    public double getPreClose() {
        return preClose;
    }

    public void setPreClose(double preClose) {
        this.preClose = preClose;
    }

    public double getNetChangeRatio() {
        return netChangeRatio;
    }

    public void setNetChangeRatio(double netChangeRatio) {
        this.netChangeRatio = netChangeRatio;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getJ() {
        return j;
    }

    public void setJ(double j) {
        this.j = j;
    }

    public double getKline() {
        return kline;
    }

    public void setKline(double kline) {
        this.kline = kline;
    }

    public double getMa5() {
        return ma5;
    }

    public void setMa5(double ma5) {
        this.ma5 = ma5;
    }

    public double getMa10() {
        return ma10;
    }

    public void setMa10(double ma10) {
        this.ma10 = ma10;
    }

    public double getMa20() {
        return ma20;
    }

    public void setMa20(double ma20) {
        this.ma20 = ma20;
    }

    public double getMa30() {
        return ma30;
    }

    public void setMa30(double ma30) {
        this.ma30 = ma30;
    }

    public double getMa60() {
        return ma60;
    }

    public void setMa60(double ma60) {
        this.ma60 = ma60;
    }

    public double getMa120() {
        return ma120;
    }

    public void setMa120(double ma120) {
        this.ma120 = ma120;
    }

    public double getMa200() {
        return ma200;
    }

    public void setMa200(double ma200) {
        this.ma200 = ma200;
    }

    public double getMa250() {
        return ma250;
    }

    public void setMa250(double ma250) {
        this.ma250 = ma250;
    }

    public double getDif() {
        return dif;
    }

    public void setDif(double dif) {
        this.dif = dif;
    }

    public double getDea() {
        return dea;
    }

    public void setDea(double dea) {
        this.dea = dea;
    }

    public double getMacd() {
        return macd;
    }

    public void setMacd(double macd) {
        this.macd = macd;
    }

    public double getRsi6() {
        return rsi6;
    }

    public void setRsi6(double rsi6) {
        this.rsi6 = rsi6;
    }

    public double getRsi12() {
        return rsi12;
    }

    public void setRsi12(double rsi12) {
        this.rsi12 = rsi12;
    }

    public double getRsi24() {
        return rsi24;
    }

    public void setRsi24(double rsi24) {
        this.rsi24 = rsi24;
    }

    public double getWr10() {
        return wr10;
    }

    public void setWr10(double wr10) {
        this.wr10 = wr10;
    }

    public double getWr6() {
        return wr6;
    }

    public void setWr6(double wr6) {
        this.wr6 = wr6;
    }
}
