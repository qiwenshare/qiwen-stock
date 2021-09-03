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

    EchnicalaspectBean getEchnicalaspectBean(long stockid);


    EchnicalaspectBean getEchnicalaspectInfo(List<StockDayInfo> stockDayInfoList, StockBean stockBean);

    void insertEchnicalaspect(EchnicalaspectBean echnicalaspectBean);

    void insertStockOptional(StockOptionalBean stockOptionalBean);

    void updateEchnicalaspect(EchnicalaspectBean echnicalaspectBean);

    StockBean getStockInfo(StockBean stockBean, List<StockDayInfo> stockdayinfoList);

    void updateStock(StockBean stockBean);

    void insertStockBid(StockBidBean stockBidBean);


    StockBidBean getStockBidBean(String stockNum);


    StockBidBean getBidByStockBean(StockBean stockBean);

    void updateStockBid(StockBidBean stockBidBean);

    void insertAbnormalaAction(AbnormalactionBean abnormalactionBean);

    void updateAbnormalaAction(AbnormalactionBean abnormalactionBean);

    AbnormalactionBean getAbnormalactionBean(long stockid);

    void insertReplay(List<ReplayBean> replayBeanList);

    void deleteReplay(long stockid);

    List<ReplayBean> selectReplayList(long stockid);

    List<ReplayBean> selectAllReplayList(TableQueryBean tableQueryBean);
}
