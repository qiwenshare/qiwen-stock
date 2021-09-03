package com.qiwenshare.stock.indicator.product;

import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;

import java.util.ArrayList;
import java.util.List;

public class RSI implements Indicator {
    public static final int VERY_WEAK = 1;
    public static final int WEAK = 2;
    public static final int STRONG = 3;
    public static final int VERY_STRONG = 4;


    public int prosperityLevel(List<StockDayInfo> stockdayinfoList) {
        double rsi24 = stockdayinfoList.get(stockdayinfoList.size() - 1).getRsi24();
        if (rsi24 < 20) {
            return VERY_WEAK;
        } else if (rsi24 >= 20 && rsi24 < 50) {
            return WEAK;
        } else if (rsi24 >= 50 && rsi24 < 80) {
            return STRONG;
        } else {
            return VERY_STRONG;
        }
    }

    @Override
    public List<StockDayInfo> getDayIndicatorList(List<StockDayInfo> stockdayinfoList) {
        int stockDayInfoListSize = stockdayinfoList.size();

        double preUpAverage6 = 0;
        double preDownAverage6 = 0;
        double preUpAverage12 = 0;
        double preDownAverage12 = 0;
        double preUpAverage24 = 0;
        double preDownAverage24 = 0;
        List<StockDayInfo> newStockDayInfoList = new ArrayList<StockDayInfo>();
        for (int i = 0; i < stockDayInfoListSize; i++) {
            StockDayInfo stockDayInfo = stockdayinfoList.get(i);
            if (i == 0) {
                newStockDayInfoList.add(stockDayInfo);
                continue;
            }

            double currentClose = stockdayinfoList.get(i).getClose();
            double preClose = stockdayinfoList.get(i - 1).getClose();
            double currentUpDownPrice = currentClose - preClose;

            double currentUpAverage6 = 0;
            double currentUpAverage12 = 0;
            double currentUpAverage24 = 0;
            double currentDownAverage6 = 0;
            double currentDownAverage12 = 0;
            double currentDownAverage24 = 0;

            if (currentUpDownPrice > 0) {
                currentUpAverage6 = preUpAverage6 * 5 / 6 + currentUpDownPrice / 6;
                currentUpAverage12 = preUpAverage12 * 11 / 12 + currentUpDownPrice / 12;
                currentUpAverage24 = preUpAverage24 * 23 / 24 + currentUpDownPrice / 24;

                currentDownAverage6 = preDownAverage6 * 5 / 6 + 0;
                currentDownAverage12 = preDownAverage12 * 11 / 12 + 0;
                currentDownAverage24 = preDownAverage24 * 23 / 24 + 0;
            } else {
                currentUpAverage6 = preUpAverage6 * 5 / 6 + 0;
                currentUpAverage12 = preUpAverage12 * 11 / 12 + 0;
                currentUpAverage24 = preUpAverage24 * 23 / 24 + 0;

                currentDownAverage6 = preDownAverage6 * 5 / 6 + currentUpDownPrice / 6 * (-1);
                currentDownAverage12 = preDownAverage12 * 11 / 12 + currentUpDownPrice / 12 * (-1);
                currentDownAverage24 = preDownAverage24 * 23 / 24 + currentUpDownPrice / 24 * (-1);
            }

            double rsi6 = 0;
            double rsi12 = 0;
            double rsi24 = 0;
            if (currentDownAverage6 == 0) {
                rsi6 = 100;
            } else {
                double rs6 = currentUpAverage6 / currentDownAverage6;
                rsi6 = rs6 / (1 + rs6) * 100;
            }

            if (currentDownAverage12 == 0) {
                rsi12 = 100;
            } else {
                double rs12 = currentUpAverage12 / currentDownAverage12;
                rsi12 = rs12 / (1 + rs12) * 100;
            }

            if (currentDownAverage24 == 0) {
                rsi24 = 100;
            } else {
                double rs24 = currentUpAverage24 / currentDownAverage24;
                rsi24 = rs24 / (1 + rs24) * 100;
            }

            stockDayInfo.setRsi6(rsi6);
            stockDayInfo.setRsi12(rsi12);
            stockDayInfo.setRsi24(rsi24);

            newStockDayInfoList.add(stockDayInfo);

            preUpAverage6 = currentUpAverage6;
            preDownAverage6 = currentDownAverage6;
            preUpAverage12 = currentUpAverage12;
            preDownAverage12 = currentDownAverage12;
            preUpAverage24 = currentUpAverage24;
            preDownAverage24 = currentDownAverage24;
        }
        return newStockDayInfoList;
    }

