package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.StockTimeInfo;

import java.util.List;

public interface IStockTimeInfoService {
    public List<StockTimeInfo> getStocktimebar(String stockNum);

    public List<StockTimeInfo> selectStocktimeListByStockNum(StockTimeInfo stockTimeInfo);

    void insertStockTimeInfo(String stockNum, List<StockTimeInfo> stocktimeinfo);

    List<StockTimeInfo> crawlStockTimeInfoList(String stockNum);

}
