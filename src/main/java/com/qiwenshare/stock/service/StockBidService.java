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
        lambdaUpdateWrapper.eq(StockBidBean::getStockid, stockBidBean.getStockid());
        stockBidMapper.update(stockBidBean, lambdaUpdateWrapper);

    }

    @Override
    public StockBidBean getStockBidBean(String stockNum) {
        LambdaQueryWrapper<StockBidBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StockBidBean::getStocknum, stockNum);
        List<StockBidBean> stockBeanList = stockBidMapper.selectList(lambdaQueryWrapper);
        return stockBeanList.get(0);
    }

    @Override
    public void insertStockBid(StockBidBean stockBidBean) {
        stockBidMapper.insert(stockBidBean);
    }

    @Override
    public StockBidBean getBidByStockBean(StockBean stockBean) {
        String url = "http://yunhq.sse.com.cn:32041/v1/sh1/snap/" + stockBean.getStocknum();
        //String param = "select=ask,bid";
        Map<String, String> param = new HashMap<String, String>();
        param.put("select", "ask,bid");
        String stockBidJson = new ProxyHttpRequest().sendGet(url, param);

        if (StringUtils.isEmpty(stockBidJson)) {
            log.info("网络不可用:股票编号{}, stockBidJson{}", stockBean.getStocknum(),stockBidJson);
            return null;
        }
        StockBidObj stockBidObj = null;
        try {
            stockBidObj = JSON.parseObject(stockBidJson, StockBidObj.class);
        } catch (Exception e) {
            log.error("stockBidJson解析失败：stockNum:{}, stockBidJson:{}", stockBean.getStocknum(), stockBidJson);
        }
        if (stockBidObj == null) {
            log.info("网络不可用:股票编号{}, stockBidJson{}", stockBean.getStocknum(),stockBidJson);
            return null;
        }
        List<String> stockBidElemList = JSON.parseArray(stockBidObj.getSnap(), String.class);
        List<String> stockSellBid = JSON.parseArray(stockBidElemList.get(0), String.class);
        List<String> stockBoughtBid = JSON.parseArray(stockBidElemList.get(1), String.class);

        StockBidBean stockBidBean = new StockBidBean();
        stockBidBean.setStocknum(stockBean.getStocknum());
        stockBidBean.setStockid(stockBean.getStockid());
        stockBidBean.setSellprice1(Double.parseDouble(stockSellBid.get(0)));
        stockBidBean.setSellcount1(Integer.parseInt(stockSellBid.get(1)));
        stockBidBean.setSellprice2(Double.parseDouble(stockSellBid.get(2)));
        stockBidBean.setSellcount2(Integer.parseInt(stockSellBid.get(3)));
        stockBidBean.setSellprice3(Double.parseDouble(stockSellBid.get(4)));
        stockBidBean.setSellcount3(Integer.parseInt(stockSellBid.get(5)));
        stockBidBean.setSellprice4(Double.parseDouble(stockSellBid.get(6)));
        stockBidBean.setSellcount4(Integer.parseInt(stockSellBid.get(7)));
        stockBidBean.setSellprice5(Double.parseDouble(stockSellBid.get(8)));
        stockBidBean.setSellcount5(Integer.parseInt(stockSellBid.get(9)));

        stockBidBean.setBoughtprice1(Double.parseDouble(stockBoughtBid.get(0)));
        stockBidBean.setBoughtcount1(Integer.parseInt(stockBoughtBid.get(1)));
        stockBidBean.setBoughtprice2(Double.parseDouble(stockBoughtBid.get(2)));
        stockBidBean.setBoughtcount2(Integer.parseInt(stockBoughtBid.get(3)));
        stockBidBean.setBoughtprice3(Double.parseDouble(stockBoughtBid.get(4)));
        stockBidBean.setBoughtcount3(Integer.parseInt(stockBoughtBid.get(5)));
        stockBidBean.setBoughtprice4(Double.parseDouble(stockBoughtBid.get(6)));
        stockBidBean.setBoughtcount4(Integer.parseInt(stockBoughtBid.get(7)));
        stockBidBean.setBoughtprice5(Double.parseDouble(stockBoughtBid.get(8)));
        stockBidBean.setBoughtcount5(Integer.parseInt(stockBoughtBid.get(9)));
        return stockBidBean;
    }
}
