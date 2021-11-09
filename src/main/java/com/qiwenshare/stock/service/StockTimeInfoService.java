package com.qiwenshare.stock.service;

import com.alibaba.fastjson.JSON;
import com.qiwenshare.common.util.HttpsUtils;
import com.qiwenshare.stock.api.IStockTimeInfoService;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockTimeInfo;
import com.qiwenshare.stock.domain.StockTimeLineObj;
import com.qiwenshare.stock.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StockTimeInfoService implements IStockTimeInfoService {

    @Resource
    StockMapper stockMapper;

    @Override
    public List<StockTimeInfo> getStocktimebar(String stockNum) {
        StockTimeInfo stockTimeInfo = new StockTimeInfo();
        stockTimeInfo.setStockTableName("stocktimeinfo_" + stockNum);
        List<StockTimeInfo> result = selectStocktimeListByStockNum(stockTimeInfo);
        return result;
    }

    @Override
    public List<StockTimeInfo> selectStocktimeListByStockNum(StockTimeInfo stockTimeInfo) {
        return stockMapper.selectStocktimeListByStockNum(stockTimeInfo);
    }

    /**
     * 获取分时数据
     *
     * @param stockBean
     * @return
     */
    @Override
    public List<StockTimeInfo> getStockTimeInfoListByStockBean(StockBean stockBean) {
        String url = "http://yunhq.sse.com.cn:32041/v1/sh1/line/" + stockBean.getStockNum();
        //String param = "begin=0&end=-1&select=time,price,volume";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("begin", "0");
        param.put("end", "-1");
        param.put("select", "time,price,volume");
        String stockTimeLineJson = "";
        try {
            stockTimeLineJson = IOUtils.toString(HttpsUtils.doGet(url, param), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(stockTimeLineJson)) {
            log.error("stockTimeLineJson爬取数据为空：stockNum:" + stockBean.getStockNum());
            return null;
        }
        StockTimeLineObj stockTimeLineObj = null;
        try {
            stockTimeLineObj = JSON.parseObject(stockTimeLineJson, StockTimeLineObj.class);
        } catch (Exception e) {
            log.error("解析分时数据报错：stockTileLineJson: {}", stockTimeLineJson);
        }

        if (stockTimeLineObj == null) {
            log.error("stockTimeLineJson解析数据为空:stockNum:" + stockBean.getStockNum());
            return null;
        }
        List<String> stockTimeLineList = JSON.parseArray(stockTimeLineObj.getLine(), String.class);
        String date = stockTimeLineObj.getDate();
        double sumPrice = 0;
        double prevClose = Double.parseDouble(stockTimeLineObj.getPrev_close());
        List<StockTimeInfo> stockTimeInfoList = new ArrayList<StockTimeInfo>();
        for (int i = 0; i < stockTimeLineList.size(); i++) {
            StockTimeInfo stockTimeInfo = new StockTimeInfo();
            List<String> stockParseTimeline = JSON.parseArray(stockTimeLineList.get(i), String.class);
            double price = Double.parseDouble(stockParseTimeline.get(1));
            sumPrice += price;
            stockTimeInfo.setTime(stockParseTimeline.get(0));
            stockTimeInfo.setDate(date);
            stockTimeInfo.setUpDownRange((price - prevClose) / prevClose);
            stockTimeInfo.setPrice(price);
            stockTimeInfo.setAvgPrice(sumPrice / (i + 1));
            stockTimeInfo.setVolume(Double.parseDouble(stockParseTimeline.get(2)));
            stockTimeInfoList.add(stockTimeInfo);
        }

        return stockTimeInfoList;
    }

    @Override
    public void insertStockTimeInfo(String stockTimeInfoTable, List<StockTimeInfo> stocktimeinfo) {

        Map<String, Object> stockDayInfoMap = new HashMap<String, Object>();
        stockDayInfoMap.put("stockTimeInfoTable", stockTimeInfoTable);
        stockDayInfoMap.put("stocktimeinfo", stocktimeinfo);
        stockMapper.insertStockTimeInfo(stockDayInfoMap);
    }
}
