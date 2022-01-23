package com.qiwenshare.stock.domain;

import lombok.Data;

@Data
public class StockDayInfo {
    private String dayInfoId;



    /**
     * 日期
     */
    private String date;
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

    public StockDayInfo(String date, double open, double high, double low, double close,
                        double volume, double amount) {
        super();
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.amount = amount;
    }


}
