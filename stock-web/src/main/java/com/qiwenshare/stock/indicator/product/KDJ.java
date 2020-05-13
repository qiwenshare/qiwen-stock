package com.qiwenshare.stock.indicator.product;

import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class KDJ implements Indicator {

    public boolean isOverBought(double kValue, double dValue) {
        if (kValue > 90 || dValue > 80) {
            return true;
        }
        return false;
    }

    public boolean isOverBought(StockDayInfo stockDayInfo) {
        double kValue = stockDayInfo.getK();
        double dValue = stockDayInfo.getD();
        return isOverBought(kValue, dValue);
    }

    public Boolean isOverbought(List<StockDayInfo> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        StockDayInfo stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1);
        return isOverBought(stockdayinfo);
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
//
//	public List<StockDayInfo> setStockKDJInfo(List<StockDayInfo> StockdayinfoList) {
//
//
//	}

    public double getRSV(double Cn, double Ln, double Hn) {
        double rsv = (Cn - Ln) / (Hn - Ln) * 100;
        return rsv;
    }

    @Override
    public List<StockDayInfo> getDayIndicatorList(List<StockDayInfo> stockdayinfoList) {
        Queue<StockDayInfo> queue9 = new LinkedList<StockDayInfo>();

        List<StockDayInfo> newstockDayinfoList = new ArrayList<StockDayInfo>();
        double preKValue = 0;
        double preDValue = 0;
        for (StockDayInfo stockdayinfo : stockdayinfoList) {
            double high = 0;
            double low = 1000000;
            double close = 0;

            queue9.offer(stockdayinfo);

            if (queue9.size() == 9) {
                for (StockDayInfo d : queue9) {
                    if (d.getHigh() > high) {
                        high = d.getHigh();
                    }
                    if (d.getLow() < low) {
                        low = d.getLow();
                    }
                    close = d.getClose();
                }

                double rsvValue = getRSV(close, low, high);
                if (preKValue == 0) {
                    preKValue = 50;
                }
                if (preDValue == 0) {
                    preDValue = 50;
                }
                double kValue = ((double) 2 / 3) * preKValue + ((double) 1 / 3) * rsvValue;
                double dValue = ((double) 2 / 3) * preDValue + ((double) 1 / 3) * kValue;
                double jValue = 3 * kValue - 2 * dValue;
                stockdayinfo.setK(kValue);
                stockdayinfo.setD(dValue);
                stockdayinfo.setJ(jValue);
                preKValue = kValue;
                preDValue = dValue;
                queue9.poll();
            }

            newstockDayinfoList.add(stockdayinfo);
        }
        return newstockDayinfoList;

    }

    @Override
    public List<StockWeekInfo> getWeekIndicatorList(List<StockWeekInfo> stockweekinfoList) {
        Queue<StockWeekInfo> queue9 = new LinkedList<StockWeekInfo>();

        List<StockWeekInfo> newstockWeekinfoList = new ArrayList<StockWeekInfo>();
        double preKValue = 0;
        double preDValue = 0;
        for (StockWeekInfo stockWeekInfo : stockweekinfoList) {
            double high = 0;
            double low = 1000000;
            double close = 0;

            queue9.offer(stockWeekInfo);

            if (queue9.size() == 9) {
                for (StockWeekInfo d : queue9) {
                    if (d.getHigh() > high) {
                        high = d.getHigh();
                    }
                    if (d.getLow() < low) {
                        low = d.getLow();
                    }
                    close = d.getClose();
                }

                double rsvValue = getRSV(close, low, high);
                if (preKValue == 0) {
                    preKValue = 50;
                }
                if (preDValue == 0) {
                    preDValue = 50;
                }
                double kValue = ((double) 2 / 3) * preKValue + ((double) 1 / 3) * rsvValue;
                double dValue = ((double) 2 / 3) * preDValue + ((double) 1 / 3) * kValue;
                double jValue = 3 * kValue - 2 * dValue;
                stockWeekInfo.setK(kValue);
                stockWeekInfo.setD(dValue);
                stockWeekInfo.setJ(jValue);
                preKValue = kValue;
                preDValue = dValue;
                queue9.poll();
            }

            newstockWeekinfoList.add(stockWeekInfo);
        }
        return newstockWeekinfoList;
    }

    @Override
    public List<StockMonthInfo> getMonthIndicatorList(List<StockMonthInfo> stockmonthinfoList) {
        Queue<StockMonthInfo> queue9 = new LinkedList<StockMonthInfo>();

        List<StockMonthInfo> newstockMonthinfoList = new ArrayList<StockMonthInfo>();
        double preKValue = 0;
        double preDValue = 0;
        for (StockMonthInfo stockMonthInfo : stockmonthinfoList) {
            double high = 0;
            double low = 1000000;
            double close = 0;

            queue9.offer(stockMonthInfo);

            if (queue9.size() == 9) {
                for (StockMonthInfo d : queue9) {
                    if (d.getHigh() > high) {
                        high = d.getHigh();
                    }
                    if (d.getLow() < low) {
                        low = d.getLow();
                    }
                    close = d.getClose();
                }

                double rsvValue = getRSV(close, low, high);
                if (preKValue == 0) {
                    preKValue = 50;
                }
                if (preDValue == 0) {
                    preDValue = 50;
                }
                double kValue = ((double) 2 / 3) * preKValue + ((double) 1 / 3) * rsvValue;
                double dValue = ((double) 2 / 3) * preDValue + ((double) 1 / 3) * kValue;
                double jValue = 3 * kValue - 2 * dValue;
                stockMonthInfo.setK(kValue);
                stockMonthInfo.setD(dValue);
                stockMonthInfo.setJ(jValue);
                preKValue = kValue;
                preDValue = dValue;
                queue9.poll();
            }

            newstockMonthinfoList.add(stockMonthInfo);
        }
        return newstockMonthinfoList;
    }
}
