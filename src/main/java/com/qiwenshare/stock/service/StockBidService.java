package com.qiwenshare.stock.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qiwenshare.stock.api.IStockBidService;
import com.qiwenshare.stock.common.ProxyHttpRequest;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockBidBean;
import com.qiwenshare.stock.domain.StockBidObj;
import com.qiwenshare.stock.mapper.StockBidMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StockBidService implements IStockBidService {

    @Resource
    StockBidMapper stockBidMapper;


    @Override
    public void updateStockBid(StockBidBean stockBidBean) {
        LambdaUpdateWrapper<StockBidBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(StockBidBean::getStockNum, stockBidBean.getStockNum());
        stockBidMapper.update(stockBidBean, lambdaUpdateWrapper);

    }

    @Override
    public StockBidBean getStockBidBean(String stockNum) {
        LambdaQueryWrapper<StockBidBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StockBidBean::getStockNum, stockNum);
        List<StockBidBean> stockBeanList = stockBidMapper.selectList(lambdaQueryWrapper);
        return stockBeanList.get(0);
    }

    @Override
    public void insertStockBid(StockBidBean stockBidBean) {
        stockBidMapper.insert(stockBidBean);
    }

    @Override
    public StockBidBean getBidByStockBean(StockBean stockBean) {
        String url = "http://yunhq.sse.com.cn:32041/v1/sh1/snap/" + stockBean.getStockNum();
        //String param = "select=ask,bid";
        Map<String, String> param = new HashMap<String, String>();
        param.put("select", "ask,bid");
        String stockBidJson = new ProxyHttpRequest().sendGet(url, param);

        if (StringUtils.isEmpty(stockBidJson)) {
            log.info("网络不可用:股票编号{}, stockBidJson{}", stockBean.getStockNum(),stockBidJson);
            return null;
        }
        StockBidObj stockBidObj = null;
        try {
            stockBidObj = JSON.parseObject(stockBidJson, StockBidObj.class);
        } catch (Exception e) {
            log.error("stockBidJson解析失败：stockNum:{}, stockBidJson:{}", stockBean.getStockNum(), stockBidJson);
        }
        if (stockBidObj == null) {
            log.info("网络不可用:股票编号{}, stockBidJson{}", stockBean.getStockNum(),stockBidJson);
            return null;
        }
        List<String> stockBidElemList = JSON.parseArray(stockBidObj.getSnap(), String.class);
        List<String> stockSellBid = JSON.parseArray(stockBidElemList.get(0), String.class);
        List<String> stockBoughtBid = JSON.parseArray(stockBidElemList.get(1), String.class);

        StockBidBean stockBidBean = new StockBidBean();
        stockBidBean.setStockNum(stockBean.getStockNum());
//        stockBidBean.setStockId(stockBean.getStockId());
        stockBidBean.setSellPrice1(Double.parseDouble(stockSellBid.get(0)));
        stockBidBean.setSellCount1(Integer.parseInt(stockSellBid.get(1)));
        stockBidBean.setSellPrice2(Double.parseDouble(stockSellBid.get(2)));
        stockBidBean.setSellCount2(Integer.parseInt(stockSellBid.get(3)));
        stockBidBean.setSellPrice3(Double.parseDouble(stockSellBid.get(4)));
        stockBidBean.setSellCount3(Integer.parseInt(stockSellBid.get(5)));
        stockBidBean.setSellPrice4(Double.parseDouble(stockSellBid.get(6)));
        stockBidBean.setSellCount4(Integer.parseInt(stockSellBid.get(7)));
        stockBidBean.setSellPrice5(Double.parseDouble(stockSellBid.get(8)));
        stockBidBean.setSellCount5(Integer.parseInt(stockSellBid.get(9)));

        stockBidBean.setBoughtPrice1(Double.parseDouble(stockBoughtBid.get(0)));
        stockBidBean.setBoughtCount1(Integer.parseInt(stockBoughtBid.get(1)));
        stockBidBean.setBoughtPrice2(Double.parseDouble(stockBoughtBid.get(2)));
        stockBidBean.setBoughtCount2(Integer.parseInt(stockBoughtBid.get(3)));
        stockBidBean.setBoughtPrice3(Double.parseDouble(stockBoughtBid.get(4)));
        stockBidBean.setBoughtCount3(Integer.parseInt(stockBoughtBid.get(5)));
        stockBidBean.setBoughtPrice4(Double.parseDouble(stockBoughtBid.get(6)));
        stockBidBean.setBoughtCount4(Integer.parseInt(stockBoughtBid.get(7)));
        stockBidBean.setBoughtPrice5(Double.parseDouble(stockBoughtBid.get(8)));
        stockBidBean.setBoughtCount5(Integer.parseInt(stockBoughtBid.get(9)));
        return stockBidBean;
    }
}
