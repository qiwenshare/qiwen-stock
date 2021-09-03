package com.qiwenshare.stock.analysis.tactics;

import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.List;

public class DragonVeinAnalysis extends TacticsAnalysis {
    @Override
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        int stockDayInfoListSize = stockDayInfoList.size();
        ReplayBean replayBean = new ReplayBean();

        if (stockDayInfoListSize < 32) {
            return replayBean;
        }
        StockDayInfo currentStockDayInfo = stockDayInfoList.get(stockDayInfoListSize - 1);
        StockDayInfo preStockDayInfo = stockDayInfoList.get(stockDayInfoListSize - 2);

        //判断之前是否有涨停
        for (int i = stockDayInfoListSize - 1; i > stockDayInfoListSize - 12; i--) {
            double tempCurrentVolumn = stockDayInfoList.get(i).getVolume();
            double tempPreVolumn = stockDayInfoList.get(i - 1).getVolume();
            double tempCurrentClose = stockDayInfoList.get(i).getClose();
            double tempPreClose = stockDayInfoList.get(i - 1).getClose();
            double tempCurrentVolumn120 = stockDayInfoList.get(i).getVolume120();
            if (tempCurrentVolumn < tempCurrentVolumn120) {
                return replayBean;
            }
            //是否存在放量下跌
//            if (tempPreVolumn < tempCurrentVolumn
//                    && tempCurrentClose < tempPreClose){
//                return replayBean;
//            }
        }
        StockDayInfo temp = stockDayInfoList.get(stockDayInfoListSize - 12);
        if (temp.getVolume() < temp.getVolume120()
                && currentStockDayInfo.getMa10() > preStockDayInfo.getMa10()) {
            replayBean.setBought(1);
            replayBean.setStatusCode(7691);
            replayBean.setDate(currentStockDayInfo.getDate());
            replayBean.setReason("龙脉突破");
        }
//        if (currentStockDayInfo.getVolume() > currentStockDayInfo.getVolume120()
//                && preStockDayInfo.getVolume() < preStockDayInfo.getVolume120()) {
//
//        }
        return replayBean;
    }
}
