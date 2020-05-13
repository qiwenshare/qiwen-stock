package com.qiwenshare.stock.indicator.factory;

import com.qiwenshare.stock.indicator.product.Indicator;

public interface IndicatorFactory {
    /**
     * 工厂方法
     *
     * @return
     */
    public Indicator getIndicator();
}