    @Override
    public List<StockWeekInfo> getWeekIndicatorList(List<StockWeekInfo> stockweekinfoList) {
        int stockWeekInfoListSize = stockweekinfoList.size();

        double preUpAverage6 = 0;
        double preDownAverage6 = 0;
        double preUpAverage12 = 0;
        double preDownAverage12 = 0;
        double preUpAverage24 = 0;
        double preDownAverage24 = 0;
        List<StockWeekInfo> newStockWeekInfoList = new ArrayList<StockWeekInfo>();
        for (int i = 0; i < stockWeekInfoListSize; i++) {
            StockWeekInfo stockWeekInfo = stockweekinfoList.get(i);
            if (i == 0) {
                newStockWeekInfoList.add(stockWeekInfo);
                continue;
            }

            double currentClose = stockweekinfoList.get(i).getClose();
            double preClose = stockweekinfoList.get(i - 1).getClose();
            double currentUpDownPrice = currentClose - preClose;

            double currentUpAverage6 = 0;
            double currentUpAverage12 = 0;
            double currentUpAverage24 = 0;
            double currentDownAverage6 = 0;
            double currentDownAverage12 = 0;
            double currentDownAverage24 = 0;

            if (currentUpDownPrice > 0) {
                currentUpAverage6 = preUpAverage6 * 5 / 6 + currentUpDownPrice / 6;
                currentUpAverage12 = preUpAverage12 * 11 / 12 + currentUpDownPrice / 12;
                currentUpAverage24 = preUpAverage24 * 23 / 24 + currentUpDownPrice / 24;

                currentDownAverage6 = preDownAverage6 * 5 / 6 + 0;
                currentDownAverage12 = preDownAverage12 * 11 / 12 + 0;
                currentDownAverage24 = preDownAverage24 * 23 / 24 + 0;
            } else {
                currentUpAverage6 = preUpAverage6 * 5 / 6 + 0;
                currentUpAverage12 = preUpAverage12 * 11 / 12 + 0;
                currentUpAverage24 = preUpAverage24 * 23 / 24 + 0;

                currentDownAverage6 = preDownAverage6 * 5 / 6 + currentUpDownPrice / 6 * (-1);
                currentDownAverage12 = preDownAverage12 * 11 / 12 + currentUpDownPrice / 12 * (-1);
                currentDownAverage24 = preDownAverage24 * 23 / 24 + currentUpDownPrice / 24 * (-1);
            }

            double rsi6 = 0;
            double rsi12 = 0;
            double rsi24 = 0;
            if (currentDownAverage6 == 0) {
                rsi6 = 100;
            } else {
                double rs6 = currentUpAverage6 / currentDownAverage6;
                rsi6 = rs6 / (1 + rs6) * 100;
            }

            if (currentDownAverage12 == 0) {
                rsi12 = 100;
            } else {
                double rs12 = currentUpAverage12 / currentDownAverage12;
                rsi12 = rs12 / (1 + rs12) * 100;
            }

            if (currentDownAverage24 == 0) {
                rsi24 = 100;
            } else {
                double rs24 = currentUpAverage24 / currentDownAverage24;
                rsi24 = rs24 / (1 + rs24) * 100;
            }

            stockWeekInfo.setRsi6(rsi6);
            stockWeekInfo.setRsi12(rsi12);
            stockWeekInfo.setRsi24(rsi24);

            newStockWeekInfoList.add(stockWeekInfo);

            preUpAverage6 = currentUpAverage6;
            preDownAverage6 = currentDownAverage6;
            preUpAverage12 = currentUpAverage12;
            preDownAverage12 = currentDownAverage12;
            preUpAverage24 = currentUpAverage24;
            preDownAverage24 = currentDownAverage24;
        }
        return newStockWeekInfoList;
    }

