package com.qiwenshare.stock.service;


import com.qiwenshare.common.util.DateUtil;
import com.qiwenshare.stock.api.IStockMonthInfoService;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;
import com.qiwenshare.stock.indicator.proxy.IndicatorProxy;
import com.qiwenshare.stock.mapper.StockMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Service
public class StockMonthInfoService implements IStockMonthInfoService {
    @Resource
    StockMapper stockMapper;


    public boolean notBeginDay(int dayOfMonth, int preDayOfMonth) {
        if (dayOfMonth != 1 && preDayOfMonth == 0) {
            return true;
        }
        return false;
    }

    public boolean notEndDay(int dayOfMonth, int preDayOfMonth) {
        if (dayOfMonth == 1 && preDayOfMonth != 0) {
            return true;
        }
        return false;
    }

    public boolean notBeginAndEndDay(int dayOfMonth, int preDayOfMonth) {
        if (dayOfMonth != 1
                && preDayOfMonth != 0
                && (preDayOfMonth - dayOfMonth > 20)) {
            return true;
        }
        return false;
    }

    /**
     * 一月的开始
     *
     * @param dayOfMonth
     * @param preDayOfMonth
     * @return
     */
    public boolean beginOfMonth(int dayOfMonth, int preDayOfMonth) {
        if (dayOfMonth == 1
                || notBeginDay(dayOfMonth, preDayOfMonth)
                || notEndDay(dayOfMonth, preDayOfMonth)
                || notBeginAndEndDay(dayOfMonth, preDayOfMonth)) {
            return true;
        }
        return false;
    }

    @Override
    public List<StockMonthInfo> getStockmonthbar(String stockNum) {
        StockBean stockBean = new StockBean();
        stockBean.setStockNum(stockNum);
        stockBean.setStockTableName("stockmonthinfo_" + stockNum);
        List<StockMonthInfo> result = selectStockmonthListByStockNum(stockBean);
        return result;
    }

    @Override
    public List<StockMonthInfo> selectStockmonthListByStockNum(StockBean stockBean) {
        return stockMapper.selectStockmonthListByStockNum(stockBean);


    }

    /**
     * 获取月线信息
     *
     * @param stockDayInfoList
     * @return
     */
    @Override
    public List<StockMonthInfo> getStockMonthInfoList(List<StockDayInfo> stockDayInfoList) {
        double monthOpen = 0;
        double monthClose = 0;
        double monthHigh = 0;
        double monthLow = 10000;
        double monthVolume = 0;
        double monthAmount = 0;
        double monthPreClose = 0;
        String monthDate = "";
        int preDayOfMonth = 0;
        List<StockMonthInfo> stockMonthInfoList = new ArrayList<>();
        for (int i = 0; i < stockDayInfoList.size(); i++) {
            double open = stockDayInfoList.get(i).getOpen();
            double close = stockDayInfoList.get(i).getClose();
            double high = stockDayInfoList.get(i).getHigh();
            double low = stockDayInfoList.get(i).getLow();
            double volume = stockDayInfoList.get(i).getVolume();
            double amount = stockDayInfoList.get(i).getAmount();
            double preClose = stockDayInfoList.get(i).getPreClose();
            String date = stockDayInfoList.get(i).getDate();

            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(DateUtil.getDateByFormatString(date, "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (beginOfMonth(dayOfMonth, preDayOfMonth)) {
                //先处理上周的数据
                if (monthOpen != 0) {
                    StockMonthInfo stockMonthInfo = new StockMonthInfo();
                    stockMonthInfo.setOpen(monthOpen);
                    stockMonthInfo.setClose(monthClose);
                    stockMonthInfo.setHigh(monthHigh);
                    stockMonthInfo.setLow(monthLow);
                    stockMonthInfo.setAmount(monthAmount);
                    stockMonthInfo.setVolume(monthVolume);
                    stockMonthInfo.setPreClose(monthPreClose);
                    stockMonthInfo.setDate(monthDate);
                    stockMonthInfoList.add(stockMonthInfo);
                    monthOpen = 0;
                    monthClose = 0;
                    monthHigh = 0;
                    monthLow = 10000;
                    monthVolume = 0;
                    monthAmount = 0;
                    monthPreClose = 0;
                }
                monthOpen = open;
            }
            if (monthHigh < high) {
                monthHigh = high;
            }
            if (monthLow > low) {
                monthLow = low;
            }
            monthVolume += volume;
            monthAmount += amount;
            monthPreClose += preClose;
            monthClose = close;
            monthDate = date;


            if (dayOfMonth == lastDay) {
                preDayOfMonth = 0;
            } else {
                preDayOfMonth = dayOfMonth;
            }


        }

        StockMonthInfo stockMonthInfo = new StockMonthInfo();
        stockMonthInfo.setOpen(monthOpen);
        stockMonthInfo.setClose(monthClose);
        stockMonthInfo.setHigh(monthHigh);
        stockMonthInfo.setLow(monthLow);
        stockMonthInfo.setAmount(monthAmount);
        stockMonthInfo.setVolume(monthVolume);
        stockMonthInfo.setPreClose(monthPreClose);
        stockMonthInfo.setDate(monthDate);
        stockMonthInfoList.add(stockMonthInfo);
        stockMonthInfoList = new IndicatorProxy().getMonthIndicatorList(stockMonthInfoList);
        return stockMonthInfoList;
    }

    @Override
    public void insertStockMonthInfo(String stockNum, List<StockMonthInfo> stockmonthinfo) {
        Map<String, Object> stockMonthInfoMap = new HashMap<String, Object>();
        stockMonthInfoMap.put("stockMonthInfoTable", "stockmonthinfo_" + stockNum);
        stockMonthInfoMap.put("stockmonthinfo", stockmonthinfo);
        stockMapper.insertStockMonthInfo(stockMonthInfoMap);
    }
}
