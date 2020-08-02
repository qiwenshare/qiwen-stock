package com.qiwenshare.stock.executor;

import com.qiwenshare.stock.api.*;
import com.qiwenshare.stock.constant.StockTaskTypeEnum;
import com.qiwenshare.stock.domain.StockBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StockService {
    private static final int NTHREADS = 40;
    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    public static ExecutorService stockExecutor;
    public static ExecutorService stockDayInfoExecutor;
    public static ExecutorService stockTimeInfoExecutor;
    public static ExecutorService stockWeekInfoExecutor;
    public static ExecutorService stockMonthInfoExecutor;
    private IStockDIService stockDIService;
    private IStockTimeInfoService stockTimeInfoService;
    private IStockDayInfoService stockDayInfoService;
    private IStockWeekInfoService stockWeekInfoService;
    private IStockMonthInfoService stockMonthInfoService;
    private StockTaskTypeEnum taskType;

    public StockService() {

    }

    public StockService(IStockDIService stockDIService) {
        stockExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDIService = stockDIService;
        taskType = StockTaskTypeEnum.STOCK;
    }

    public StockService(IStockDIService stockDIService, IStockTimeInfoService stockTimeInfoService) {
        stockTimeInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDIService = stockDIService;
        this.stockTimeInfoService = stockTimeInfoService;
        taskType = StockTaskTypeEnum.TIME;
    }

    public StockService(IStockDIService stockDIService, IStockDayInfoService stockDayInfoService) {
        stockDayInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDIService = stockDIService;
        this.stockDayInfoService = stockDayInfoService;
        taskType = StockTaskTypeEnum.DAY;
    }

    public StockService(IStockDayInfoService stockDayInfoService, IStockWeekInfoService stockWeekInfoService) {
        stockWeekInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDayInfoService = stockDayInfoService;
        this.stockWeekInfoService = stockWeekInfoService;
        taskType = StockTaskTypeEnum.WEEK;
    }

    public StockService(IStockDayInfoService stockDayInfoService, IStockMonthInfoService stockMonthInfoService) {
        stockMonthInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDayInfoService = stockDayInfoService;
        this.stockMonthInfoService = stockMonthInfoService;
        taskType = StockTaskTypeEnum.MONTH;
    }

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
     * @param taskType
     * @return
     * @throws InterruptedException
     */
    public boolean stop(int taskType) throws InterruptedException {
        if (taskType == StockTaskTypeEnum.STOCK.getTypeCode()) {
            if (stockExecutor != null && stockExecutor.isShutdown()) {
                return false;
            }
            stockExecutor.shutdown();
            stockExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (taskType == StockTaskTypeEnum.TIME.getTypeCode()) {
            if (stockTimeInfoExecutor != null && stockTimeInfoExecutor.isShutdown()) {
                return false;
            }
            stockTimeInfoExecutor.shutdown();
            stockTimeInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (taskType == StockTaskTypeEnum.DAY.getTypeCode()) {
            if (stockDayInfoExecutor != null && stockDayInfoExecutor.isShutdown()) {
                return false;
            }
            stockDayInfoExecutor.shutdown();
            stockDayInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (taskType == StockTaskTypeEnum.WEEK.getTypeCode()) {
            if (stockWeekInfoExecutor != null && stockWeekInfoExecutor.isShutdown()) {
                return false;
            }
            stockWeekInfoExecutor.shutdown();
            stockWeekInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (taskType == StockTaskTypeEnum.MONTH.getTypeCode()) {
            if (stockMonthInfoExecutor != null && stockMonthInfoExecutor.isShutdown()) {
                return false;
            }
            stockMonthInfoExecutor.shutdown();
            stockMonthInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        }
        return true;
    }

    public void initProcessData(List<StockBean> stockList) {
        if (taskType == StockTaskTypeEnum.STOCK) {
            StockRunnable.totalCount = stockList.size();
            StockRunnable.updateCount = 0;
        } else if (taskType == StockTaskTypeEnum.TIME) {
            StockTimeInfoRunnable.totalCount = stockList.size();
            StockTimeInfoRunnable.updateCount = 0;
        } else if (taskType == StockTaskTypeEnum.DAY) {
            StockDayInfoRunnable.totalCount = stockList.size();
            StockDayInfoRunnable.updateCount = 0;
        } else if (taskType == StockTaskTypeEnum.WEEK) {
            StockWeekInfoRunnable.totalCount = stockList.size();
            StockWeekInfoRunnable.updateCount = 0;
        } else if (taskType == StockTaskTypeEnum.MONTH) {
            StockMonthInfoRunnable.totalCount = stockList.size();
            StockMonthInfoRunnable.updateCount = 0;
        }
    }

    public void stockUpdateTask(StockBean stockBean) {
        if (taskType == StockTaskTypeEnum.STOCK) {
            stockExecutor.execute(new StockRunnable(stockBean, stockDIService));
        } else if (taskType == StockTaskTypeEnum.TIME) {
            stockTimeInfoExecutor.execute(new StockTimeInfoRunnable(stockBean, stockDIService, stockTimeInfoService));
        } else if (taskType == StockTaskTypeEnum.DAY) {
            stockDayInfoExecutor.execute(new StockDayInfoRunnable(stockBean, stockDayInfoService, stockDIService));
        } else if (taskType == StockTaskTypeEnum.WEEK) {
            stockWeekInfoExecutor.execute(new StockWeekInfoRunnable(stockBean, stockWeekInfoService, stockDayInfoService));
        } else if (taskType == StockTaskTypeEnum.MONTH) {
            stockMonthInfoExecutor.execute(new StockMonthInfoRunnable(stockBean, stockMonthInfoService, stockDayInfoService));
        }
    }
}
