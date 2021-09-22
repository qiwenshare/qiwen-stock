package com.qiwenshare.stock.api;

import com.qiwenshare.stock.common.TableQueryBean;
import com.qiwenshare.stock.domain.*;

import java.util.List;

public interface IStockDIService {

    List<StockBean> selectStockBeanList(String key);

    //mapper
    void insertStockList(List<StockBean> stockBeanList);

    public void createStockInfoTable(String stocknum);

    public void initStockTable();

    List<StockBean> selectStockList(TableQueryBean miniuiTableQueryBean);

    StockBean getStockInfoById(String stockId);

    int getStockCountBySelect(TableQueryBean miniuiTableQueryBean);

    List<StockBean> selectTotalStockList();

    List<StockBean> getNoExistStockList(List<StockBean> stockBeanList);

    List<StockBean> getStockListByScript();



    StockBean getStockInfo(StockBean stockBean, List<StockDayInfo> stockdayinfoList);

    void updateStock(StockBean stockBean);


}
