package com.qiwenshare.stock.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiwenshare.stock.api.IStockDIService;
import com.qiwenshare.stock.common.HttpsUtils;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.mapper.StockMapper;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class StockService extends ServiceImpl<StockMapper, StockBean> implements IStockDIService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    public static ExecutorService executor = Executors.newFixedThreadPool(50);
    @Resource
    StockMapper stockMapper;

    @Override
    public void createStockInfoTable(String stockNum) {
        stockMapper.createStockDayInfoTable("stockdayinfo_" + stockNum);
        stockMapper.createStockWeekInfoTable("stockweekinfo_" + stockNum);
        stockMapper.createStockMonthInfoTable("stockmonthinfo_" + stockNum);
        stockMapper.createStockTimeInfoTable("stocktimeinfo_" + stockNum);
    }


    @Override
    public void insertStockList(List<StockBean> stockBeanList) {
        stockMapper.insertStockList(stockBeanList);
    }

    @Override
    public List<StockBean> selectStockList(@Param("key") String key, @Param("beginCount") Long beginCount, @Param("pageCount") Long pageCount) {
//        TableQueryBean miniuiTablePageQuery = MiniuiUtil.getMiniuiTablePageQuery(miniuiTableQueryBean);
        return stockMapper.selectStockList(key, beginCount, pageCount);
    }


    @Override
    public int getStockCount(String key, Long beginCount, Long pageCount) {
        return stockMapper.getStockCount(key, beginCount, pageCount);
    }

    @Override
    public List<StockBean> selectTotalStockList() {
        return stockMapper.selectTotalStockList();
    }

    @Override
    public StockBean getStockInfoById(String stockId) {
        return stockMapper.getStockInfoById(stockId);
    }

    @Override
    public List<StockBean> getNoExistStockList(List<StockBean> stockBeanList) {
        List<StockBean> stockList = selectTotalStockList();
        List<StockBean> newStock = new ArrayList<StockBean>();
        for (int i = 0; i < stockBeanList.size(); i++) {
            if (!stockList.contains(stockBeanList.get(i))) {
                newStock.add(stockBeanList.get(i));
            }
        }
        return newStock;
    }


    public JSONObject getStockShare(String stockCode) {
        System.out.println("正在获取股票编号：" + stockCode);
        String url = "http://query.sse.com.cn/commonQuery.do";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("isPagination", "false");
        param.put("sqlId", "COMMON_SSE_CP_GPLB_GPGK_GBJG_C");
        param.put("companyCode", stockCode);
        String sendResult = HttpsUtils.doGetString(url, param);
        JSONArray result = null;
        try {
            result = JSONObject.parseObject(sendResult).getJSONArray("result");
        } catch (Exception e) {
            logger.error("com.alibaba.fastjson.JSONException: scan null error:{}" + e);
        }

        if (result == null || result.size() == 0) {
            return new JSONObject();
        }
        return result.getJSONObject(0);
    }


    @Override
    public List<StockBean> getStockListByScript() {
        String url = "http://19.push2.eastmoney.com/api/qt/clist/get";

        List<StockBean> stockBeanList = new ArrayList<StockBean>();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pn", "1");
        param.put("pz", "3000");
        param.put("po", "1");
        param.put("np", "1");
        param.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        param.put("fltt", "2");
        param.put("invt", "2");
        param.put("fid", "f3");
        param.put("fs", "m:1+t:2,m:1+t:23");
        param.put("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f38,f39,f22,f11,f62,f128,f136,f115,f152,f297");
        param.put("invt", "2");
        param.put("invt", "2");
        param.put("invt", "2");
        String sendResult = HttpsUtils.doGetString(url, param);

        JSONObject data = new JSONObject();
        try {
            data = JSONObject.parseObject(sendResult).getJSONObject("data");
        } catch (JSONException e) {
            logger.error("解析jsonb报错：" + data.toJSONString());
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        JSONArray diff = data.getJSONArray("diff");
        for (Object o : diff) {
            Map<String, Object> map = (Map) o;
            StockBean stockBean = new StockBean();
            stockBean.setStockNum((String) map.get("f12"));
            stockBean.setStockName((String) map.get("f14"));
            stockBean.setTotalFlowShares(objectToBigDecimal(map.get("f39")));
            stockBean.setTotalShares(objectToBigDecimal(map.get("f38")));
            stockBean.setUpDownRange(objectToBigDecimal(map.get("f3")));
            stockBean.setTurnOverrate(objectToBigDecimal(map.get("f8")));
            stockBean.setUpDownPrices(objectToBigDecimal(map.get("f4")));
            stockBean.setOpen(objectToBigDecimal(map.get("f17")));
            stockBean.setClose(objectToBigDecimal(map.get("f2")));
            stockBean.setHigh(objectToBigDecimal(map.get("f15")));
            stockBean.setLow(objectToBigDecimal(map.get("f16")));
            stockBean.setPreClose(objectToBigDecimal(map.get("f18")));
            stockBean.setVolume(objectToBigDecimal(map.get("f5")));
            stockBean.setAmount(objectToBigDecimal(map.get("f6")));
            stockBean.setAmplitude(objectToBigDecimal(map.get("f7")));
            stockBean.setTotalMarketValue(objectToBigDecimal( map.get("f20")));
            stockBean.setFlowMarketValue(objectToBigDecimal(map.get("f21")));
            stockBean.setListingDate(String.valueOf(map.get("f26")));

            stockBeanList.add(stockBean);
        }

        return stockBeanList;
    }

    public Double objectToBigDecimal(Object o) {
        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return Double.valueOf(0);
        }


    }


    @Override
    public void updateStock(StockBean stockBean) {
        stockMapper.updateById(stockBean);
    }

    @Override
    public StockBean getStockInfo(StockBean stockBean, List<StockDayInfo> stockdayinfoList) {
        StockBean currentStockBean = new StockBean();
        BeanUtil.copyProperties(stockBean, currentStockBean);
        int stockdayinfoListSize = stockdayinfoList.size();
        StockDayInfo currentStockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1);
        StockDayInfo preStockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1 - 1);
        StockDayInfo pre3Stockdayinfo = new StockDayInfo();
        StockDayInfo pre5Stockdayinfo = new StockDayInfo();
        if (stockdayinfoListSize >= 4) {
            pre3Stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1 - 3);
        }
        if (stockdayinfoListSize >= 6) {
            pre5Stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1 - 5);
        }

        double currentClosePrise = currentStockdayinfo.getClose();
        double currentVolume = currentStockdayinfo.getVolume();
        double currentTotalFlowShares = stockBean.getTotalFlowShares();
        double currentHigh = currentStockdayinfo.getHigh();
        double currentLow = currentStockdayinfo.getLow();
        double preClosePrise = preStockdayinfo.getClose();
        double pre3ClosePrise = pre3Stockdayinfo.getClose();
        double pre5ClosePrise = pre5Stockdayinfo.getClose();
        double upDownRange = 0;
        double upDownRange3 = 0;
        double upDownRange5 = 0;
        double turnOverrate = 0;
        if (currentTotalFlowShares == 0) {
            logger.error("currentTotalFlowShares is zero, stockBean : {}", JSON.toJSONString(stockBean));
        } else {
            turnOverrate = currentVolume * 100 / currentTotalFlowShares;
        }

        double upDownPrices = currentClosePrise - preClosePrise;
        String newDate = currentStockdayinfo.getDate();

        double amplitude = 0;
        if (currentClosePrise - preClosePrise != 0) {
            if (preClosePrise == 0) {
                logger.error("preClosePrise is zero, stockBean : {}", JSON.toJSONString(stockBean));
            } else {
                amplitude = (currentHigh - currentLow) / preClosePrise;
                upDownRange = (currentClosePrise - preClosePrise) / preClosePrise;
            }

        }
        if (currentClosePrise - pre3ClosePrise != 0) {
            if (pre3ClosePrise == 0) {
                logger.error("pre3ClosePrise is zero, stockBean : {}", JSON.toJSONString(stockBean));
            } else {
                upDownRange3 = (currentClosePrise - pre3ClosePrise) / pre3ClosePrise;
            }

        }
        if (currentClosePrise - pre5ClosePrise != 0) {
            if (pre5ClosePrise == 0) {
                logger.error("pre5ClosePrise is zero, stockBean : {}", JSON.toJSONString(stockBean));
            } else {
                upDownRange5 = (currentClosePrise - pre5ClosePrise) / pre5ClosePrise;
            }

        }

        currentStockBean.setUpDownRange(upDownRange);
        currentStockBean.setUpDownRange3(upDownRange3);
        currentStockBean.setUpDownRange5(upDownRange5);
        currentStockBean.setTurnOverrate(turnOverrate);
        currentStockBean.setUpDownPrices(upDownPrices);
        currentStockBean.setOpen(currentStockdayinfo.getOpen());
        currentStockBean.setClose(currentStockdayinfo.getClose());
        currentStockBean.setHigh(currentStockdayinfo.getHigh());
        currentStockBean.setLow(currentStockdayinfo.getLow());
        currentStockBean.setPreClose(preClosePrise);
        currentStockBean.setVolume(currentStockdayinfo.getVolume());
        currentStockBean.setAmount(currentStockdayinfo.getAmount());
        currentStockBean.setAmplitude(amplitude);
        currentStockBean.setTotalMarketValue(stockBean.getTotalShares() * currentClosePrise);
        currentStockBean.setFlowMarketValue(stockBean.getTotalFlowShares() * currentClosePrise);
        currentStockBean.setStockId(stockBean.getStockId());
        currentStockBean.setUpdateDate(newDate);

        return currentStockBean;
    }

}
