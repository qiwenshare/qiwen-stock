package com.qiwenshare.stock.analysis.indicator;

import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.List;

public class TurnoverRateAnalysis extends IndicatorAnalysis {
    @Override
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        int stockDayInfoListSize = stockDayInfoList.size();
        StockDayInfo currentStockDayInfo = stockDayInfoList.get(stockDayInfoListSize);
        double currentVolume = currentStockDayInfo.getVolume();
        double currentTotalFlowShares = stockBean.getTotalFlowShares();
        double turnOverrate = currentVolume / currentTotalFlowShares;
        if (turnOverrate < 0.03) {
            //观望
        } else if (turnOverrate < 0.07) {
            //原则观望
        } else if (turnOverrate < 0.10) {
            //考虑买入或卖出
        } else if (turnOverrate < 0.15) {
            //大举买入或卖出
        } else if (turnOverrate < 0.25) {
            //短线进入或中线清仓
        } else {
            //清仓走人
        }
        return null;

    }
}
