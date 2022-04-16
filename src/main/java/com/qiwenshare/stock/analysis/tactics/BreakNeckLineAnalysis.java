package com.qiwenshare.stock.analysis.tactics;

import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.indicator.product.KDJ;

import java.util.List;

public class BreakNeckLineAnalysis extends TacticsAnalysis {
    @Override
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        int stockDayInfoListSize = stockDayInfoList.size();
        ReplayBean replayBean = new ReplayBean();
        if (stockDayInfoListSize < 130) {
            return replayBean;
        }

        double lowestOfUp = 100000;

        int low_i_index = 0;
        StockDayInfo currentStockDayInfo = stockDayInfoList.get(stockDayInfoListSize - 1);
        StockDayInfo preStockDayInfo = stockDayInfoList.get(stockDayInfoListSize - 2);
//        StockDayInfo beforePreStockDayInfo = stockDayInfoList.get(stockDayInfoListSize - 3);
        double currentClosePrise = currentStockDayInfo.getClose();
        double preClosePrise = preStockDayInfo.getClose();

        //       double getLowOfTimeday = getLowOfTimeday(stockDayInfoList, 60);
        //       double updownrangeTimeDay = (currentClosePrise - getLowOfTimeday) / getLowOfTimeday;

//        for (int i = stockDayInfoListSize - 2; i > stockDayInfoListSize - 12; i--) {
//            if (stockDayInfoList.get(i).getHigh() > currentClosePrise) {
//                return replayBean;
//            }
//        }

//        if (updownrangeTimeDay >= 0.4){
//            return replayBean;
//        }

//        for (int i = stockDayInfoSize - 2; i > stockDayInfoSize - 10; i--) {
//            if (stockDayInfoList.get(i).getHigh() >= currentClosePrise) {
//                return replayBean;
//            }
//        }

        //低
        for (int i = stockDayInfoListSize - 3; i > stockDayInfoListSize - 123; i--) {
            double pre2Ma20 = stockDayInfoList.get(i - 2).getMa10();
            double pre4Ma20 = stockDayInfoList.get(i - 4).getMa10();
            double curMa20 = stockDayInfoList.get(i).getMa10();
            double after2Ma20 = stockDayInfoList.get(i + 2).getMa10();
            if (pre4Ma20 > pre2Ma20 && pre2Ma20 > curMa20
                    && curMa20 < after2Ma20) {
                for (int j = i - 10; j < i + 1; j++) {
                    if (stockDayInfoList.get(j).getLow() < lowestOfUp) {
                        lowestOfUp = stockDayInfoList.get(j).getLow();
                        low_i_index = i;
                    }
                }
                break;
            }

        }

        //高
        double neckline = 0;
        String formatDate = "";
        while (low_i_index > 30) {
            double pre2Ma20 = stockDayInfoList.get(low_i_index - 2).getMa10();
            double pre4Ma20 = stockDayInfoList.get(low_i_index - 4).getMa10();
            double pre6Ma20 = stockDayInfoList.get(low_i_index - 6).getMa10();
            double curMa20 = stockDayInfoList.get(low_i_index).getMa10();
            if (low_i_index + 2 >= stockDayInfoListSize) {
                low_i_index--;
                continue;
            }
            double after2Ma20 = stockDayInfoList.get(low_i_index + 2).getMa10();
            if (pre6Ma20 < pre4Ma20 && pre4Ma20 < pre2Ma20 && pre2Ma20 < curMa20
                    && curMa20 > after2Ma20) {
                for (int j = low_i_index - 30; j < low_i_index + 1; j++) {
                    if (stockDayInfoList.get(j).getHigh() > neckline) {
                        neckline = stockDayInfoList.get(j).getHigh();
                        formatDate = stockDayInfoList.get(j).getDate();
                    }
                }
                break;
            }
            low_i_index--;
        }
        KDJ kdj = new KDJ();
        if (preStockDayInfo.getClose() < neckline
                && (currentStockDayInfo.getClose() - neckline) / neckline > 0.03
                && currentStockDayInfo.getMa10() > preStockDayInfo.getMa10()
                && currentStockDayInfo.getVolume() > currentStockDayInfo.getVolume120()
                && currentClosePrise > preClosePrise
                && !kdj.isOverBought(currentStockDayInfo)) { //没有超买
            replayBean.setBought(1);
            replayBean.setStatusCode(5891);
            replayBean.setDate(currentStockDayInfo.getDate());
            replayBean.setReason("颈线突破：" + neckline + "颈线时间：" + formatDate);
        }
        return replayBean;
    }


    public double getLowOfTimeday(List<StockDayInfo> stockDayInfoList, int timeday) {
        int stockDayInfoListSize = stockDayInfoList.size();
        double lowestOfTimeday = 100000;
        for (int i = stockDayInfoListSize - 1; i > stockDayInfoListSize - timeday; i--) {
            if (stockDayInfoList.get(i).getClose() < lowestOfTimeday) {
                lowestOfTimeday = stockDayInfoList.get(i).getClose();
            }
        }
        return lowestOfTimeday;
    }
}
