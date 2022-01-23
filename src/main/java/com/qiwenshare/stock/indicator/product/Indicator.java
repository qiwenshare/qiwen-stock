package com.qiwenshare.stock.indicator.product;

import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;

import java.util.List;

public interface Indicator {

    public List<StockDayInfo> getDayIndicatorList(List<StockDayInfo> StockdayinfoList);

    public List<StockWeekInfo> getWeekIndicatorList(List<StockWeekInfo> stockweekinfoList);

    public List<StockMonthInfo> getMonthIndicatorList(List<StockMonthInfo> stockmonthinfoList);

}
