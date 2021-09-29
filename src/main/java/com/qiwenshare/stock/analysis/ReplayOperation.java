package com.qiwenshare.stock.analysis;

import com.qiwenshare.stock.analysis.indicator.MACDAnalysis;
import com.qiwenshare.stock.analysis.tactics.BreakNeckLineAnalysis;
import com.qiwenshare.stock.analysis.tactics.CattleCatchingAnalysis;
import com.qiwenshare.stock.analysis.tactics.DragonVeinAnalysis;
import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReplayOperation {

    public List<ReplayBean> getReplayInfo(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        List<ReplayBean> replayBeanList = new ArrayList<>();
        Collections.reverse(stockDayInfoList);

        TechnologyAnalysis IndicatorAnalysis = new MACDAnalysis();
        TechnologyAnalysis cattleCatchingAnalysis = new CattleCatchingAnalysis();
        TechnologyAnalysis breakNeckLinkAnalysis = new BreakNeckLineAnalysis();
        TechnologyAnalysis dragonVeinAnalysis = new DragonVeinAnalysis();
        ArrayList<StockDayInfo> analysisStockDayInfos = new ArrayList<StockDayInfo>();
        for (int i = 0; i < stockDayInfoList.size(); i++) {
            analysisStockDayInfos.add(stockDayInfoList.get(i));
            if (analysisStockDayInfos.size() < 3) {
                continue;
            }
            //1、指标分析
            ReplayBean indicatorReplayBean = IndicatorAnalysis.getOperation(analysisStockDayInfos, stockBean);
            ReplayBean cattleCatchingReplayBean = cattleCatchingAnalysis.getOperation(analysisStockDayInfos, stockBean);
            ReplayBean breakNeckLinkReplayBean = breakNeckLinkAnalysis.getOperation(analysisStockDayInfos, stockBean);
            ReplayBean dragonVeinReplayBean = dragonVeinAnalysis.getOperation(analysisStockDayInfos, stockBean);
            String reason = "";
            ReplayBean replayBean = new ReplayBean();
            if (//indicatorReplayBean.getBought() == 1||
                //cattleCatchingReplayBean.getBought() == 1
                //      ||
                    breakNeckLinkReplayBean.getBought() == 1
                //    ||
                //    dragonVeinReplayBean.getBought() == 1
            ) {
//                if (indicatorReplayBean.getBought() == 1){
//                    reason += indicatorReplayBean.getReason();
//                }
//                if (cattleCatchingReplayBean.getBought() == 1) {
//                    reason += cattleCatchingReplayBean.getReason();
//                }
                if (breakNeckLinkReplayBean.getBought() == 1) {
                    reason += breakNeckLinkReplayBean.getReason();
                }
//                if (dragonVeinReplayBean.getBought() == 1) {
//                    reason += dragonVeinReplayBean.getReason();
//                }
                double profit = profit(stockDayInfoList, i);
                replayBean.setProfit(profit);
                if (profit > 0.05) {
                    replayBean.setSuccess(1);
                } else {
                    replayBean.setSuccess(0);
                }
                replayBean.setReason(reason);
                replayBean.setBought(1);
                replayBean.setStockNum(stockBean.getStockNum());
                replayBean.setDate(stockDayInfoList.get(i).getDate());
                replayBean.setStatusCode(indicatorReplayBean.getStatusCode());
                replayBean.setClosePrice(stockDayInfoList.get(i).getClose());
                replayBeanList.add(replayBean);
            }
//
        }

        return replayBeanList;
    }

    public double profit(List<StockDayInfo> stockDayInfoList, int index) {
        int stockDayInfoListSize = stockDayInfoList.size();
        double beginPrice = stockDayInfoList.get(index).getClose();
        if (index + 30 >= stockDayInfoListSize) {
            return 0;
        }
        double highClosePrice = 0;

        for (int i = index + 1; i < index + 31; i++) {
            double tempClosePrice = stockDayInfoList.get(i).getClose();
            if (tempClosePrice > highClosePrice) {
                highClosePrice = tempClosePrice;
            }
        }
        return (highClosePrice - beginPrice) / beginPrice;
//        if ((highClosePrice - beginPrice) / beginPrice > 0.10) {
//            return true;
//        } else {
//            return false;
//        }
    }


}
