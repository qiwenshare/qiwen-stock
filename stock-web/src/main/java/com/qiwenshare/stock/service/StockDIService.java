package com.qiwenshare.stock.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.qiwenshare.common.cbb.DateUtil;
//import com.qiwenshare.common.cbb.HibernateUtil;
import com.qiwenshare.common.cbb.MiniuiUtil;
import com.qiwenshare.common.domain.TableQueryBean;
import com.qiwenshare.stock.analysis.tactics.CattleCatchingAnalysis;
import com.qiwenshare.stock.analysis.tactics.TacticsAnalysis;
import com.qiwenshare.stock.api.IStockDIService;
import com.qiwenshare.stock.domain.*;
import com.qiwenshare.stock.indicator.product.KDJ;
import com.qiwenshare.stock.indicator.product.MA;
import com.qiwenshare.stock.indicator.product.MACD;
import com.qiwenshare.stock.indicator.product.RSI;
import com.qiwenshare.stock.mapper.StockMapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.query.Query;
import org.hibernate.type.*;
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
public class StockDIService implements IStockDIService {

    private static final Logger logger = LoggerFactory.getLogger(StockDIService.class);
    public static ExecutorService executor = Executors.newFixedThreadPool(50);
    @Resource
    StockMapper stockMapper;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public StockBidBean getStockBidBean(String stockNum) {
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = sqlsession.beginTransaction();
            Query query = sqlsession.createQuery("from StockBidBean where stocknum = :stocknum");
            query.setParameter("stocknum", stockNum);

            List<StockBidBean> stockBeanList = (List<StockBidBean>) query.list();
            tx.commit();
            return stockBeanList.get(0);


        } finally {
            sqlsession.close();
        }
    }

    @Override
    public EchnicalaspectBean getEchnicalaspectBean(long stockid) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction tx = null;
        try {
            tx = sqlsession.beginTransaction();

            Criteria criteria = sqlsession.createCriteria(EchnicalaspectBean.class);
            SimpleExpression se1 = Expression.eq("stockid", stockid);
            criteria.add(se1);
            List<EchnicalaspectBean> list = criteria.list();

            tx.commit();
            return list.get(0);


        } finally {
            sqlsession.close();
        }
    }

    @Override
    public AbnormalactionBean getAbnormalactionBean(long stockid) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction tx = null;
        try {
            tx = sqlsession.beginTransaction();

            Criteria criteria = sqlsession.createCriteria(AbnormalactionBean.class);
            SimpleExpression se1 = Expression.eq("stockid", stockid);
            criteria.add(se1);
            List<AbnormalactionBean> list = criteria.list();

            tx.commit();
            return list.get(0);


        } finally {
            sqlsession.close();
        }
    }

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

            insertEchnicalaspect(echnicalaspect);
            insertStockBid(stockBidBean);
            insertAbnormalaAction(abnormalactionBean);
        }
    }


    @Override
    public void insertStockList(List<StockBean> stockBeanList) {
        stockMapper.insertStockList(stockBeanList);
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
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction tx = null;
        try {
            tx = sqlsession.beginTransaction();
            Query query = sqlsession.createQuery("from StockBean where stocknum like :key or stockname like :key");
            query.setParameter("key", "%" + key + "%");

            List<StockBean> stockBeanList = (List<StockBean>) query.list();
            tx.commit();
            return stockBeanList;


        } finally {
            sqlsession.close();
        }
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
        String sendResult = new com.qiwenshare.common.cbb.ProxyHttpRequest().sendGet(url, param);
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
            String sendResult = new com.qiwenshare.common.cbb.ProxyHttpRequest().sendGet(url, param);
            int retryCount = 0;
            while (sendResult.indexOf("Welcome To Zscaler Directory Authentication Sign In") != -1 && retryCount < 50) {
                sendResult = new com.qiwenshare.common.cbb.ProxyHttpRequest().sendGet(url, param);
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

        for (StockBean stockBean : stockBeanList) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("股票详情获取中：", stockBean.getStocknum());
                        JSONObject stockShare = getStockShare(stockBean.getCOMPANY_CODE());
                        stockBean.setTotalFlowShares(stockShare.getDoubleValue("DOMESTIC_SHARES") * 10000);
                        stockBean.setTotalShares(stockShare.getDoubleValue("UNLIMITED_SHARES") * 10000);
                    } catch (Exception e) {
                        logger.error("getStockShare fail:stocknum{}, errorMessage: {}", stockBean.getStocknum(), e);
                    }
                }
            });
        }

        return stockBeanList;
    }

    @Override
    public void updateStockBid(StockBidBean stockBidBean) {
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Transaction transaction = sqlsession.beginTransaction();
        System.out.println(stockBidBean.getBoughtcount1());
        Query query = sqlsession.createQuery("update StockBidBean set " +
                "stocknum = :stocknum, " +
                "date = :date, " +
                "time = :time, " +
                "sellprice5 = :sellprice5, " +
                "sellprice4 = :sellprice4, " +
                "sellprice3 = :sellprice3, " +
                "sellprice2 = :sellprice2, " +
                "sellprice1 = :sellprice1, " +
                "boughtprice1 = :boughtprice1, " +
                "boughtprice2 = :boughtprice2, " +
                "boughtprice3 = :boughtprice3, " +
                "boughtprice4 = :boughtprice4, " +
                "boughtprice5 = :boughtprice5, " +
                "sellcount5 = :sellcount5, " +
                "sellcount4 = :sellcount4, " +
                "sellcount3 = :sellcount3, " +
                "sellcount2 = :sellcount2, " +
                "sellcount1 = :sellcount1, " +
                "boughtcount1 = :boughtcount1, " +
                "boughtcount2 = :boughtcount2, " +
                "boughtcount3 = :boughtcount3, " +
                "boughtcount4 = :boughtcount4, " +
                "boughtcount5 = :boughtcount5 " +
                "where stockid = :stockid");
        query.setParameter("stocknum", stockBidBean.getStocknum(), StringType.INSTANCE);
        query.setParameter("date", stockBidBean.getDate(), StringType.INSTANCE);
        query.setParameter("time", stockBidBean.getTime(), StringType.INSTANCE);
        query.setParameter("sellprice5", stockBidBean.getSellprice5(), DoubleType.INSTANCE);
        query.setParameter("sellprice4", stockBidBean.getSellprice4(), DoubleType.INSTANCE);
        query.setParameter("sellprice3", stockBidBean.getSellprice3(), DoubleType.INSTANCE);
        query.setParameter("sellprice2", stockBidBean.getSellprice2(), DoubleType.INSTANCE);
        query.setParameter("sellprice1", stockBidBean.getSellprice1(), DoubleType.INSTANCE);
        query.setParameter("boughtprice1", stockBidBean.getBoughtprice1(), DoubleType.INSTANCE);
        query.setParameter("boughtprice2", stockBidBean.getBoughtprice2(), DoubleType.INSTANCE);
        query.setParameter("boughtprice3", stockBidBean.getBoughtprice3(), DoubleType.INSTANCE);
        query.setParameter("boughtprice4", stockBidBean.getBoughtprice4(), DoubleType.INSTANCE);
        query.setParameter("boughtprice5", stockBidBean.getBoughtprice5(), DoubleType.INSTANCE);
        query.setParameter("sellcount5", stockBidBean.getSellcount5(), IntegerType.INSTANCE);
        query.setParameter("sellcount4", stockBidBean.getSellcount4(), IntegerType.INSTANCE);
        query.setParameter("sellcount3", stockBidBean.getSellcount3(), IntegerType.INSTANCE);
        query.setParameter("sellcount2", stockBidBean.getSellcount2(), IntegerType.INSTANCE);
        query.setParameter("sellcount1", stockBidBean.getSellcount1(), IntegerType.INSTANCE);
        query.setParameter("boughtcount1", stockBidBean.getBoughtcount1(), IntegerType.INSTANCE);
        query.setParameter("boughtcount2", stockBidBean.getBoughtcount2(), IntegerType.INSTANCE);
        query.setParameter("boughtcount3", stockBidBean.getBoughtcount3(), IntegerType.INSTANCE);
        query.setParameter("boughtcount4", stockBidBean.getBoughtcount4(), IntegerType.INSTANCE);
        query.setParameter("boughtcount5", stockBidBean.getBoughtcount5(), IntegerType.INSTANCE);
        query.setParameter("stockid", stockBidBean.getStockid(), LongType.INSTANCE);
        query.executeUpdate();
        //提交事务
        transaction.commit();
        sqlsession.close();
    }

    @Override
    public void updateEchnicalaspect(EchnicalaspectBean echnicalaspectBean) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();

        Query query = sqlsession.createQuery("update EchnicalaspectBean set "
                + "updatedate = :updatedate, "
                + "goldencross = :goldencross, "
                + "deathcross = :deathcross, "
                + "technicalreturn = :technicalreturn, "
                + "overbought = :overbought, "
                + "oversold = :oversold, "
                + "kdjgoldencross = :kdjgoldencross, "
                + "kdjdeathcross = :kdjdeathcross, "
                + "shortupwardtrend = :shortupwardtrend, "
                + "bullMarket = :bullMarket, "
                + "bearMarket = :bearMarket, "
                + "uptrend = :uptrend, "
                + "downtrend = :downtrend, "
                + "prosperitylevel = :prosperitylevel, "
                + "cattlestart = :cattlestart, "
                + "shortdownwardtrend = :shortdownwardtrend "
                + "where stockid = :stockid");
        query.setParameter("updatedate", echnicalaspectBean.getUpdatedate(), StringType.INSTANCE);
        query.setParameter("goldencross", echnicalaspectBean.getGoldencross(), IntegerType.INSTANCE);
        query.setParameter("deathcross", echnicalaspectBean.getDeathcross(), IntegerType.INSTANCE);
        query.setParameter("technicalreturn", echnicalaspectBean.getTechnicalreturn(), IntegerType.INSTANCE);
        query.setParameter("stockid", echnicalaspectBean.getStockid(), LongType.INSTANCE);
        query.setParameter("overbought", echnicalaspectBean.getOverbought(), IntegerType.INSTANCE);
        query.setParameter("oversold", echnicalaspectBean.getOversold(), IntegerType.INSTANCE);
        query.setParameter("kdjgoldencross", echnicalaspectBean.getKdjgoldencross(), IntegerType.INSTANCE);
        query.setParameter("kdjdeathcross", echnicalaspectBean.getKdjdeathcross(), IntegerType.INSTANCE);
        query.setParameter("shortupwardtrend", echnicalaspectBean.getShortupwardtrend(), IntegerType.INSTANCE);
        query.setParameter("shortdownwardtrend", echnicalaspectBean.getShortdownwardtrend(), IntegerType.INSTANCE);
        query.setParameter("bullMarket", echnicalaspectBean.getBullMarket(), IntegerType.INSTANCE);
        query.setParameter("bearMarket", echnicalaspectBean.getBearMarket(), IntegerType.INSTANCE);
        query.setParameter("uptrend", echnicalaspectBean.getUptrend(), IntegerType.INSTANCE);
        query.setParameter("downtrend", echnicalaspectBean.getDowntrend(), IntegerType.INSTANCE);
        query.setParameter("prosperitylevel", echnicalaspectBean.getProsperitylevel(), IntegerType.INSTANCE);
        query.setParameter("cattlestart", echnicalaspectBean.getCattlestart(), IntegerType.INSTANCE);
        query.executeUpdate();
        //提交事务
        transaction.commit();
        sqlsession.close();
    }


    @Override
    public void insertEchnicalaspect(EchnicalaspectBean echnicalaspectBean) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();
        sqlsession.save(echnicalaspectBean);
        transaction.commit();
        sqlsession.close();
    }

    @Override
    public void insertStockOptional(StockOptionalBean stockOptionalBean) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();
        sqlsession.save(stockOptionalBean);
        transaction.commit();
        sqlsession.close();
    }

    @Override
    public void updateStock(StockBean stockBean) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();

        Query query = sqlsession.createQuery("update StockBean set "
                + "updownrange = :updownrange, "
                + "updownrange3 = :updownrange3, "
                + "updownrange5 = :updownrange5, "
                + "turnoverrate = :turnoverrate, "
                + "updownprices = :updownprices, "
                + "open = :open, "
                + "close = :close, "
                + "high = :high, "
                + "low = :low, "
                + "preclose = :preclose, "
                + "volume = :volume, "
                + "amount = :amount, "
                + "amplitude = :amplitude, "
                + "totalmarketvalue = :totalmarketvalue, "
                + "flowmarketvalue = :flowmarketvalue, "
                + "updateDate = :updateDate "
                + "where stockid = :stockid");
        query.setParameter("updownrange", stockBean.getUpdownrange(), DoubleType.INSTANCE);
        query.setParameter("updownrange3", stockBean.getUpdownrange3(), DoubleType.INSTANCE);
        query.setParameter("updownrange5", stockBean.getUpdownrange5(), DoubleType.INSTANCE);
        query.setParameter("turnoverrate", stockBean.getTurnoverrate(), DoubleType.INSTANCE);
        query.setParameter("updownprices", stockBean.getUpdownprices(), DoubleType.INSTANCE);
        query.setParameter("open", stockBean.getOpen(), DoubleType.INSTANCE);
        query.setParameter("close", stockBean.getClose(), DoubleType.INSTANCE);
        query.setParameter("high", stockBean.getHigh(), DoubleType.INSTANCE);
        query.setParameter("low", stockBean.getLow(), DoubleType.INSTANCE);
        query.setParameter("preclose", stockBean.getPreclose(), DoubleType.INSTANCE);
        query.setParameter("volume", stockBean.getVolume(), DoubleType.INSTANCE);
        query.setParameter("amount", stockBean.getAmount(), DoubleType.INSTANCE);
        query.setParameter("amplitude", stockBean.getAmplitude(), DoubleType.INSTANCE);
        query.setParameter("totalmarketvalue", stockBean.getTotalmarketvalue(), DoubleType.INSTANCE);
        query.setParameter("flowmarketvalue", stockBean.getFlowmarketvalue(), DoubleType.INSTANCE);
        query.setParameter("updateDate", stockBean.getUpdateDate(), DateType.INSTANCE);
        query.setParameter("stockid", stockBean.getStockid(), LongType.INSTANCE);
        query.executeUpdate();
        //提交事务
        transaction.commit();
        sqlsession.close();
    }

    @Override
    public StockBidBean getBidByStockBean(StockBean stockBean) {
        String url = "http://yunhq.sse.com.cn:32041/v1/sh1/snap/" + stockBean.getStocknum();
        //String param = "select=ask,bid";
        Map<String, String> param = new HashMap<String, String>();
        param.put("select", "ask,bid");
        String stockBidJson = new com.qiwenshare.common.cbb.ProxyHttpRequest().sendGet(url, param);
        StockBidObj stockBidObj = JSON.parseObject(stockBidJson, StockBidObj.class);
        if (stockBidObj == null) {
            System.out.println("网络不可用:" + stockBean.getStocknum());
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

    @Override
    public void insertStockBid(StockBidBean stockBidBean) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();
        sqlsession.save(stockBidBean);
        transaction.commit();
        sqlsession.close();
    }

    @Override
    public List<ReplayBean> selectAllReplayList(TableQueryBean tableQueryBean) {
        TableQueryBean miniuiTablePageQuery = MiniuiUtil.getMiniuiTablePageQuery(tableQueryBean);
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction tx = null;
        try {
            tx = sqlsession.beginTransaction();
            Query query = sqlsession.createQuery("from ReplayBean a left join StockBean b on b.stockid = a.stockid");
            query.setFirstResult(miniuiTablePageQuery.getBeginCount());
            query.setMaxResults(miniuiTablePageQuery.getLimit());

            List<ReplayBean> replayBeanList = (List<ReplayBean>) query.list();
            tx.commit();
            return replayBeanList;


        } finally {
            sqlsession.close();
        }
    }

    @Override
    public List<ReplayBean> selectReplayList(long stockid) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction tx = null;
        try {
            tx = sqlsession.beginTransaction();
            Query query = sqlsession.createQuery("from ReplayBean where stockid = :stockid");
            query.setParameter("stockid", stockid);

            List<ReplayBean> replayBeanList = (List<ReplayBean>) query.list();
            tx.commit();
            return replayBeanList;


        } finally {
            sqlsession.close();
        }
    }

    @Override
    public void deleteReplay(long stockid) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();
        Query query = sqlsession.createQuery("delete from ReplayBean where stockid = :stockid");
        query.setParameter("stockid", stockid, LongType.INSTANCE);
        query.executeUpdate();
        //提交事务
        transaction.commit();
        sqlsession.close();

    }

    @Override
    public void insertReplay(List<ReplayBean> replayBeanList) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();
        for (int i = 0; i < replayBeanList.size(); i++) {
            sqlsession.save(replayBeanList.get(i));
            if (i % 20 == 0) {
                sqlsession.flush();
                sqlsession.clear();
            }
        }
        sqlsession.flush();
        sqlsession.clear();
        transaction.commit();
        sqlsession.close();
    }

    @Override
    public void insertAbnormalaAction(AbnormalactionBean abnormalactionBean) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();
        sqlsession.save(abnormalactionBean);
        transaction.commit();
        sqlsession.close();
    }

    @Override
    public void updateAbnormalaAction(AbnormalactionBean abnormalactionBean) {
//        Session sqlsession = HibernateUtil.getInstance().getSessionFactory().openSession();
        Session sqlsession = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = sqlsession.beginTransaction();

        Query query = sqlsession.createQuery("update AbnormalactionBean set "
                + "stocknum = :stocknum, "
                + "isabnormalaction = :isabnormalaction, "
                + "date = :date, "
                + "time = :time, "
                + "updownrange = :updownrange "
                + "where stockid = :stockid");
        query.setParameter("stocknum", abnormalactionBean.getStocknum(), StringType.INSTANCE);
        query.setParameter("isabnormalaction", abnormalactionBean.getIsabnormalaction(), IntegerType.INSTANCE);
        query.setParameter("date", abnormalactionBean.getDate(), DateType.INSTANCE);
        query.setParameter("time", abnormalactionBean.getTime(), TimeType.INSTANCE);
        query.setParameter("updownrange", abnormalactionBean.getUpdownrange(), DoubleType.INSTANCE);
        query.setParameter("stockid", abnormalactionBean.getStockid(), LongType.INSTANCE);
        query.executeUpdate();
        //提交事务
        transaction.commit();
        sqlsession.close();
    }

    @Override
    public EchnicalaspectBean getEchnicalaspectInfo(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        MA ma = new MA();
        KDJ kdj = new KDJ();
        MACD macd = new MACD();
        RSI rsi = new RSI();
        TacticsAnalysis tacticsAnalysis = new CattleCatchingAnalysis();
        StockDayInfo preStockDayInfo = stockDayInfoList.get(stockDayInfoList.size() - 2);
        StockDayInfo stockDayInfo = stockDayInfoList.get(stockDayInfoList.size() - 1);
        ReplayBean replayBean = tacticsAnalysis.getOperation(stockDayInfoList, stockBean);

        boolean isGoldenCross = ma.isGoldenCross(stockDayInfoList);
        boolean isDeathCross = ma.isDeathCross(stockDayInfoList);
        boolean isTechnicalreturn = ma.isTechnicalreturn(stockDayInfoList);
        boolean isOverBought = kdj.isOverbought(stockDayInfoList);
        boolean isOverSold = kdj.isOversold(stockDayInfoList);
        boolean isKdjGoldencross = kdj.isKdjGoldencross(stockDayInfoList);
        boolean isKdjdeathcross = kdj.isKdjdeathcross(stockDayInfoList);
        boolean isShortUpwardTrend = kdj.isShortUpwardTrend(stockDayInfoList);
        boolean isShortDownwardTrend = kdj.isShortDownwardTrend(stockDayInfoList);
        boolean isBullMarket = macd.isBullMarket(preStockDayInfo, stockDayInfo);
        boolean isBearMarket = macd.isBearMarket(preStockDayInfo, stockDayInfo);
        boolean isUpTrend = macd.isUpTrend(preStockDayInfo, stockDayInfo);
        boolean isDownTrend = macd.isDownTrend(preStockDayInfo, stockDayInfo);
        int prosperityLevel = rsi.prosperityLevel(stockDayInfoList);

        String currentTime = DateUtil.getCurrentTime();
        EchnicalaspectBean echnicalaspectBean = new EchnicalaspectBean();
        echnicalaspectBean.setStockid(stockBean.getStockid());
        echnicalaspectBean.setUpdatedate(currentTime);
        echnicalaspectBean.setGoldencross(isGoldenCross ? 1 : 0);
        echnicalaspectBean.setDeathcross(isDeathCross ? 1 : 0);
        echnicalaspectBean.setTechnicalreturn(isTechnicalreturn ? 1 : 0);
        echnicalaspectBean.setOverbought(isOverBought ? 1 : 0);
        echnicalaspectBean.setOversold(isOverSold ? 1 : 0);
        echnicalaspectBean.setKdjgoldencross(isKdjGoldencross ? 1 : 0);
        echnicalaspectBean.setKdjdeathcross(isKdjdeathcross ? 1 : 0);
        echnicalaspectBean.setShortupwardtrend(isShortUpwardTrend ? 1 : 0);
        echnicalaspectBean.setShortdownwardtrend(isShortDownwardTrend ? 1 : 0);
        echnicalaspectBean.setBullMarket(isBullMarket ? 1 : 0);
        echnicalaspectBean.setBearMarket(isBearMarket ? 1 : 0);
        echnicalaspectBean.setUptrend(isUpTrend ? 1 : 0);
        echnicalaspectBean.setDowntrend(isDownTrend ? 1 : 0);
        echnicalaspectBean.setProsperitylevel(prosperityLevel);
        echnicalaspectBean.setCattlestart(replayBean.getBought());

        return echnicalaspectBean;
    }

    @Override
    public StockBean getStockInfo(StockBean stockBean, List<StockDayInfo> stockdayinfoList) {
        StockBean currentStockBean = new StockBean();
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
