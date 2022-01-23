package com.qiwenshare.stock.indicator.factory;

import com.qiwenshare.stock.indicator.product.Indicator;
import com.qiwenshare.stock.indicator.product.KDJ;

public class KDJIndicatorFactory implements IndicatorFactory {

    @Override
    public Indicator getIndicator() {
        return new KDJ();
    }
}
