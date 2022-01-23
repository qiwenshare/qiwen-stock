package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;

import java.util.List;

public interface IStockMonthInfoService {
    List<StockMonthInfo> getStockMonthInfoList(List<StockDayInfo> stockDayInfoList);

    void insertStockMonthInfo(String stockNum, List<StockMonthInfo> stockMonthinfo);

    public List<StockMonthInfo> getStockmonthbar(String stockNum);

    List<StockMonthInfo> selectStockmonthListByStockNum(StockBean stockBean);
}
