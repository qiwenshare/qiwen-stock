package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockBidBean;

public interface IStockBidService {
    void insertStockBid(StockBidBean stockBidBean);


    StockBidBean getStockBidBean(String stockNum);


    StockBidBean getBidByStockBean(StockBean stockBean);

    void updateStockBid(StockBidBean stockBidBean);
}
