package com.qiwenshare.stock.domain;

import lombok.Data;

import java.util.List;

@Data
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

}
