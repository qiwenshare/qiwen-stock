package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;

import java.util.List;

public interface IStockWeekInfoService {
    List<StockWeekInfo> getStockWeekInfoList(List<StockDayInfo> stockDayInfoList);

    void insertStockWeekInfo(String stockNum, List<StockWeekInfo> stockWeekinfo);

    public List<StockWeekInfo> getStockweekbar(String stockNum);

    List<StockWeekInfo> selectStockweekListByStockNum(StockBean stockBean);
}
