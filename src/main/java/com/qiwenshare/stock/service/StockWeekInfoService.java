package com.qiwenshare.stock.service;

import com.qiwenshare.common.util.DateUtil;
import com.qiwenshare.stock.api.IStockWeekInfoService;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;
import com.qiwenshare.stock.indicator.proxy.IndicatorProxy;
import com.qiwenshare.stock.mapper.StockMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Service
public class StockWeekInfoService implements IStockWeekInfoService {

    @Resource
    StockMapper stockMapper;


    public boolean notBeginDay(int dayOfWeek, int preDayOfWeek) {
        if (dayOfWeek != 1 && preDayOfWeek == 0) {
            return true;
        }
        return false;
    }

    public boolean notEndDay(int dayOfWeek, int preDayOfWeek) {
        if (dayOfWeek == 1 && preDayOfWeek != 0) {
            return true;
        }
        return false;
    }

    public boolean notBeginAndEndDay(int dayOfWeek, int preDayOfWeek) {
        if (dayOfWeek != 1
                && preDayOfWeek != 0
                && (dayOfWeek - preDayOfWeek != 1)) {
            return true;
        }
        return false;
    }

    /**
     * 一周的开始
     *
     * @param dayOfWeek
     * @param preDayOfWeek
     * @return
     */
    public boolean beginOfWeek(int dayOfWeek, int preDayOfWeek) {
        if (dayOfWeek == 1
                || notBeginDay(dayOfWeek, preDayOfWeek)
                || notEndDay(dayOfWeek, preDayOfWeek)
                || notBeginAndEndDay(dayOfWeek, preDayOfWeek)) {
            return true;
        }
        return false;
    }

    @Override
    public List<StockWeekInfo> getStockweekbar(String stockNum) {
        StockBean stockBean = new StockBean();
        stockBean.setStockNum(stockNum);
        stockBean.setStockTableName("stockweekinfo_" + stockNum);
        List<StockWeekInfo> result = selectStockweekListByStockNum(stockBean);
        return result;
    }

    @Override
    public List<StockWeekInfo> selectStockweekListByStockNum(StockBean stockBean) {
        return stockMapper.selectStockweekListByStockNum(stockBean);


    }


    /**
     * 获取周线信息
     *
     * @param stockDayInfoList
     * @return
     */
    @Override
    public List<StockWeekInfo> getStockWeekInfoList(List<StockDayInfo> stockDayInfoList) {
        double weekOpen = 0;
        double weekClose = 0;
        double weekHigh = 0;
        double weekLow = 10000;
        double weekVolume = 0;
        double weekAmount = 0;
        double weekPreClose = 0;
        String weekDate = new String();
        int preDayOfWeek = 0;
        List<StockWeekInfo> stockWeekInfoList = new ArrayList<>();
        for (int i = 0; i < stockDayInfoList.size(); i++) {
            String stringDate = stockDayInfoList.get(i).getDate();
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
                cal.setTime(DateUtil.getDateByFormatString(stringDate, "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == 1) {
                dayOfWeek = 7;
            } else {
                dayOfWeek = dayOfWeek - 1;
            }
            if (beginOfWeek(dayOfWeek, preDayOfWeek)) {
                //先处理上周的数据
                if (weekOpen != 0) {
                    StockWeekInfo stockWeekInfo = new StockWeekInfo();
                    stockWeekInfo.setOpen(weekOpen);
                    stockWeekInfo.setClose(weekClose);
                    stockWeekInfo.setHigh(weekHigh);
                    stockWeekInfo.setLow(weekLow);
                    stockWeekInfo.setAmount(weekAmount);
                    stockWeekInfo.setVolume(weekVolume);
                    stockWeekInfo.setPreClose(weekPreClose);
                    stockWeekInfo.setDate(weekDate);
                    stockWeekInfoList.add(stockWeekInfo);
                    weekOpen = 0;
                    weekClose = 0;
                    weekHigh = 0;
                    weekLow = 10000;
                    weekVolume = 0;
                    weekAmount = 0;
                    weekPreClose = 0;
                }
                weekOpen = open;
            }
            if (weekHigh < high) {
                weekHigh = high;
            }
            if (weekLow > low) {
                weekLow = low;
            }
            weekVolume += volume;
            weekAmount += amount;
            weekPreClose += preClose;
            weekClose = close;
            weekDate = date;


            if (dayOfWeek == 5) {
                preDayOfWeek = 0;
            } else {
                preDayOfWeek = dayOfWeek;
            }


        }

        StockWeekInfo stockWeekInfo = new StockWeekInfo();
        stockWeekInfo.setOpen(weekOpen);
        stockWeekInfo.setClose(weekClose);
        stockWeekInfo.setHigh(weekHigh);
        stockWeekInfo.setLow(weekLow);
        stockWeekInfo.setAmount(weekAmount);
        stockWeekInfo.setVolume(weekVolume);
        stockWeekInfo.setPreClose(weekPreClose);
        stockWeekInfo.setDate(weekDate);
        stockWeekInfoList.add(stockWeekInfo);
        stockWeekInfoList = new IndicatorProxy().getWeekIndicatorList(stockWeekInfoList);
        return stockWeekInfoList;
    }

    @Override
    public void insertStockWeekInfo(String stockNum, List<StockWeekInfo> stockweekinfo) {
        Map<String, Object> stockWeekInfoMap = new HashMap<String, Object>();
        stockWeekInfoMap.put("stockWeekInfoTable", "stockweekinfo_" + stockNum);

        List<StockWeekInfo> tempStockWeekInfoList = stockweekinfo;
        while (tempStockWeekInfoList.size() > 300) {
            List<StockWeekInfo> insertStockWeekInfoList = tempStockWeekInfoList.subList(0, 300);
            stockWeekInfoMap.put("stockweekinfo", insertStockWeekInfoList);
            stockMapper.insertStockWeekInfo(stockWeekInfoMap);
            tempStockWeekInfoList = tempStockWeekInfoList.subList(300, tempStockWeekInfoList.size());
        }
        stockWeekInfoMap.put("stockweekinfo", tempStockWeekInfoList);//.subList(stockDayInfoSize - 5, stockDayInfoSize));
        stockMapper.insertStockWeekInfo(stockWeekInfoMap);
    }
}
