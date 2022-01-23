package com.qiwenshare.stock.indicator.product;

import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MACD implements Indicator {
    private static final Logger LOG = LoggerFactory.getLogger(MACD.class);


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
    public List<StockDayInfo> getDayIndicatorList(List<StockDayInfo> stockdayinfoList) {
        List<StockDayInfo> newstockDayinfoList = new ArrayList<StockDayInfo>();
        StockDayInfo preStockdayinfo = null;
        double preEMA12 = 0;
        double preEMA26 = 0;
        double preDEA = 0;
        for (StockDayInfo stockdayinfo : stockdayinfoList) {
            if (preStockdayinfo == null) {
                preStockdayinfo = stockdayinfo;
                continue;
            }
            double ema12 = preEMA12 * 11 / 13 + stockdayinfo.getClose() * 2 / 13;
            double ema26 = preEMA26 * 25 / 27 + stockdayinfo.getClose() * 2 / 27;
            double dif = ema12 - ema26;
            double dea = preDEA * 8 / 10 + dif * 2 / 10;
            double macd = (dif - dea) * 2;
            stockdayinfo.setDif(dif);
            stockdayinfo.setDea(dea);
            stockdayinfo.setMacd(macd);

            newstockDayinfoList.add(stockdayinfo);
            preStockdayinfo = stockdayinfo;
            preEMA12 = ema12;
            preEMA26 = ema26;
            preDEA = dea;
        }
        return newstockDayinfoList;
    }

    @Override
    public List<StockWeekInfo> getWeekIndicatorList(List<StockWeekInfo> stockweekinfoList) {
        List<StockWeekInfo> newstockWeekinfoList = new ArrayList<StockWeekInfo>();
        StockWeekInfo preStockdayinfo = null;
        double preEMA12 = 0;
        double preEMA26 = 0;
        double preDEA = 0;
        for (StockWeekInfo stockWeekInfo : stockweekinfoList) {
            if (preStockdayinfo == null) {
                preStockdayinfo = stockWeekInfo;
                continue;
            }
            double ema12 = preEMA12 * 11 / 13 + stockWeekInfo.getClose() * 2 / 13;
            double ema26 = preEMA26 * 25 / 27 + stockWeekInfo.getClose() * 2 / 27;
            double dif = ema12 - ema26;
            double dea = preDEA * 8 / 10 + dif * 2 / 10;
            double macd = (dif - dea) * 2;
            stockWeekInfo.setDif(dif);
            stockWeekInfo.setDea(dea);
            stockWeekInfo.setMacd(macd);

            newstockWeekinfoList.add(stockWeekInfo);
            preStockdayinfo = stockWeekInfo;
            preEMA12 = ema12;
            preEMA26 = ema26;
            preDEA = dea;
        }
        return newstockWeekinfoList;
    }

    @Override
    public List<StockMonthInfo> getMonthIndicatorList(List<StockMonthInfo> stockmonthinfoList) {
        List<StockMonthInfo> newstockMonthinfoList = new ArrayList<StockMonthInfo>();
        StockMonthInfo preStockdayinfo = null;
        double preEMA12 = 0;
        double preEMA26 = 0;
        double preDEA = 0;
        for (StockMonthInfo stockMonthInfo : stockmonthinfoList) {
            if (preStockdayinfo == null) {
                preStockdayinfo = stockMonthInfo;
                continue;
            }
            double ema12 = preEMA12 * 11 / 13 + stockMonthInfo.getClose() * 2 / 13;
            double ema26 = preEMA26 * 25 / 27 + stockMonthInfo.getClose() * 2 / 27;
            double dif = ema12 - ema26;
            double dea = preDEA * 8 / 10 + dif * 2 / 10;
            double macd = (dif - dea) * 2;
            stockMonthInfo.setDif(dif);
            stockMonthInfo.setDea(dea);
            stockMonthInfo.setMacd(macd);

            newstockMonthinfoList.add(stockMonthInfo);
            preStockdayinfo = stockMonthInfo;
            preEMA12 = ema12;
            preEMA26 = ema26;
            preDEA = dea;
        }
        return newstockMonthinfoList;
    }
}
