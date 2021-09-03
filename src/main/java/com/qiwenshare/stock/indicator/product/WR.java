package com.qiwenshare.stock.indicator.product;

import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WR implements Indicator {

    @Override
    public List<StockDayInfo> getDayIndicatorList(List<StockDayInfo> stockdayinfoList) {
        Queue<StockDayInfo> queue6 = new LinkedList<StockDayInfo>();

        Queue<StockDayInfo> queue10 = new LinkedList<StockDayInfo>();

        double higherPrice6 = 0;
        double lowerPrice6 = 0;
        double higherPrice10 = 0;
        double lowerPrice10 = 0;

        List<StockDayInfo> newstockDayinfoList = new ArrayList<StockDayInfo>();
        for (StockDayInfo stockdayinfo : stockdayinfoList) {
            higherPrice6 = 0;
            lowerPrice6 = 1000000;
            higherPrice10 = 0;
            lowerPrice10 = 1000000;

            queue6.offer(stockdayinfo);
            queue10.offer(stockdayinfo);


            if (queue6.size() == 6) {
                for (StockDayInfo sdi : queue6) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice6) {
                        higherPrice6 = currhigh;
                    }
                    if (currlow < lowerPrice6) {
                        lowerPrice6 = currlow;
                    }
                }
                queue6.poll();
            }
            if (queue10.size() == 10) {
                for (StockDayInfo sdi : queue10) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice10) {
                        higherPrice10 = currhigh;
                    }

                    if (currlow < lowerPrice10) {
                        lowerPrice10 = currlow;
                    }
                }
                queue10.poll();
            }
            double currentClosePrice = stockdayinfo.getClose();
            double wr10 = 100 * (higherPrice10 - currentClosePrice) / (higherPrice10 - lowerPrice10);
            double wr6 = 100 * (higherPrice6 - currentClosePrice) / (higherPrice6 - lowerPrice6);
            stockdayinfo.setWr6(wr6);
            stockdayinfo.setWr10(wr10);
            newstockDayinfoList.add(stockdayinfo);
        }
        return newstockDayinfoList;
    }

    @Override
    public List<StockWeekInfo> getWeekIndicatorList(List<StockWeekInfo> stockweekinfoList) {
        Queue<StockWeekInfo> queue6 = new LinkedList<StockWeekInfo>();

        Queue<StockWeekInfo> queue10 = new LinkedList<StockWeekInfo>();

        double higherPrice6 = 0;
        double lowerPrice6 = 0;
        double higherPrice10 = 0;
        double lowerPrice10 = 0;

        List<StockWeekInfo> newstockDayinfoList = new ArrayList<StockWeekInfo>();
        for (StockWeekInfo stockweekinfo : stockweekinfoList) {
            higherPrice6 = 0;
            lowerPrice6 = 1000000;
            higherPrice10 = 0;
            lowerPrice10 = 1000000;

            queue6.offer(stockweekinfo);
            queue10.offer(stockweekinfo);


            if (queue6.size() == 6) {
                for (StockWeekInfo sdi : queue6) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice6) {
                        higherPrice6 = currhigh;
                    }
                    if (currlow < lowerPrice6) {
                        lowerPrice6 = currlow;
                    }
                }
                queue6.poll();
            }
            if (queue10.size() == 10) {
                for (StockWeekInfo sdi : queue10) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice10) {
                        higherPrice10 = currhigh;
                    }

                    if (currlow < lowerPrice10) {
                        lowerPrice10 = currlow;
                    }
                }
                queue10.poll();
            }
            double currentClosePrice = stockweekinfo.getClose();
            double wr10 = 100 * (higherPrice10 - currentClosePrice) / (higherPrice10 - lowerPrice10);
            double wr6 = 100 * (higherPrice6 - currentClosePrice) / (higherPrice6 - lowerPrice6);
            stockweekinfo.setWr6(wr6);
            stockweekinfo.setWr10(wr10);
            newstockDayinfoList.add(stockweekinfo);
        }
        return newstockDayinfoList;
    }

    @Override
    public List<StockMonthInfo> getMonthIndicatorList(List<StockMonthInfo> stockmonthinfoList) {
        Queue<StockMonthInfo> queue6 = new LinkedList<StockMonthInfo>();

        Queue<StockMonthInfo> queue10 = new LinkedList<StockMonthInfo>();

        double higherPrice6 = 0;
        double lowerPrice6 = 0;
        double higherPrice10 = 0;
        double lowerPrice10 = 0;

        List<StockMonthInfo> newstockDayinfoList = new ArrayList<StockMonthInfo>();
        for (StockMonthInfo stockmonthinfo : stockmonthinfoList) {
            higherPrice6 = 0;
            lowerPrice6 = 1000000;
            higherPrice10 = 0;
            lowerPrice10 = 1000000;

            queue6.offer(stockmonthinfo);
            queue10.offer(stockmonthinfo);


            if (queue6.size() == 6) {
                for (StockMonthInfo sdi : queue6) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice6) {
                        higherPrice6 = currhigh;
                    }
                    if (currlow < lowerPrice6) {
                        lowerPrice6 = currlow;
                    }
                }
                queue6.poll();
            }
            if (queue10.size() == 10) {
                for (StockMonthInfo sdi : queue10) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice10) {
                        higherPrice10 = currhigh;
                    }

                    if (currlow < lowerPrice10) {
                        lowerPrice10 = currlow;
                    }
                }
                queue10.poll();
            }
            double currentClosePrice = stockmonthinfo.getClose();
            double wr10 = 100 * (higherPrice10 - currentClosePrice) / (higherPrice10 - lowerPrice10);
            double wr6 = 100 * (higherPrice6 - currentClosePrice) / (higherPrice6 - lowerPrice6);
            stockmonthinfo.setWr6(wr6);
            stockmonthinfo.setWr10(wr10);
            newstockDayinfoList.add(stockmonthinfo);
        }
        return newstockDayinfoList;
    }
}
