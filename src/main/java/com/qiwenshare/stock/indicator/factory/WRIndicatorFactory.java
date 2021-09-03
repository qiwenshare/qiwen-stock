package com.qiwenshare.stock.indicator.factory;

import com.qiwenshare.stock.indicator.product.Indicator;
import com.qiwenshare.stock.indicator.product.WR;

public class WRIndicatorFactory implements IndicatorFactory {
    @Override
    public Indicator getIndicator() {
        return new WR();
    }
}
