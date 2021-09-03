package com.qiwenshare.stock.analysis.pressureandsupport;

import com.qiwenshare.stock.domain.StockDayInfo;

public interface PressureAndSupportPositions {
    double pressurePositions(StockDayInfo preStockDayInfo, StockDayInfo stockDayInfo);

    double supportPositions(StockDayInfo preStockDayInfo, StockDayInfo stockDayInfo);
}
