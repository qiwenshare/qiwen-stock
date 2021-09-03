package com.qiwenshare.stock.analysis.pressureandsupport;

import com.qiwenshare.stock.analysis.TechnologyAnalysis;
import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;

import java.util.List;

public class MaPressureAndSupportPositions extends TechnologyAnalysis implements PressureAndSupportPositions {
    @Override
    public double pressurePositions(StockDayInfo preStockDayInfo, StockDayInfo stockDayInfo) {
        return 0;
    }

    @Override
    public double supportPositions(StockDayInfo preStockDayInfo, StockDayInfo stockDayInfo) {
        return 0;
    }

    @Override
    public ReplayBean getOperation(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        return null;
    }
}
