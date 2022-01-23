package com.qiwenshare.stock.executor;

import com.qiwenshare.stock.api.*;
import com.qiwenshare.stock.common.TaskProcess;
import com.qiwenshare.stock.constant.StockTaskTypeEnum;
import com.qiwenshare.stock.controller.StockController;
import com.qiwenshare.stock.domain.*;
import com.qiwenshare.stock.indicator.product.Abnormalaction;
import com.qiwenshare.stock.indicator.proxy.IndicatorProxy;
import com.qiwenshare.stock.websocket.StockWebsocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class StockExecutor {
    private static final int NTHREADS = 20;
    public static volatile int updateCount = 0;

    public static int totalCount = 0;
    public static ExecutorService stockExecutor = Executors.newFixedThreadPool(NTHREADS);
    @Resource
    IStockDIService stockDIService;
    @Resource
    IStockTimeInfoService stockTimeInfoService;
    @Resource
    IStockDayInfoService stockDayInfoService;
    @Resource
    IStockWeekInfoService stockWeekInfoService;
    @Resource
    IStockMonthInfoService stockMonthInfoService;
    @Resource
    IEchnicalaspectService echnicalaspectService;
    @Resource
    IAbnormalaActionService abnormalaActionService;
    @Resource
    IStockBidService stockBidService;

    /**
     * 任务启动入口
     * @param stockList
     * @return
     */
    public boolean start(List<StockBean> stockList) {
        //初始化进度数据
        initProcessData(stockList);


        for (StockBean stockBean : stockList) {
            stockUpdateTask(stockBean);
        }
        return true;
    }

    /**
     * 任务停止入口
     * @param
     * @return
     * @throws InterruptedException
     */
    public boolean stop() throws InterruptedException {
        if (stockExecutor != null && stockExecutor.isShutdown()) {
            return false;
        }
        stockExecutor.shutdown();
        stockExecutor.awaitTermination(3, TimeUnit.SECONDS);

        return true;
    }

    public void initProcessData(List<StockBean> stockList) {
        totalCount = stockList.size();
        updateCount = 0;

    }

    public void stockUpdateTask(StockBean stockBean) {
        if (stockExecutor.isShutdown()) {
            log.info("股票列表线程池被关闭，重新初始化线程池");
            stockExecutor = Executors.newFixedThreadPool(NTHREADS);
        }
        stockExecutor.execute(new Runnable() {
            @Override
            public void run() {
                log.info("StockRunnable run start");
                if (!StockExecutor.stockExecutor.isShutdown()) {
                    TaskProcess taskProcess = new TaskProcess();
                    taskProcess.setTaskId(StockTaskTypeEnum.STOCK_DETAIL.getTypeCode());

                    synchronized (StockController.class) {
                        updateCount++;
                    }
                    String stockNum = stockBean.getStockNum();
                    List<StockTimeInfo> stockTimeInfoList = stockTimeInfoService.crawlStockTimeInfoList(stockNum);
                    StockBidBean stockBidBean = stockBidService.crawlBidByStockBean(stockNum);
                    stockBidService.updateStockBid(stockBidBean);
                    stockTimeInfoService.insertStockTimeInfo(stockNum, stockTimeInfoList);

                    AbnormalactionBean abnormalaction = new Abnormalaction().getAbnormalaction(stockTimeInfoList);
                    abnormalaction.setStockNum(stockNum);
                    abnormalaActionService.updateAbnormalaAction(abnormalaction);


                    List<StockDayInfo> stockDayInfoList = stockDayInfoService.crawlStockDayInfoListByStockBean(stockNum);
                    stockDayInfoList = new IndicatorProxy().getDayIndicatorList(stockDayInfoList);
                    stockDayInfoService.insertStockDayInfo(stockBean, stockDayInfoList);

                    EchnicalaspectBean echnicalaspectBean = echnicalaspectService.getEchnicalaspectInfo(stockNum, stockDayInfoList);
                    StockBean stockInfo = stockDIService.getStockInfo(stockBean, stockDayInfoList);
                    echnicalaspectService.updateEchnicalaspect(echnicalaspectBean);
                    stockDIService.updateStock(stockInfo);


                    List<StockWeekInfo> stockWeekInfoList = stockWeekInfoService.getStockWeekInfoList(stockDayInfoList);
                    stockWeekInfoService.insertStockWeekInfo(stockNum, stockWeekInfoList);
                    List<StockMonthInfo> stockMonthInfoList = stockMonthInfoService.getStockMonthInfoList(stockDayInfoList);
                    stockMonthInfoService.insertStockMonthInfo(stockNum, stockMonthInfoList);

                    taskProcess.setCompleteCount(updateCount);
                    taskProcess.setTotalCount(totalCount);
                    taskProcess.setTaskInfo("采集项：" + stockNum + "完成进度：" + updateCount + "/" + totalCount);
                    taskProcess.setRunTask(totalCount != updateCount);
                    StockWebsocket.pushTaskProcess(taskProcess);
                }
            }
        });

    }
}
