package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.EchnicalaspectBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockOptionalBean;

import java.util.List;

public interface IEchnicalaspectService {
    EchnicalaspectBean getEchnicalaspectInfo(List<StockDayInfo> stockDayInfoList, StockBean stockBean);

    void insertEchnicalaspect(EchnicalaspectBean echnicalaspectBean);



    void updateEchnicalaspect(EchnicalaspectBean echnicalaspectBean);

    EchnicalaspectBean getEchnicalaspectBean(long stockid);


}
