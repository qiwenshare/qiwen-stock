package com.qiwenshare.stock.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.qiwenshare.stock.api.IStockTimeInfoService;
import com.qiwenshare.stock.common.HttpsUtils;
import com.qiwenshare.stock.domain.StockTimeInfo;
import com.qiwenshare.stock.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public List<StockTimeInfo> crawlStockTimeInfoList(String stockNum) {
        String url = "http://push2his.eastmoney.com/api/qt/stock/trends2/get";
        //String param = "begin=0&end=-1&select=time,price,volume";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("fields1", "f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6%2Cf7%2Cf8%2Cf9%2Cf10%2Cf11%2Cf12%2Cf13");
        param.put("fields2", "f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58");
        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
        param.put("ndays", "1");
        param.put("iscr", "0");
        param.put("secid", "1." + stockNum);
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

        List<StockTimeInfo> stockTimeInfoList = new ArrayList<StockTimeInfo>();
        Double prevClose = objectToBigDecimal(data.get("prePrice"));
        Double sumPrice = 0D;
        JSONArray trends = data.getJSONArray("trends");
        for (int i = 0; i < trends.size(); i++) {

            StockTimeInfo stockTimeInfo = new StockTimeInfo();
            String trend = (String) trends.get(i);
            String[] item = trend.split(",");
            Double price = objectToBigDecimal(item[2]);
            sumPrice += price;
            String datetime = item[0];
            stockTimeInfo.setDate(datetime.split(" ")[0]);
            stockTimeInfo.setTime(datetime.split(" ")[1]);
            stockTimeInfo.setStockCode(stockNum);
            stockTimeInfo.setPrice(objectToBigDecimal(item[2]));
            stockTimeInfo.setAmount(objectToBigDecimal(item[6]));
            stockTimeInfo.setVolume(objectToBigDecimal(item[5]));
            stockTimeInfo.setUpDownRange((price-prevClose)/prevClose);
            stockTimeInfo.setAvgPrice(sumPrice/(i+1));
            stockTimeInfoList.add(stockTimeInfo);
        }
        return stockTimeInfoList;
    }

    @Override
    public void insertStockTimeInfo(String stockNum, List<StockTimeInfo> stocktimeinfo) {

        Map<String, Object> stockDayInfoMap = new HashMap<String, Object>();
        stockDayInfoMap.put("stockTimeInfoTable", "stocktimeinfo_" + stockNum);
        stockDayInfoMap.put("stocktimeinfo", stocktimeinfo);
        stockMapper.insertStockTimeInfo(stockDayInfoMap);
    }


    public Double objectToBigDecimal(Object o) {
        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return Double.valueOf(0);
        }


    }
}
