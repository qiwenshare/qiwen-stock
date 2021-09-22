package com.qiwenshare.stock.service;

import com.qiwenshare.stock.api.IStockOptionalService;
import com.qiwenshare.stock.domain.StockOptionalBean;
import com.qiwenshare.stock.mapper.StockOptionalMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StockOptionalService implements IStockOptionalService {
    @Resource
    StockOptionalMapper stockOptionalMapper;

    @Override
    public void insertStockOptional(StockOptionalBean stockOptionalBean) {
        stockOptionalMapper.insert(stockOptionalBean);
    }
}
