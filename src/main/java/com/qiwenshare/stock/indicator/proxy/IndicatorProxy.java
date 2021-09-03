package com.qiwenshare.stock.indicator.proxy;

import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;
import com.qiwenshare.stock.indicator.factory.*;
import com.qiwenshare.stock.indicator.product.Indicator;

import java.util.List;

/**
 * 指标代理
 */
public class IndicatorProxy implements Indicator {

    /**
     * 用来获取一系列指标
     * 用户新增加一项指标，只需在该类中添加即可
     *
     * @param stockDayInfoList
     * @return
     */
    @Override
    public List<StockDayInfo> getDayIndicatorList(List<StockDayInfo> stockDayInfoList) {
        Indicator technicalIndicators = new WRIndicatorFactory().getIndicator();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new MACDIndicatorFactory().getIndicator();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new KDJIndicatorFactory().getIndicator();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new MAIndicatorFactory().getIndicator();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new RSIIndicatorFactory().getIndicator();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);
        return stockDayInfoList;
    }

    @Override
    public List<StockWeekInfo> getWeekIndicatorList(List<StockWeekInfo> stockweekinfoList) {
        Indicator technicalIndicators = new WRIndicatorFactory().getIndicator();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new MACDIndicatorFactory().getIndicator();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new KDJIndicatorFactory().getIndicator();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new MAIndicatorFactory().getIndicator();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new RSIIndicatorFactory().getIndicator();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);
        return stockweekinfoList;
    }

    @Override
    public List<StockMonthInfo> getMonthIndicatorList(List<StockMonthInfo> stockmonthinfoList) {
        Indicator technicalIndicators = new WRIndicatorFactory().getIndicator();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new MACDIndicatorFactory().getIndicator();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new KDJIndicatorFactory().getIndicator();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new MAIndicatorFactory().getIndicator();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new RSIIndicatorFactory().getIndicator();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);
        return stockmonthinfoList;
    }
}
