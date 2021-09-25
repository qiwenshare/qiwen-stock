package com.qiwenshare.stock.domain;

import lombok.Data;

@Data
public class StockKlineBean {
    private String date;
    private String kdj;
    private String kline;
    private String ma5;
    private String ma10;
    private String ma20;
    private String macd;
    private String rsi;

}
