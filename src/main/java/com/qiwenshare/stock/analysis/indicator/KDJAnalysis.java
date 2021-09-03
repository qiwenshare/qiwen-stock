package com.qiwenshare.stock.analysis.indicator;

import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.ArrayList;
import java.util.List;

public class KDJAnalysis extends IndicatorAnalysis {

    public Boolean isOverbought(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        StockDayInfo stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1);
        double kValue = stockdayinfo.getK();
        double dValue = stockdayinfo.getD();
        if (kValue > 90 || dValue > 80) {
            return true;
        }
        return false;
    }

    public Boolean isOversold(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        StockDayInfo stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1);
        double kValue = stockdayinfo.getK();
        double dValue = stockdayinfo.getD();
        if (kValue < 10 || dValue < 20) {
            return true;
        }
        return false;
    }

    public boolean isKdjGoldencross(List<StockDayInfo> stockdayinfoList) {
        if (isOversold(stockdayinfoList)
                && KUpCrossD(stockdayinfoList)) {
            return true;
        }
        return false;
    }

    public boolean isKdjdeathcross(List<StockDayInfo> stockdayinfoList) {
        if (isOverbought(stockdayinfoList)
                && KDownCrossD(stockdayinfoList)) {
            return true;
        }
        return false;
    }

    public boolean isShortUpwardTrend(List<StockDayInfo> stockdayinfoList) {
        int trendDay = 3;
        int stockdayinfoListSize = stockdayinfoList.size();
        List<Double> kValueList = new ArrayList<>();
        List<Double> dValueList = new ArrayList<>();

        for (int i = stockdayinfoListSize - trendDay; i < stockdayinfoListSize; i++) {
            kValueList.add(stockdayinfoList.get(i).getK());
            dValueList.add(stockdayinfoList.get(i).getD());
        }
        if (isDecreaseKAndD(kValueList, dValueList)
                && isSlowKAndD(kValueList, dValueList)) {
            return true;
        }

        return false;
    }

    public boolean isShortDownwardTrend(List<StockDayInfo> stockdayinfoList) {
        int trendDay = 3;
        int stockdayinfoListSize = stockdayinfoList.size();
        List<Double> kValueList = new ArrayList<>();
        List<Double> dValueList = new ArrayList<>();

        for (int i = stockdayinfoListSize - trendDay; i < stockdayinfoListSize; i++) {
            kValueList.add(stockdayinfoList.get(i).getK());
            dValueList.add(stockdayinfoList.get(i).getD());
        }
        if (isIncreaseKAndD(kValueList, dValueList)
                && isSlowKAndD(kValueList, dValueList)) {
            return true;
        }

        return false;
    }

    public boolean KUpCrossD(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        StockDayInfo stockdayinfoPre = stockdayinfoList.get(stockdayinfoListSize - 2);
        StockDayInfo stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1);
        double preK = stockdayinfoPre.getK();
        double preD = stockdayinfoPre.getD();
        double k = stockdayinfo.getK();
        double d = stockdayinfo.getD();
        if (k > d && preK < preD) {
            return true;
        }
        return false;
    }

    public boolean KDownCrossD(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        StockDayInfo stockdayinfoPre = stockdayinfoList.get(stockdayinfoListSize - 2);
        StockDayInfo stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1);
        double preK = stockdayinfoPre.getK();
        double preD = stockdayinfoPre.getD();
        double k = stockdayinfo.getK();
        double d = stockdayinfo.getD();
        if (k < d && preK > preD) {
            return true;
        }
        return false;
    }

    public boolean isSlowKAndD(List<Double> kValueList, List<Double> dValueList) {
        if (doubleArrayIsSlow(kValueList, kValueList.size()) &&
                doubleArrayIsSlow(dValueList, dValueList.size())) {
            return true;
        }
        return false;
    }

    public boolean isIncreaseKAndD(List<Double> kValueList, List<Double> dValueList) {
        if (doubleArrayIsIncrease(kValueList, kValueList.size()) &&
                doubleArrayIsIncrease(dValueList, dValueList.size())) {
            return true;
        }
        return false;
    }


    public boolean isDecreaseKAndD(List<Double> kValueList, List<Double> dValueList) {
        if (doubleArrayIsDecrease(kValueList, kValueList.size()) &&
                doubleArrayIsDecrease(dValueList, dValueList.size())) {
            return true;
        }
        return false;
    }

    public boolean doubleArrayIsSlow(List<Double> list, int count) {

        if (count == 2) {
            return true;
        }
        boolean isSlow = Math.abs(list.get(count - 1) - list.get(count - 2))
                < Math.abs(list.get(count - 2) - list.get(count - 3));
        return (isSlow && doubleArrayIsIncrease(list, count - 1));
    }

    public boolean doubleArrayIsIncrease(List<Double> list, int count) {

        if (count == 1) {
            return true;
        }
        return (list.get(count - 1) > list.get(count - 2) && doubleArrayIsIncrease(list, count - 1));
    }

    public boolean doubleArrayIsDecrease(List<Double> list, int count) {

        if (count == 1) {
            return true;
        }
        return (list.get(count - 1) < list.get(count - 2) && doubleArrayIsDecrease(list, count - 1));
    }

    public double getRSV(double Cn, double Ln, double Hn) {
        double rsv = (Cn - Ln) / (Hn - Ln) * 100;
        return rsv;
    }

    @Override
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        return null;
    }
}
