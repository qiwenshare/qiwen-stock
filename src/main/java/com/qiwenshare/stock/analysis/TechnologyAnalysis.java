package com.qiwenshare.stock.analysis;

import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.List;

public abstract class TechnologyAnalysis {

    public abstract ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean);

    /**
     * 阳线
     *
     * @param stockDayInfo
     * @return
     */
    public boolean isYangLine(StockDayInfo stockDayInfo) {
        double closePrice = stockDayInfo.getClose();
        double openPrice = stockDayInfo.getOpen();
        if (openPrice < closePrice) {
            return true;
        }
        return false;
    }


    /**
     * 阴线
     *
     * @param stockDayInfo
     * @return
     */
    public boolean isYinLine(StockDayInfo stockDayInfo) {
        double closePrice = stockDayInfo.getClose();
        double openPrice = stockDayInfo.getOpen();
        if (openPrice > closePrice) {
            return true;
        }
        return false;
    }

    /**
     * 放量
     *
     * @return
     */
    public boolean volumnUp(StockDayInfo preStockDayInfo, StockDayInfo currentStockDayInfo) {
        if (preStockDayInfo.getVolume() < currentStockDayInfo.getVolume()) {
            return true;
        }
        return false;
    }

    /**
     * 缩量
     *
     * @return
     */
    public boolean volumnDown(StockDayInfo preStockDayInfo, StockDayInfo currentStockDayInfo) {
        if (preStockDayInfo.getVolume() > currentStockDayInfo.getVolume()) {
            return true;
        }
        return false;
    }

    /**
     * 放量阴线，卖出
     *
     * @param beforepreStockDayInfo
     * @param preStockDayInfo
     * @param currentStockDayInfo
     * @return
     */
    public boolean volumnUpButYinLine(StockDayInfo beforepreStockDayInfo, StockDayInfo preStockDayInfo, StockDayInfo currentStockDayInfo) {
        if (beforepreStockDayInfo == null) {
            return false;
        }
        //涨幅
        double prerange = (preStockDayInfo.getClose() - beforepreStockDayInfo.getClose()) / beforepreStockDayInfo.getClose();
        double currentrange = (currentStockDayInfo.getClose() - preStockDayInfo.getClose()) / preStockDayInfo.getClose();
        //成交量
        double preVolume = preStockDayInfo.getVolume();
        double currentVolumn = currentStockDayInfo.getVolume();
        if (isYangLine(preStockDayInfo)
                && isYinLine(currentStockDayInfo)
                //&& currentStockDayInfo.getClose() < preStockDayInfo.getClose()
                && volumnUp(preStockDayInfo, currentStockDayInfo)) {
            return true;
        }
        return false;
    }

    /**
     * 缩量上涨，卖出
     *
     * @param preStockDayInfo
     * @param currentStockDayInfo
     * @return
     */
    public boolean volumnDownButPriceUp(StockDayInfo preStockDayInfo, StockDayInfo currentStockDayInfo) {

        if (//isYangLine(preStockDayInfo)
            //&& isYangLine(currentStockDayInfo)
                currentStockDayInfo.getClose() > preStockDayInfo.getClose()
                        && volumnDown(preStockDayInfo, currentStockDayInfo)) {
            return true;
        }
        return false;
    }

    /**
     * 放量缩涨，卖出(主力出货)
     *
     * @param beforepreStockDayInfo
     * @param preStockDayInfo
     * @param currentStockDayInfo
     * @return
     */
    public boolean volumnBigUpButPriceSmallUp(StockDayInfo beforepreStockDayInfo, StockDayInfo preStockDayInfo, StockDayInfo currentStockDayInfo) {
        if (beforepreStockDayInfo == null) {
            return false;
        }
        double prerange = (preStockDayInfo.getClose() - beforepreStockDayInfo.getClose()) / beforepreStockDayInfo.getClose();
        double currentrange = (currentStockDayInfo.getClose() - preStockDayInfo.getClose()) / preStockDayInfo.getClose();
        double preVolume = preStockDayInfo.getVolume();
        double currentVolumn = currentStockDayInfo.getVolume();
        if (isYangLine(preStockDayInfo)
                && isYangLine(currentStockDayInfo)
                && currentStockDayInfo.getClose() > preStockDayInfo.getClose()
                //&& prerange > 0 && currentrange > 0 //今天和昨天涨幅正的
                && prerange > currentrange  //今天没有昨天涨的多
                && preVolume < currentVolumn) { //今天的成交量大于昨天的
            return true;
        }
        return false;
    }

}
