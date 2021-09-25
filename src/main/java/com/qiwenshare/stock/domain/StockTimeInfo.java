package com.qiwenshare.stock.domain;

import lombok.Data;

@Data
public class StockTimeInfo {
    private int timeInfoId;
    private int stockId;
    private String stockCode;
    private double amount;
    private double price;
    private double avgPrice;
    private String date;
    private String time;
    private double volume;
    /**
     * 涨跌幅
     */
    private double upDownRange;
    private String stockTableName;

}
