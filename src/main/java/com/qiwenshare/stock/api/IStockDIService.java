package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.List;

public interface IStockDIService {

//    List<StockBean> selectStockBeanList(String key);

    //mapper
    void insertStockList(List<StockBean> stockBeanList);

    public void createStockInfoTable(String stockNum);


    List<StockBean> selectStockList(String key, Long beginCount, Long pageCount);

    StockBean getStockInfoById(String stockId);

    int getStockCount(String key, Long beginCount, Long pageCount);

    List<StockBean> selectTotalStockList();

    List<StockBean> getNoExistStockList(List<StockBean> stockBeanList);

    List<StockBean> getStockListByScript();



    StockBean getStockInfo(StockBean stockBean, List<StockDayInfo> stockdayinfoList);

    void updateStock(StockBean stockBean);


}
