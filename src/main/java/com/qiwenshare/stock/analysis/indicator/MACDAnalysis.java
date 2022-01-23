package com.qiwenshare.stock.analysis.indicator;

import com.qiwenshare.stock.constant.StatusCode;
import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.*;

public class MACDAnalysis extends IndicatorAnalysis {

    /**
     * 多头行情
     * 操作：可买入开仓或多头持仓
     *
     * @return
     */
    public boolean isBullMarket(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {

        if (isDIFAndDEAGreaterThanZero(currentStockdayinfo)
                && isUpDIFAndDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    /**
     * 空头行情
     * 卖出开仓或观望
     *
     * @return
     */
    public boolean isBearMarket(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {
        if (isDIFAndDEALessThanZero(currentStockdayinfo)
                && isDownDIFAndDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    /**
     * 上升趋势
     * 股票将上涨，可以买入开仓或多头持仓
     *
     * @return
     */
    public boolean isUpTrend(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {
        if (isDIFAndDEALessThanZero(currentStockdayinfo)
                && isUpDIFAndDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    /**
     * 下降趋势
     * 卖出开仓或观望
     *
     * @return
     */
    public boolean isDownTrend(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {
        if (isDIFAndDEAGreaterThanZero(currentStockdayinfo)
                && isDownDIFAndDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    /**
     * macd死叉
     * 卖出信号
     *
     * @return
     */
    public boolean isDIFDownCrossDEA(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {
        if (preStockdayinfo.getDif() > preStockdayinfo.getDea()
                && currentStockdayinfo.getDif() < currentStockdayinfo.getDea()) {
            return true;
        }
        return false;
    }

    /**
     * macd金叉
     * 买入信号
     *
     * @return
     */
    public boolean isDIFUpCrossDEA(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {

        if (preStockdayinfo.getDif() < preStockdayinfo.getDea()
                && currentStockdayinfo.getDif() > currentStockdayinfo.getDea()) {
            return true;
        }
        return false;
    }

    /**
     * 市场由空头转多头
     *
     * @return
     */
    public boolean isMACDGreaterThanZero(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {
        double premacd = preStockdayinfo.getMacd();
        double currentmacd = currentStockdayinfo.getMacd();
        if (premacd < 0 && currentmacd > 0) {
            return true;
        }
        return false;
    }

    /**
     * 市场由多头转空头
     *
     * @return
     */
    public boolean isMACDLessThanZero(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {
        double premacd = preStockdayinfo.getMacd();
        double currentmacd = currentStockdayinfo.getMacd();
        if (premacd > 0 && currentmacd < 0) {
            return true;
        }
        return false;
    }

    public boolean isDIFAndDEAGreaterThanZero(StockDayInfo stockdayinfo) {
        double dif = stockdayinfo.getDif();
        double dea = stockdayinfo.getDea();
        if (dif > 0 && dea > 0) {
            return true;
        }
        return false;
    }

    public boolean isDIFAndDEALessThanZero(StockDayInfo stockdayinfo) {
        double dif = stockdayinfo.getDif();
        double dea = stockdayinfo.getDea();
        if (dif < 0 && dea < 0) {
            return true;
        }
        return false;
    }

    public boolean isUpDIFAndDEA(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {

        if (isUpDIF(preStockdayinfo, currentStockdayinfo)
                && isUpDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    public boolean isDownDIFAndDEA(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {

        if (!isUpDIF(preStockdayinfo, currentStockdayinfo)
                && !isUpDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    public boolean isUpDIF(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {
        if (preStockdayinfo.getDif() < currentStockdayinfo.getDif()) {
            return true;
        }
        return false;
    }

    public boolean isUpDEA(StockDayInfo preStockdayinfo, StockDayInfo currentStockdayinfo) {
        if (preStockdayinfo.getDea() < currentStockdayinfo.getDea()) {
            return true;
        }
        return false;
    }

    /**
     * 顶背离
     *
     * @return
     */
    public Map<String, Boolean> topDivergence(List<StockDayInfo> stockdayinfoList) {
        List<StockDayInfo> topStockDayInfoList = new ArrayList<>();
        Map<String, Boolean> topDivergenceMap = new HashMap<>();
        for (int i = 0; i < stockdayinfoList.size(); i++) {
            if (i == 0 || i == stockdayinfoList.size() - 1) {
                continue;
            }
            StockDayInfo preStockDayInfo = stockdayinfoList.get(i - 1);
            StockDayInfo currentStockDayInfo = stockdayinfoList.get(i);
            StockDayInfo afterStockDayInfo = stockdayinfoList.get(i + 1);
            if (preStockDayInfo.getDif() < currentStockDayInfo.getDif()
                    && currentStockDayInfo.getDif() > afterStockDayInfo.getDif()) {
                topStockDayInfoList.add(currentStockDayInfo);
            }
        }

        for (int i = 0; i < topStockDayInfoList.size(); i++) {
            if (i == 0 || i == topStockDayInfoList.size() - 1) {
                continue;
            }
            if (topStockDayInfoList.get(i).getHigh() > topStockDayInfoList.get(i - 1).getHigh() &&
                    topStockDayInfoList.get(i).getDif() < topStockDayInfoList.get(i - 1).getDif()) {
                topDivergenceMap.put(topStockDayInfoList.get(i).getDate(), true);
            }
        }
        return topDivergenceMap;
    }

    /**
     * 底背离
     *
     * @return
     */
    public Map<String, Boolean> lowDivergence(List<StockDayInfo> stockdayinfoList) {
        List<StockDayInfo> lowStockDayInfoList = new ArrayList<>();
        Map<String, Boolean> lowDivergenceMap = new HashMap();
        for (int i = 0; i < stockdayinfoList.size(); i++) {
            if (i == 0 || i == stockdayinfoList.size() - 1) {
                continue;
            }
            StockDayInfo preStockDayInfo = stockdayinfoList.get(i - 1);
            StockDayInfo currentStockDayInfo = stockdayinfoList.get(i);
            StockDayInfo afterStockDayInfo = stockdayinfoList.get(i + 1);
            if (preStockDayInfo.getDif() > currentStockDayInfo.getDif()
                    && currentStockDayInfo.getDif() < afterStockDayInfo.getDif()) {
                lowStockDayInfoList.add(currentStockDayInfo);
            }

        }

        for (int i = 0; i < lowStockDayInfoList.size(); i++) {
            if (i == 0 || i == lowStockDayInfoList.size() - 1) {
                continue;
            }
            if (lowStockDayInfoList.get(i).getHigh() > lowStockDayInfoList.get(i - 1).getHigh() &&
                    lowStockDayInfoList.get(i).getDif() < lowStockDayInfoList.get(i - 1).getDif()) {
                lowDivergenceMap.put(lowStockDayInfoList.get(i).getDate(), true);
            }
        }
        return lowDivergenceMap;
    }

    @Override
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        int stockDayInfoSize = stockDayInfoList.size();
        StockDayInfo currentStockDayInfo = stockDayInfoList.get(stockDayInfoSize - 1);
        StockDayInfo preStockDayInfo = stockDayInfoList.get(stockDayInfoSize - 2);
        StockDayInfo beforepreStockDayInfo = stockDayInfoList.get(stockDayInfoSize - 3);
        boolean bullMarket = isBullMarket(preStockDayInfo, currentStockDayInfo);
        boolean bearMarket = isBearMarket(preStockDayInfo, currentStockDayInfo);
        boolean upTrend = isUpTrend(preStockDayInfo, currentStockDayInfo);
        boolean downTrend = isDownTrend(preStockDayInfo, currentStockDayInfo);
        boolean isDIFUpCrossDEA = isDIFUpCrossDEA(preStockDayInfo, currentStockDayInfo);
        boolean isDIFDownCrossDEA = isDIFDownCrossDEA(preStockDayInfo, currentStockDayInfo);
        Map<String, Boolean> lowDivergenceMap = lowDivergence(stockDayInfoList);
        Map<String, Boolean> topDivergenceMap = topDivergence(stockDayInfoList);
        boolean isTopDivergence = topDivergenceMap.get(currentStockDayInfo.getDate()) != null ? true : false;
        boolean isLowDivergence = lowDivergenceMap.get(currentStockDayInfo.getDate()) != null ? true : false;

        ReplayBean replayBean = new ReplayBean();
        replayBean.setStockNum(stockBean.getStockNum());
        replayBean.setDate(currentStockDayInfo.getDate());
        replayBean.setClosePrice(currentStockDayInfo.getClose());
        if ((isLowDivergence || bullMarket || upTrend || isDIFUpCrossDEA)
                && !(isTopDivergence || bearMarket || downTrend || isDIFDownCrossDEA)) {
            StringBuffer reason = new StringBuffer();


            if (isLowDivergence) {
                replayBean.setBought(1);
                reason.append("【底背离,短期内可能有反弹，可买入开仓或多头持仓】");
                replayBean.setStatusCode(StatusCode.LOW_DIVERGENCE);

            }
            if (bullMarket) {
                replayBean.setBought(1);
                reason.append("【多头行情,可买入开仓或多头持仓】");
                replayBean.setStatusCode(StatusCode.BULL_MARKET);

            }
            if (upTrend) {
                replayBean.setBought(1);
                reason.append("【上升趋势,股票将上涨，可以买入开仓或多头持仓】");
                replayBean.setStatusCode(StatusCode.UP_TREND);
            }
            if (isDIFUpCrossDEA) {
                replayBean.setBought(1);
                reason.append("【macd金叉,买入信号】");
                replayBean.setStatusCode(StatusCode.DIF_UPCROSS_DEA);
            }

            if (replayBeanList.size() > 1 && replayBeanList.get(replayBeanList.size() - 1).getStatusCode() == 10000
                    && currentStockDayInfo.getClose() < preStockDayInfo.getClose()) {
                reason = new StringBuffer();
                reason.append("【建议卖出,庄家在出货】");
                replayBean.setSold(1);
                replayBean.setBought(0);
                replayBean.setStatusCode(StatusCode.VOLUMNBIGUP_PRICESMALLUP);
                replayBean.setReason(reason.toString());
            }
            if (replayBeanList.size() > 1 && replayBeanList.get(replayBeanList.size() - 1).getStatusCode() == 10001
                    && currentStockDayInfo.getVolume() < preStockDayInfo.getVolume()) {
                reason = new StringBuffer();
                reason.append("【量缩，卖出】");
                replayBean.setSold(1);
                replayBean.setBought(0);
                replayBean.setStatusCode(StatusCode.VOLUMNDOWN_PRICEUP);
                replayBean.setReason(reason.toString());
            }
            if (replayBeanList.size() > 1 && replayBeanList.get(replayBeanList.size() - 1).getStatusCode() == 10002
                    && currentStockDayInfo.getVolume() < preStockDayInfo.getVolume()) {
                reason = new StringBuffer();
                reason.append("【量缩，卖出】");
                replayBean.setSold(1);
                replayBean.setBought(0);
                replayBean.setStatusCode(StatusCode.VOLUMNUP_YINLINE);
                replayBean.setReason(reason.toString());
            }
//                if (volumnDownButPriceUp(beforepreStockDayInfo, preStockDayInfo, currentStockDayInfo)){
//                    reason.append("【建议卖出，缩量上涨，短期会回调】");
//                    replayBean.setSold(1);
//                    replayBean.setBought(0);
//                    replayBean.setStatusCode(StatusCode.VOLUMNDOWN_PRICEUP);
//                    replayBean.setReason(reason.toString());
//                }
            if (volumnBigUpButPriceSmallUp(beforepreStockDayInfo, preStockDayInfo, currentStockDayInfo)) {
                reason.append("【建议卖出，放量缩涨，庄家可能在出货】");
                replayBean.setSold(1);
                replayBean.setBought(0);
                replayBean.setStatusCode(StatusCode.VOLUMNBIGUP_PRICESMALLUP);
                replayBean.setReason(reason.toString());
            }
            if (volumnUpButYinLine(beforepreStockDayInfo, preStockDayInfo, currentStockDayInfo)) {
                reason.append("【建议卖出，放量阴线，庄家可能在出货】");
                replayBean.setSold(1);
                replayBean.setBought(0);
                replayBean.setStatusCode(StatusCode.VOLUMNUP_YINLINE);
                replayBean.setReason(reason.toString());
            }
            replayBean.setReason(reason.toString());
            replayBeanList.add(replayBean);
        }

        if ((isTopDivergence || bearMarket || downTrend || isDIFDownCrossDEA)
                && !(isLowDivergence || bullMarket || upTrend || isDIFUpCrossDEA)) {
            StringBuffer reason = new StringBuffer();
            replayBean.setSold(1);
            if (isTopDivergence) {
                reason.append("【顶背离，,短期内可能见顶，可卖出开仓或观望】");
            }
            if (bearMarket) {
                reason.append("【空头行情,可卖出开仓或观望】");
            }
            if (downTrend) {
                reason.append("【下降趋势,股票处于下跌阶段，可卖出开仓或观望】");
            }
            if (isDIFDownCrossDEA) {
                reason.append("【macd死叉,卖出信号】");
            }
            replayBean.setReason(reason.toString());
            replayBeanList.add(replayBean);
        }

        return replayBean;
    }
}
