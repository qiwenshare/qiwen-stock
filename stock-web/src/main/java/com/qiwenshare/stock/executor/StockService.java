package com.qiwenshare.stock.executor;

import com.qiwenshare.stock.api.*;
import com.qiwenshare.stock.domain.StockBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StockService {
    private static final int NTHREADS = 5;
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
    private int taskType;

    public StockService() {

    }

    public StockService(IStockDIService stockDIService) {
        stockExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDIService = stockDIService;
        taskType = 0;
    }

    public StockService(IStockDIService stockDIService, IStockTimeInfoService stockTimeInfoService) {
        stockTimeInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDIService = stockDIService;
        this.stockTimeInfoService = stockTimeInfoService;
        taskType = 1;
    }

    public StockService(IStockDIService stockDIService, IStockDayInfoService stockDayInfoService) {
        stockDayInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDIService = stockDIService;
        this.stockDayInfoService = stockDayInfoService;
        taskType = 2;
    }

    public StockService(IStockDayInfoService stockDayInfoService, IStockWeekInfoService stockWeekInfoService) {
        stockWeekInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDayInfoService = stockDayInfoService;
        this.stockWeekInfoService = stockWeekInfoService;
        taskType = 3;
    }

    public StockService(IStockDayInfoService stockDayInfoService, IStockMonthInfoService stockMonthInfoService) {
        stockMonthInfoExecutor = Executors.newFixedThreadPool(NTHREADS);
        this.stockDayInfoService = stockDayInfoService;
        this.stockMonthInfoService = stockMonthInfoService;
        taskType = 4;
    }

    public boolean start(List<StockBean> stockList) {
        //初始化进度数据
        initProcessData(stockList);


        for (StockBean stockBean : stockList) {
            stockUpdateTask(stockBean);
        }
        return true;
    }

    public boolean stop(int taskType) throws InterruptedException {
        if (taskType == 0) {
            if (stockExecutor != null && stockExecutor.isShutdown()) {
                return false;
            }
            stockExecutor.shutdown();
            stockExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (taskType == 1) {
            if (stockTimeInfoExecutor != null && stockTimeInfoExecutor.isShutdown()) {
                return false;
            }
            stockTimeInfoExecutor.shutdown();
            stockTimeInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (taskType == 2) {
            if (stockDayInfoExecutor != null && stockDayInfoExecutor.isShutdown()) {
                return false;
            }
            stockDayInfoExecutor.shutdown();
            stockDayInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (taskType == 3) {
            if (stockWeekInfoExecutor != null && stockWeekInfoExecutor.isShutdown()) {
                return false;
            }
            stockWeekInfoExecutor.shutdown();
            stockWeekInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } else if (taskType == 4) {
            if (stockMonthInfoExecutor != null && stockMonthInfoExecutor.isShutdown()) {
                return false;
            }
            stockMonthInfoExecutor.shutdown();
            stockMonthInfoExecutor.awaitTermination(3, TimeUnit.SECONDS);
        }
        return true;
    }

    public void initProcessData(List<StockBean> stockList) {
        if (taskType == 0) {
            StockRunnable.totalCount = stockList.size();
            StockRunnable.updateCount = 0;
        } else if (taskType == 1) {
            StockTimeInfoRunnable.totalCount = stockList.size();
            StockTimeInfoRunnable.updateCount = 0;
        } else if (taskType == 2) {
            StockDayInfoRunnable.totalCount = stockList.size();
            StockDayInfoRunnable.updateCount = 0;
        } else if (taskType == 3) {
            StockWeekInfoRunnable.totalCount = stockList.size();
            StockWeekInfoRunnable.updateCount = 0;
        } else if (taskType == 4) {
            StockMonthInfoRunnable.totalCount = stockList.size();
            StockMonthInfoRunnable.updateCount = 0;
        }
    }

    public void stockUpdateTask(StockBean stockBean) {
        if (taskType == 0) {
            stockExecutor.execute(new StockRunnable(stockBean, stockDIService));
        } else if (taskType == 1) {
            stockTimeInfoExecutor.execute(new StockTimeInfoRunnable(stockBean, stockDIService, stockTimeInfoService));
        } else if (taskType == 2) {
            stockDayInfoExecutor.execute(new StockDayInfoRunnable(stockBean, stockDayInfoService, stockDIService));
        } else if (taskType == 3) {
            stockWeekInfoExecutor.execute(new StockWeekInfoRunnable(stockBean, stockWeekInfoService, stockDayInfoService));
        } else if (taskType == 4) {
            stockMonthInfoExecutor.execute(new StockMonthInfoRunnable(stockBean, stockMonthInfoService, stockDayInfoService));
        }
    }
}
