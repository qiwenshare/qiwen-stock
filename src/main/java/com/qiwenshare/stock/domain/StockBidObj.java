package com.qiwenshare.stock.domain;

import lombok.Data;

@Data
public class StockBidObj {
    private String code;
    private String date;
    private String time;
    private String snap;

}
