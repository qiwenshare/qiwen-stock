package com.qiwenshare.stock.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.qiwenshare.common.util.DateUtil;
import com.qiwenshare.stock.api.IStockDayInfoService;
import com.qiwenshare.stock.common.HttpsUtils;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StockDayInfoService implements IStockDayInfoService {
    private static final Logger logger = LoggerFactory.getLogger(StockDayInfoService.class);
    @Resource
    StockMapper stockMapper;

    @Override
    public List<StockDayInfo> getStockdaybar(String stockNum) {
        StockBean stockBean = new StockBean();
        stockBean.setStockNum(stockNum);
        List<StockDayInfo> result = selectStockDayInfoList(stockBean);
        return result;
    }

    @Override
    public void insertStockDayInfo(StockBean stockBean, List<StockDayInfo> stockdayinfo) {
        if (stockdayinfo.size() == 0) {
            return;
        }
        String stockDayInfoTable = "stockdayinfo_" + stockBean.getStockNum();
        Map<String, Object> stockDayInfoMap = new HashMap<String, Object>();
        stockDayInfoMap.put("stockDayInfoTable", stockDayInfoTable);

        String updateDate = stockBean.getUpdateDate();

        if (StringUtils.isNotEmpty(updateDate)) {

            String currentDate = stockdayinfo.get(stockdayinfo.size() - 1).getDate();

            if (currentDate.equals(updateDate)) {
                System.out.println("已经最新");
                return;
            }

            int differentDay = 0;
            try {
                differentDay = DateUtil.getDifferentDays(DateUtil.getDateByFormatString(updateDate, "yyyy-MM-dd"), DateUtil.getDateByFormatString(currentDate, "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<StockDayInfo> notSaveStockDayInfo;
            if (stockdayinfo.size() > differentDay) {
                notSaveStockDayInfo = stockdayinfo.subList(stockdayinfo.size() - differentDay, stockdayinfo.size());
            } else {
                notSaveStockDayInfo = stockdayinfo;
            }
            stockDayInfoMap.put("stockdayinfo", notSaveStockDayInfo);
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
        stockDayInfoMap.put("stockdayinfo", notSaveStockDayInfo);
        stockMapper.insertStockDayInfo(stockDayInfoMap);

    }

    @Override
    public List<StockDayInfo> selectStockDayInfoList(StockBean stockBean) {
        stockBean.setStockTableName("stockdayinfo_" + stockBean.getStockNum());
        return stockMapper.selectStockDayInfoList(stockBean);
    }

    @Override
    public List<StockDayInfo> crawlStockDayInfoListByStockBean(String stockNum) {
        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("fields1", "f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6");
        param.put("fields2", "f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61");
        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
        param.put("klt", "101");
        param.put("fqt", "1");
        param.put("secid", "1." + stockNum);
        param.put("beg", "0");
        param.put("end", "20500000");
        String sendResult = HttpsUtils.doGetString(url, param);

        JSONObject data = new JSONObject();
        try {
            data = JSONObject.parseObject(sendResult).getJSONObject("data");
        } catch (JSONException e) {
            log.error("解析jsonb报错：" + data.toJSONString());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        List<StockDayInfo> stockDayInfoList = new ArrayList<StockDayInfo>();
//        BigDecimal preKPrice = objectToBigDecimal(data.get("preKPrice"));

        JSONArray klines = data.getJSONArray("klines");
        for (int i = 0; i < klines.size(); i++) {
            String kline = (String) klines.get(i);
            String[] item = kline.split(",");
            StockDayInfo stockDayInfo = new StockDayInfo();
            stockDayInfo.setDate(item[0]);
            stockDayInfo.setOpen(objectToBigDecimal(item[1]));
            stockDayInfo.setClose(objectToBigDecimal(item[2]));
            stockDayInfo.setHigh(objectToBigDecimal(item[3]));
            stockDayInfo.setLow(objectToBigDecimal(item[4]));
            stockDayInfo.setVolume(objectToBigDecimal(item[5]));
            stockDayInfo.setAmount(objectToBigDecimal(item[6]));
            if (objectToBigDecimal(item[9]) == null) {
                stockDayInfo.setPreClose(0);
            } else {
                if (objectToBigDecimal(item[2]) == null) {
                    log.info("---------------null-------------" + stockNum);
                }
                stockDayInfo.setPreClose(objectToBigDecimal(item[2]) == null ? 0 : objectToBigDecimal(item[2])-(objectToBigDecimal(item[9])));
            }

            stockDayInfoList.add(stockDayInfo);
        }

        return stockDayInfoList;
    }

    public Double objectToBigDecimal(Object o) {
        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return Double.valueOf(0);
        }


    }

}
