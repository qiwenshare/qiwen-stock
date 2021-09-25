package com.qiwenshare.stock.domain;


import lombok.Data;

@Data
public class StockKLineObj {
    private String code;
    private String total;
    private String begin;
    private String end;
    private String kline;


}
