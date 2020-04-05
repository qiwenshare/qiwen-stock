package com.qiwenshare.stock.analysis.indicator;

import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.List;

public class RSIAnalysis extends IndicatorAnalysis {
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
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        return null;
    }
}