    @Override
    public List<StockMonthInfo> getMonthIndicatorList(List<StockMonthInfo> stockmonthinfoList) {
        int stockMonthInfoListSize = stockmonthinfoList.size();

        double preUpAverage6 = 0;
        double preDownAverage6 = 0;
        double preUpAverage12 = 0;
        double preDownAverage12 = 0;
        double preUpAverage24 = 0;
        double preDownAverage24 = 0;
        List<StockMonthInfo> newStockMonthInfoList = new ArrayList<StockMonthInfo>();
        for (int i = 0; i < stockMonthInfoListSize; i++) {
            StockMonthInfo stockMonthInfo = stockmonthinfoList.get(i);
            if (i == 0) {
                newStockMonthInfoList.add(stockMonthInfo);
                continue;
            }

            double currentClose = stockmonthinfoList.get(i).getClose();
            double preClose = stockmonthinfoList.get(i - 1).getClose();
            double currentUpDownPrice = currentClose - preClose;

            double currentUpAverage6 = 0;
            double currentUpAverage12 = 0;
            double currentUpAverage24 = 0;
            double currentDownAverage6 = 0;
            double currentDownAverage12 = 0;
            double currentDownAverage24 = 0;

            if (currentUpDownPrice > 0) {
                currentUpAverage6 = preUpAverage6 * 5 / 6 + currentUpDownPrice / 6;
                currentUpAverage12 = preUpAverage12 * 11 / 12 + currentUpDownPrice / 12;
                currentUpAverage24 = preUpAverage24 * 23 / 24 + currentUpDownPrice / 24;

                currentDownAverage6 = preDownAverage6 * 5 / 6 + 0;
                currentDownAverage12 = preDownAverage12 * 11 / 12 + 0;
                currentDownAverage24 = preDownAverage24 * 23 / 24 + 0;
            } else {
                currentUpAverage6 = preUpAverage6 * 5 / 6 + 0;
                currentUpAverage12 = preUpAverage12 * 11 / 12 + 0;
                currentUpAverage24 = preUpAverage24 * 23 / 24 + 0;

                currentDownAverage6 = preDownAverage6 * 5 / 6 + currentUpDownPrice / 6 * (-1);
                currentDownAverage12 = preDownAverage12 * 11 / 12 + currentUpDownPrice / 12 * (-1);
                currentDownAverage24 = preDownAverage24 * 23 / 24 + currentUpDownPrice / 24 * (-1);
            }

            double rsi6 = 0;
            double rsi12 = 0;
            double rsi24 = 0;
            if (currentDownAverage6 == 0) {
                rsi6 = 100;
            } else {
                double rs6 = currentUpAverage6 / currentDownAverage6;
                rsi6 = rs6 / (1 + rs6) * 100;
            }

            if (currentDownAverage12 == 0) {
                rsi12 = 100;
            } else {
                double rs12 = currentUpAverage12 / currentDownAverage12;
                rsi12 = rs12 / (1 + rs12) * 100;
            }

            if (currentDownAverage24 == 0) {
                rsi24 = 100;
            } else {
                double rs24 = currentUpAverage24 / currentDownAverage24;
                rsi24 = rs24 / (1 + rs24) * 100;
            }

            stockMonthInfo.setRsi6(rsi6);
            stockMonthInfo.setRsi12(rsi12);
            stockMonthInfo.setRsi24(rsi24);

            newStockMonthInfoList.add(stockMonthInfo);

            preUpAverage6 = currentUpAverage6;
            preDownAverage6 = currentDownAverage6;
            preUpAverage12 = currentUpAverage12;
            preDownAverage12 = currentDownAverage12;
            preUpAverage24 = currentUpAverage24;
            preDownAverage24 = currentDownAverage24;
        }
        return newStockMonthInfoList;
    }
}
