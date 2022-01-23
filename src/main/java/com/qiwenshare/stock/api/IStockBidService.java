package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.StockBidBean;

public interface IStockBidService {
    void insertStockBid(StockBidBean stockBidBean);


    StockBidBean getStockBidBean(String stockNum);


    StockBidBean crawlBidByStockBean(String stockNum);

    void updateStockBid(StockBidBean stockBidBean);
}
