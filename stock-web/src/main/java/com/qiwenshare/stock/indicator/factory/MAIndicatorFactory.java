package com.qiwenshare.stock.indicator.factory;

import com.qiwenshare.stock.indicator.product.Indicator;
import com.qiwenshare.stock.indicator.product.MA;

public class MAIndicatorFactory implements IndicatorFactory {
    @Override
    public Indicator getIndicator() {
        return new MA();
    }
}
