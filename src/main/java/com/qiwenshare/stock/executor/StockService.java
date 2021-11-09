package com.qiwenshare.stock.executor;

import com.qiwenshare.stock.api.*;
import com.qiwenshare.stock.constant.StockTaskTypeEnum;
import com.qiwenshare.stock.domain.StockBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class StockService {
    private static final int NTHREADS = 20;
    public static ExecutorService stockExecutor = Executors.newFixedThreadPool(NTHREADS);
    public static ExecutorService stockDayInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
    public static ExecutorService stockTimeInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
    public static ExecutorService stockWeekInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
    public static ExecutorService stockMonthInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
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
    public boolean start(List<StockBean> stockList, StockTaskTypeEnum stockTaskTypeEnum) {
        //初始化进度数据
        initProcessData(stockList, stockTaskTypeEnum);


        for (StockBean stockBean : stockList) {
            stockUpdateTask(stockBean, stockTaskTypeEnum);
            Random rd=new Random();
            try {
                Thread.sleep(rd.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 任务停止入口
     * @param
     * @return
     * @throws InterruptedException
     */
    public boolean stop(StockTaskTypeEnum stockTaskTypeEnum) throws InterruptedException {
        if (stockTaskTypeEnum == StockTaskTypeEnum.STOCK) {
            if (stockExecutor != null && stockExecutor.isShutdown()) {
                return false;
            }
            stockExecutor.shutdown();
            stockExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.TIME) {
            if (stockTimeInfoExecutor != null && stockTimeInfoExecutor.isShutdown()) {
                return false;
            }
            stockTimeInfoExecutor.shutdown();
            stockTimeInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.DAY) {
            if (stockDayInfoExecutor != null && stockDayInfoExecutor.isShutdown()) {
                return false;
            }
            stockDayInfoExecutor.shutdown();
            stockDayInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.WEEK) {
            if (stockWeekInfoExecutor != null && stockWeekInfoExecutor.isShutdown()) {
                return false;
            }
            stockWeekInfoExecutor.shutdown();
            stockWeekInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.MONTH) {
            if (stockMonthInfoExecutor != null && stockMonthInfoExecutor.isShutdown()) {
                return false;
            }
            stockMonthInfoExecutor.shutdown();
            stockMonthInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        }
        return true;
    }

    public void initProcessData(List<StockBean> stockList, StockTaskTypeEnum stockTaskTypeEnum) {
        if (stockTaskTypeEnum == StockTaskTypeEnum.STOCK) {
            StockRunnable.totalCount = stockList.size();
            StockRunnable.updateCount = 0;
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.TIME) {
            StockTimeInfoRunnable.totalCount = stockList.size();
            StockTimeInfoRunnable.updateCount = 0;
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.DAY) {
            StockDayInfoRunnable.totalCount = stockList.size();
            StockDayInfoRunnable.updateCount = 0;
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.WEEK) {
            StockWeekInfoRunnable.totalCount = stockList.size();
            StockWeekInfoRunnable.updateCount = 0;
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.MONTH) {
            StockMonthInfoRunnable.totalCount = stockList.size();
            StockMonthInfoRunnable.updateCount = 0;
        }
    }

    public void stockUpdateTask(StockBean stockBean, StockTaskTypeEnum stockTaskTypeEnum) {
        if (stockTaskTypeEnum == StockTaskTypeEnum.STOCK) {
            if (stockExecutor.isShutdown()) {
                log.info("股票列表线程池被关闭，重新初始化线程池");
                stockExecutor = Executors.newFixedThreadPool(NTHREADS);
            }
            stockExecutor.execute(new StockRunnable(stockBean, stockDIService));
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.TIME) {
            if (stockTimeInfoExecutor.isShutdown()) {
                log.info("时间线线程池被关闭，重新初始化线程池");
                stockTimeInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
            }
            stockTimeInfoExecutor.execute(new StockTimeInfoRunnable(stockBean, stockDIService, stockTimeInfoService, abnormalaActionService, stockBidService));
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.DAY) {
            if (stockDayInfoExecutor.isShutdown()) {
                log.info("日线线程池被关闭，重新初始化线程池");
                stockDayInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
            }
            stockDayInfoExecutor.execute(new StockDayInfoRunnable(stockBean, stockDayInfoService, stockDIService, echnicalaspectService));
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.WEEK) {
            if (stockWeekInfoExecutor.isShutdown()) {
                log.info("周线线程池被关闭，重新初始化线程池");
                stockWeekInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
            }
            stockWeekInfoExecutor.execute(new StockWeekInfoRunnable(stockBean, stockWeekInfoService, stockDayInfoService));
        } else if (stockTaskTypeEnum == StockTaskTypeEnum.MONTH) {
            if (stockMonthInfoExecutor.isShutdown()) {
                log.info("月线线程池被关闭，重新初始化线程池");
                stockMonthInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
            }
            stockMonthInfoExecutor.execute(new StockMonthInfoRunnable(stockBean, stockMonthInfoService, stockDayInfoService));
        }
    }
}
