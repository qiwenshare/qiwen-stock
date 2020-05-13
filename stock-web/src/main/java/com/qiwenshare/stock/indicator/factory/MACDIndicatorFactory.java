package com.qiwenshare.stock.indicator.factory;

import com.qiwenshare.stock.indicator.product.Indicator;
import com.qiwenshare.stock.indicator.product.MACD;

public class MACDIndicatorFactory implements IndicatorFactory {
    @Override
    public Indicator getIndicator() {
        return new MACD();
    }
}
