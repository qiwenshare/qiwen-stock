package com.qiwenshare.stock.analysis.tactics;

import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CattleCatchingAnalysis extends TacticsAnalysis {

    private static final Logger LOGGER = LoggerFactory.getLogger(CattleCatchingAnalysis.class);

    @Override
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        //LOGGER.info("----------股票编号："+ stockBean.getStockNum());
        //1、首次放量涨停
        //2、均线呈现发散趋势
        //3、流通市值适中
        //4、套牢筹码不多

        int stockDayInfoListSize = stockDayInfoList.size();
        StockDayInfo stockDayInfo = stockDayInfoList.get(stockDayInfoListSize - 1);
        StockDayInfo preStockDayInfo = stockDayInfoList.get(stockDayInfoListSize - 2);
        double currentClosePrise = stockDayInfo.getClose();
        double preClosePrise = preStockDayInfo.getClose();
        double currentVolumn = stockDayInfo.getVolume();
        double preVolumn = preStockDayInfo.getVolume();
        double upDownRange = (currentClosePrise - preClosePrise) / preClosePrise;
        double ma120 = stockDayInfo.getMa120();
        ReplayBean replayBean = new ReplayBean();

        if (stockDayInfoListSize < 60) {
            return replayBean;
        }
        //判断流通市值
//        if (stockBean.getTotalFlowShares() > 1000000000){
//            return replayBean;
//        }

        //判断之前是否有涨停
        for (int i = stockDayInfoListSize - 2; i > stockDayInfoListSize - 30; i--) {
            double tempCurrentClosePrise = stockDayInfoList.get(i).getClose();

            double tempPreClosePrise = stockDayInfoList.get(i - 1).getClose();
            double tempupdownrange = (tempCurrentClosePrise - tempPreClosePrise) / tempPreClosePrise;
            double tempVolume = stockDayInfoList.get(i).getVolume();

            if (tempupdownrange > 0.09 || tempVolume > currentVolumn) {
                return replayBean;
            }
        }

        if (upDownRange > 0.099
                && upDownRange < 0.11
                && currentVolumn / preVolumn > 2
                && currentVolumn / preVolumn < 5
                && currentClosePrise > ma120 && preClosePrise < ma120) {
            replayBean.setBought(1);
            replayBean.setStatusCode(7691);
            replayBean.setDate(stockDayInfo.getDate());
            replayBean.setReason("牛股开启信号");
        }

        return replayBean;

    }

    //拉高出货
//    public boolean isPumpAndDump(List<StockDayInfo> stockDayInfoList){
//        double highestOfDay30 = 0;
//        double lowestOfDay30 = 0;
//        //1、拉高前已经放量
//        int low_i_index = 0;
//        int stockDayInfoSize = stockDayInfoList.size();
//        StockDayInfo currentStockDayInfo = stockDayInfoList.get(stockDayInfoSize - 1);
//        for(int i = stockDayInfoSize - 2; i > stockDayInfoSize - 32; i--){
//            double tempHighPrise = stockDayInfoList.get(i).getHigh();
//            double tempLowPrise = stockDayInfoList.get(i).getLow();
//
//            if (tempHighPrise > highestOfDay30){
//                highestOfDay30 = tempHighPrise;
//                low_i_index = i;
//            }
//        }
//        low_i_index = low_i_index - 5;
//        double neckline = 10000;
//        while(low_i_index > 1){
//            double preMa5 = stockDayInfoList.get(low_i_index - 1).getMa5();
//            double curMa5 = stockDayInfoList.get(low_i_index).getMa5();
//            double afterMa5 = stockDayInfoList.get(low_i_index + 1).getMa5();
//            if (preMa5 > curMa5 && afterMa5 < curMa5){
//                for (int j = low_i_index - 5; j < low_i_index + 5; j++){
//                    if (stockDayInfoList.get(j).getLow() < neckline){
//                        neckline = stockDayInfoList.get(j).getLow();
//                    }
//                }
//            }
//            low_i_index--;
//        }
//        if (currentStockDayInfo.getHigh() < highestOfDay30){
//            return true;
//        }
//        return false;
//    }


//    public boolean isBreakNeckLine(List<StockDayInfo> stockDayInfoList){
//        double lowestOfUp = 100000;
//        double lowestOfDay60 = 100000;
//
//        int low_i_index = 0;
//        int stockDayInfoSize = stockDayInfoList.size();
//        StockDayInfo currentStockDayInfo = stockDayInfoList.get(stockDayInfoSize - 1);
//        StockDayInfo preStockDayInfo = stockDayInfoList.get(stockDayInfoSize - 2);
//        for(int i = stockDayInfoSize - 2; i > stockDayInfoSize - 32; i--){
//            double preMa10 = stockDayInfoList.get(i - 1).getMa10();
//            double curMa10 = stockDayInfoList.get(i).getMa10();
//            double afterMa10 = stockDayInfoList.get(i + 1).getMa10();
//            if (preMa10 > curMa10 && afterMa10 > curMa10){
//                for (int j = i - 10; j < i + 2; j++){
//                    if (stockDayInfoList.get(j).getLow() < lowestOfUp){
//                        lowestOfUp = stockDayInfoList.get(j).getLow();
//                        low_i_index = i;
//                    }
//                }
//                break;
//            }
//
//        }
////        for(int i = low_i_index; i > stockDayInfoSize - 32; i--){
////            double tempLowPrise = stockDayInfoList.get(i).getLow();
////
////            if (lowestOfDay60 > tempLowPrise){
////                lowestOfDay60 = tempLowPrise;
////                low_i_index = i;
////            }
////        }
////        if (lowestOfUp > lowestOfDay60){
////            return false;
////        }
////        low_i_index = low_i_index - 5;
//        double neckline = 0;
//        while(low_i_index > 1){
//            double preMa10 = stockDayInfoList.get(low_i_index - 1).getMa10();
//            double curMa10 = stockDayInfoList.get(low_i_index).getMa10();
//            double afterMa10 = stockDayInfoList.get(low_i_index + 1).getMa10();
//            if (preMa10 < curMa10 && afterMa10 < curMa10){
//                for (int j = low_i_index - 5; j < low_i_index + 2; j++){
//                    if (stockDayInfoList.get(j).getLow() > neckline){
//                        neckline = stockDayInfoList.get(j).getLow();
//                    }
//                }
//                break;
//            }
//            low_i_index--;
//        }
//        if (preStockDayInfo.getLow() < neckline
//                && currentStockDayInfo.getHigh() > neckline){
//            return true;
//        }
//        return false;
//    }
}
