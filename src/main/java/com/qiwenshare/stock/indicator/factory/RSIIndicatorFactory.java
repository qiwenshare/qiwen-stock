package com.qiwenshare.stock.indicator.factory;

import com.qiwenshare.stock.indicator.product.Indicator;
import com.qiwenshare.stock.indicator.product.RSI;

public class RSIIndicatorFactory implements IndicatorFactory {
    @Override
    public Indicator getIndicator() {
        return new RSI();
    }
}
