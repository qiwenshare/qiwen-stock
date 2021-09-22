package com.qiwenshare.stock.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiwenshare.stock.api.IAbnormalaActionService;
import com.qiwenshare.stock.api.IEchnicalaspectService;
import com.qiwenshare.stock.api.IStockBidService;
import com.qiwenshare.stock.api.IStockDIService;
import com.qiwenshare.stock.common.MiniuiUtil;
import com.qiwenshare.stock.common.ProxyHttpRequest;
import com.qiwenshare.stock.common.TableQueryBean;
import com.qiwenshare.stock.domain.*;
import com.qiwenshare.stock.mapper.StockBidMapper;
import com.qiwenshare.stock.mapper.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class StockDIService extends ServiceImpl<StockMapper, StockBean> implements IStockDIService {

    private static final Logger logger = LoggerFactory.getLogger(StockDIService.class);
    public static ExecutorService executor = Executors.newFixedThreadPool(50);
    @Resource
    StockMapper stockMapper;
    @Resource
    StockBidMapper stockBidMapper;
    @Resource
    IStockBidService stockBidService;
    @Resource
    IAbnormalaActionService abnormalaActionService;
    @Resource
    IEchnicalaspectService echnicalaspectService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void createStockInfoTable(String stocknum) {
        stockMapper.createStockDayInfoTable("stockdayinfo_" + stocknum);
        stockMapper.createStockWeekInfoTable("stockweekinfo_" + stocknum);
        stockMapper.createStockMonthInfoTable("stockmonthinfo_" + stocknum);
        stockMapper.createStockTimeInfoTable("stocktimeinfo_" + stocknum);
    }

    @Override
    public void initStockTable() {
        List<StockBean> stockList = stockMapper.selectTotalStockList();

        for (int i = 0; i < stockList.size(); i++) {
            long stockid = stockList.get(i).getStockid();
            System.out.println("----------stockid--" + stockid);
            EchnicalaspectBean echnicalaspect = new EchnicalaspectBean(stockid);
            StockBidBean stockBidBean = new StockBidBean(stockid);
            AbnormalactionBean abnormalactionBean = new AbnormalactionBean(stockid);

            echnicalaspectService.insertEchnicalaspect(echnicalaspect);
            stockBidService.insertStockBid(stockBidBean);
            abnormalaActionService.insertAbnormalaAction(abnormalactionBean);
        }
    }


    @Override
    public void insertStockList(List<StockBean> stockBeanList) {
        stockMapper.insertStockList(stockBeanList);

        List<StockBean> stockBeans = stockMapper.selectTotalStockList();
        for (StockBean stockBean : stockBeans) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("股票详情获取中：", stockBean.getStocknum());
                        JSONObject stockShare = getStockShare(stockBean.getStocknum());
                        stockBean.setTotalFlowShares(stockShare.getDoubleValue("DOMESTIC_SHARES") * 10000);
                        stockBean.setTotalShares(stockShare.getDoubleValue("UNLIMITED_SHARES") * 10000);
                        stockMapper.updateById(stockBean);
                    } catch (Exception e) {
                        logger.error("getStockShare fail:stocknum{}, errorMessage: {}", stockBean.getStocknum(), e);
                    }
                }
            });
        }
    }

    @Override
    public List<StockBean> selectStockList(TableQueryBean miniuiTableQueryBean) {
        TableQueryBean miniuiTablePageQuery = MiniuiUtil.getMiniuiTablePageQuery(miniuiTableQueryBean);
        return stockMapper.selectStockList(miniuiTablePageQuery);
    }

    @Override
    public StockBean getStockInfoById(String stockId) {
        return stockMapper.getStockInfoById(stockId);
    }

    @Override
    public int getStockCountBySelect(TableQueryBean miniuiTableQueryBean) {
        return stockMapper.getStockCountBySelect(miniuiTableQueryBean);
    }

    @Override
    public List<StockBean> selectTotalStockList() {
        return stockMapper.selectTotalStockList();
    }

    @Override
    public List<StockBean> selectStockBeanList(String key) {
        TableQueryBean tableQueryBean = new TableQueryBean();
        tableQueryBean.setKey(key);
        List<StockBean> stockBeanList = stockMapper.selectStockList(tableQueryBean);
        return stockBeanList;
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
        Map<String, String> param = new HashMap<String, String>();
        param.put("isPagination", "false");
        param.put("sqlId", "COMMON_SSE_CP_GPLB_GPGK_GBJG_C");
        param.put("companyCode", stockCode);
        String sendResult = new ProxyHttpRequest().sendGet(url, param);
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
        String url = "http://query.sse.com.cn/security/stock/getStockListData2.do";
        //String param = "isPagination=true&stockCode=&csrcCode=&areaName=&stockType=" + stockType + "&pageHelp.cacheSize=1&pageHelp.beginPage=1&pageHelp.pageSize=3000&pageHelp.pageNo=1&_=1553181823571";
        List<StockBean> stockBeanList = new ArrayList<StockBean>();
        for (int i = 1 ; i < 5; i++) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("isPagination", "true");
            param.put("stockType", "1");
            param.put("pageHelp.cacheSize", "1");
            param.put("pageHelp.beginPage", i + "");
            param.put("pageHelp.pageSize", "1000");
            param.put("pageHelp.pageNo", i + "");
            param.put("pageHelp.endPage", i + "1");
            String sendResult = new ProxyHttpRequest().sendGet(url, param);
            int retryCount = 0;
            while (sendResult.indexOf("Welcome To Zscaler Directory Authentication Sign In") != -1 && retryCount < 50) {
                sendResult = new ProxyHttpRequest().sendGet(url, param);
                retryCount++;
            }
            JSONObject pageHelp = new JSONObject();
            try {
                pageHelp = JSONObject.parseObject(sendResult).getJSONObject("pageHelp");
            } catch (JSONException e) {
                logger.error("解析jsonb报错："+ pageHelp.toJSONString());
            }
            List<StockBean> jsonArr = JSON.parseArray(pageHelp.getString("data"), StockBean.class);

            if (jsonArr.size() == 0) {
                break;
            }


            for (int  j = 0; j < jsonArr.size(); j++) {
                StockBean stockStr = jsonArr.get(j);
                stockStr.setStocknum(stockStr.getCOMPANY_CODE());
                stockStr.setStockname(stockStr.getCOMPANY_ABBR());


                stockBeanList.add(stockStr);
            }
        }



        return stockBeanList;
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
        StockDayInfo pre3Stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1 - 3);
        StockDayInfo pre5Stockdayinfo = stockdayinfoList.get(stockdayinfoListSize - 1 - 5);

        double currentClosePrise = currentStockdayinfo.getClose();
        double currentVolume = currentStockdayinfo.getVolume();
        double currentTotalFlowShares = stockBean.getTotalFlowShares();
        double currentHigh = currentStockdayinfo.getHigh();
        double currentLow = currentStockdayinfo.getLow();
        double preClosePrise = preStockdayinfo.getClose();
        double pre3ClosePrise = pre3Stockdayinfo.getClose();
        double pre5ClosePrise = pre5Stockdayinfo.getClose();
        double updownrange = 0;
        double updownrange3 = 0;
        double updownrange5 = 0;
        double turnoverrate = 0;
        if (currentTotalFlowShares == 0) {
            logger.error("currentTotalFlowShares is zero, stockBean : {}", JSON.toJSONString(stockBean));
        } else {
            turnoverrate = currentVolume / currentTotalFlowShares;
        }

        double updownprices = currentClosePrise - preClosePrise;
        Date newDate = currentStockdayinfo.getDate();

        double amplitude = 0;
        if (currentClosePrise - preClosePrise != 0) {
            if (preClosePrise == 0) {
                logger.error("preClosePrise is zero, stockBean : {}", JSON.toJSONString(stockBean));
            } else {
                amplitude = (currentHigh - currentLow) / preClosePrise;
                updownrange = (currentClosePrise - preClosePrise) / preClosePrise;
            }

        }
        if (currentClosePrise - pre3ClosePrise != 0) {
            if (pre3ClosePrise == 0) {
                logger.error("pre3ClosePrise is zero, stockBean : {}", JSON.toJSONString(stockBean));
            } else {
                updownrange3 = (currentClosePrise - pre3ClosePrise) / pre3ClosePrise;
            }

        }
        if (currentClosePrise - pre5ClosePrise != 0) {
            if (pre5ClosePrise == 0) {
                logger.error("pre5ClosePrise is zero, stockBean : {}", JSON.toJSONString(stockBean));
            } else {
                updownrange5 = (currentClosePrise - pre5ClosePrise) / pre5ClosePrise;
            }

        }

        currentStockBean.setUpdownrange(updownrange);
        currentStockBean.setUpdownrange3(updownrange3);
        currentStockBean.setUpdownrange5(updownrange5);
        currentStockBean.setTurnoverrate(turnoverrate);
        currentStockBean.setUpdownprices(updownprices);
        currentStockBean.setOpen(currentStockdayinfo.getOpen());
        currentStockBean.setClose(currentStockdayinfo.getClose());
        currentStockBean.setHigh(currentStockdayinfo.getHigh());
        currentStockBean.setLow(currentStockdayinfo.getLow());
        currentStockBean.setPreclose(preClosePrise);
        currentStockBean.setVolume(currentStockdayinfo.getVolume());
        currentStockBean.setAmount(currentStockdayinfo.getAmount());
        currentStockBean.setAmplitude(amplitude);
        currentStockBean.setTotalmarketvalue(stockBean.getTotalShares() * currentClosePrise);
        currentStockBean.setFlowmarketvalue(stockBean.getTotalFlowShares() * currentClosePrise);
        currentStockBean.setStockid(stockBean.getStockid());
        currentStockBean.setUpdateDate(newDate);

        return currentStockBean;
    }

}
