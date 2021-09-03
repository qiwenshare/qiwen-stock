package com.qiwenshare.stock.analysis.indicator;

import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.List;

public class MAAnalysis extends IndicatorAnalysis {

    public boolean isGoldenCross(List<StockDayInfo> stockdayinfoList) {
        if (isUpMAShotAndLong(stockdayinfoList) && isShortUpCrossLong(stockdayinfoList)) {
            return true;
        }
        return false;
    }

    public boolean isDeathCross(List<StockDayInfo> stockdayinfoList) {
        if (!isUpMAShotAndLong(stockdayinfoList) && isShortDownCrossLong(stockdayinfoList)) {
            return true;
        }
        return false;
    }

    /**
     * 技术回档
     *
     * @param stockdayinfoList
     * @return
     */
    public boolean isTechnicalreturn(List<StockDayInfo> stockdayinfoList) {
        if (isDownMA10(stockdayinfoList) && isUpMA20(stockdayinfoList)) {
            return true;
        }
        return false;
    }

    public boolean isShortUpCrossLong(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        StockDayInfo stockdayinfofirst = stockdayinfoList.get(stockdayinfoListSize - 2);
        StockDayInfo stockdayinfolast = stockdayinfoList.get(stockdayinfoListSize - 1);
        if (isMA5UpCrossMA10(stockdayinfofirst, stockdayinfolast)
                || isMA10UpCrossMA30(stockdayinfofirst, stockdayinfolast)) {
            return true;
        }
        return false;

    }

    public boolean isShortDownCrossLong(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        StockDayInfo stockdayinfofirst = stockdayinfoList.get(stockdayinfoListSize - 2);
        StockDayInfo stockdayinfolast = stockdayinfoList.get(stockdayinfoListSize - 1);
        if (isMA5DownCrossMA10(stockdayinfofirst, stockdayinfolast)
                || isMA10DownCrossMA30(stockdayinfofirst, stockdayinfolast)) {
            return true;
        }
        return false;

    }

    public Boolean isMA5DownCrossMA10(StockDayInfo stockdayinfofirst, StockDayInfo stockdayinfolast) {
        Double firstMa5 = stockdayinfofirst.getMa5();
        Double firstMa10 = stockdayinfofirst.getMa10();
        Double lastMa5 = stockdayinfolast.getMa5();
        Double lastMa10 = stockdayinfolast.getMa10();
        if (firstMa5 > firstMa10 && lastMa5 < lastMa10) {
            return true;
        }
        return false;

    }

    public Boolean isMA10DownCrossMA30(StockDayInfo stockdayinfofirst, StockDayInfo stockdayinfolast) {
        Double firstMa10 = stockdayinfofirst.getMa10();
        Double firstMa30 = stockdayinfofirst.getMa30();
        Double lastMa10 = stockdayinfolast.getMa10();
        Double lastMa30 = stockdayinfolast.getMa30();
        if (firstMa10 > firstMa30 && lastMa10 < lastMa30) {
            return true;
        }
        return false;

    }

    public Boolean isMA5UpCrossMA10(StockDayInfo stockdayinfofirst, StockDayInfo stockdayinfolast) {
        Double firstMa5 = stockdayinfofirst.getMa5();
        Double firstMa10 = stockdayinfofirst.getMa10();
        Double lastMa5 = stockdayinfolast.getMa5();
        Double lastMa10 = stockdayinfolast.getMa10();
        if (firstMa5 < firstMa10 && lastMa5 > lastMa10) {
            return true;
        }
        return false;

    }

    public Boolean isMA10UpCrossMA30(StockDayInfo stockdayinfofirst, StockDayInfo stockdayinfolast) {
        Double firstMa10 = stockdayinfofirst.getMa10();
        Double firstMa30 = stockdayinfofirst.getMa30();
        Double lastMa10 = stockdayinfolast.getMa10();
        Double lastMa30 = stockdayinfolast.getMa30();
        if (firstMa10 < firstMa30 && lastMa10 > lastMa30) {
            return true;
        }
        return false;

    }

    public Boolean isUpMAShotAndLong(List<StockDayInfo> stockdayinfoList) {
        return isUpMA5(stockdayinfoList) && isUpMA10(stockdayinfoList) && isUpMA20(stockdayinfoList)
                && isUpMA30(stockdayinfoList);
    }

    public boolean isUpMA30(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa30 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa30();
        double ma30 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa30();
        if (preMa30 < ma30) {
            return true;
        }
        return false;
    }

    public boolean isUpMA20(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa20 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa20();
        double ma20 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa20();
        if (preMa20 < ma20) {
            return true;
        }
        return false;
    }

    public boolean isUpMA10(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa10 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa10();
        double ma10 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa10();
        if (preMa10 < ma10) {
            return true;
        }
        return false;
    }

    public boolean isDownMA10(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa10 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa10();
        double ma10 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa10();
        if (preMa10 > ma10) {
            return true;
        }
        return false;
    }

    public boolean isUpMA5(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa5 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa5();
        double ma5 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa5();
        if (preMa5 < ma5) {
            return true;
        }
        return false;
    }

    @Override
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        return null;
    }
}
