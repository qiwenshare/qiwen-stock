package com.qiwenshare.stock.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.qiwenshare.common.cbb.DateUtil;
import com.qiwenshare.stock.api.IStockDayInfoService;
import com.qiwenshare.stock.constant.StockConstant;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockKLineObj;
import com.qiwenshare.stock.executor.StockDayInfoRunnable;
import com.qiwenshare.stock.mapper.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockDayInfoService implements IStockDayInfoService {
    private static final Logger logger = LoggerFactory.getLogger(StockDayInfoService.class);
    @Resource
    StockMapper stockMapper;

    @Override
    public List<StockDayInfo> getStockdaybar(String stockNum) {
        StockBean stockBean = new StockBean();
        stockBean.setStocknum(stockNum);
        List<StockDayInfo> result = selectStockDayInfoList(stockBean);
        return result;
    }

    @Override
    public void insertStockDayInfo(StockBean stockBean, List<StockDayInfo> stockdayinfo) {
        String stockDayInfoTable = "stockdayinfo_" + stockBean.getStocknum();
        Map<String, Object> stockDayInfoMap = new HashMap<String, Object>();
        stockDayInfoMap.put("stockDayInfoTable", stockDayInfoTable);

        java.sql.Date updateDate = stockBean.getUpdateDate();

        if (updateDate != null) {

            Date currentDate = stockdayinfo.get(stockdayinfo.size() - 1).getDate();
            int differentDay = DateUtil.getDifferentDays(updateDate, currentDate);
            if (differentDay == 0) {
                System.out.println("已经最新");
                return;
            }
            List<StockDayInfo> notSaveStockDayInfo = stockdayinfo.subList(stockdayinfo.size() - differentDay, stockdayinfo.size());
            stockDayInfoMap.put("stockdayinfo", notSaveStockDayInfo);//.subList(stockDayInfoSize - 5, stockDayInfoSize));
            stockMapper.insertStockDayInfo(stockDayInfoMap);
            return;
        }

        List<StockDayInfo> notSaveStockDayInfo = stockdayinfo;
        while (notSaveStockDayInfo.size() > 200) {
            List<StockDayInfo> insertStockDayInfoList = notSaveStockDayInfo.subList(0, 200);
            stockDayInfoMap.put("stockdayinfo", insertStockDayInfoList);
            stockMapper.insertStockDayInfo(stockDayInfoMap);
            notSaveStockDayInfo = notSaveStockDayInfo.subList(200, notSaveStockDayInfo.size());
        }
        stockDayInfoMap.put("stockdayinfo", notSaveStockDayInfo);//.subList(stockDayInfoSize - 5, stockDayInfoSize));
        stockMapper.insertStockDayInfo(stockDayInfoMap);

    }

    @Override
    public List<StockDayInfo> selectStockDayInfoList(StockBean stockBean) {
        stockBean.setStockTableName("stockdayinfo_" + stockBean.getStocknum());
        return stockMapper.selectStockDayInfoList(stockBean);
    }

    /**
     * 获取日线信息
     *
     * @param stockBean
     * @return
     */
    @Override
    public List<StockDayInfo> crawlStockDayInfoListByStockBean(StockBean stockBean) {
        String url = "http://yunhq.sse.com.cn:32041/v1/sh1/dayk/" + stockBean.getStocknum();
        Map<String, String> param = new HashMap<String, String>();
        param.put("begin", "-1800");
        param.put("end", "-1");
        StockKLineObj stockObjBean = null;
        String stockJson = "";
        try {
            stockJson = new com.qiwenshare.common.cbb.ProxyHttpRequest().sendGet(url, param);
            stockObjBean = JSON.parseObject(stockJson, StockKLineObj.class);
        } catch (JSONException e) {
            logger.error("JSONException:抓取报文{}", stockJson);
        }

        if (stockObjBean == null) {
            System.out.println("stockObjBean空指针异常:" + stockBean.getStocknum());
            return null;
        }
        List<String> klineList = JSON.parseArray(stockObjBean.getKline(), String.class);
        List<StockDayInfo> stockDayInfoList = new ArrayList<StockDayInfo>();
        for (String kline : klineList) {
            List<String> stockParseKlineList = JSON.parseArray(kline, String.class);
            StockDayInfo stockDayInfo = new StockDayInfo();
            stockDayInfo.setStockid(stockBean.getStockid());
            try {
                stockDayInfo.setDate(DateUtil.getSqlDateByFormatString("yyyyMMdd", stockParseKlineList.get(StockConstant.DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            stockDayInfo.setStockcode(stockBean.getStocknum());
            stockDayInfo.setOpen(Double.parseDouble(stockParseKlineList.get(StockConstant.OPEN)));
            stockDayInfo.setClose(Double.parseDouble(stockParseKlineList.get(StockConstant.CLOSE)));
            stockDayInfo.setHigh(Double.parseDouble(stockParseKlineList.get(StockConstant.HIGH)));
            stockDayInfo.setLow(Double.parseDouble(stockParseKlineList.get(StockConstant.LOW)));
            stockDayInfo.setVolume(Double.parseDouble(stockParseKlineList.get(StockConstant.VOLUME)));
            stockDayInfo.setAmount(Double.parseDouble(stockParseKlineList.get(StockConstant.AMOUNT)));
            stockDayInfoList.add(stockDayInfo);

        }
        return stockDayInfoList;
    }
}
