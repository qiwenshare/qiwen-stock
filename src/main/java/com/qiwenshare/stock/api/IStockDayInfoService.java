package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.List;

public interface IStockDayInfoService {
    List<StockDayInfo> getStockdaybar(String stockNum);

    void insertStockDayInfo(StockBean stockBean, List<StockDayInfo> stockdayinfo);

    List<StockDayInfo> selectStockDayInfoList(StockBean stockBean);

    List<StockDayInfo> crawlStockDayInfoListByStockBean(String stockNum);
}
