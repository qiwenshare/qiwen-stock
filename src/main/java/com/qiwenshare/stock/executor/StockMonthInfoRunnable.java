package com.qiwenshare.stock.executor;

import com.qiwenshare.stock.api.IStockDayInfoService;
import com.qiwenshare.stock.api.IStockMonthInfoService;
import com.qiwenshare.stock.common.TaskProcess;
import com.qiwenshare.stock.controller.StockController;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockMonthInfo;
import com.qiwenshare.stock.websocket.StockWebsocket;

import java.util.List;

public class StockMonthInfoRunnable implements Runnable {

    public static volatile int updateCount = 0;
    public static int totalCount = 0;
    public StockBean stockBean;
    IStockDayInfoService stockDayInfoService;
    IStockMonthInfoService stockMonthInfoService;

    public StockMonthInfoRunnable(StockBean stockBean, IStockMonthInfoService stockMonthInfoService, IStockDayInfoService stockDayInfoService) {
        this.stockBean = stockBean;
        this.stockDayInfoService = stockDayInfoService;
        this.stockMonthInfoService = stockMonthInfoService;
    }

    @Override
    public void run() {
        if (!StockService.stockMonthInfoExecutor.isShutdown()) {
            TaskProcess taskProcess = new TaskProcess();


            List<StockDayInfo> stockDayInfoList = stockDayInfoService.getStockdaybar(stockBean.getStockNum());
            List<StockMonthInfo> stockMonthInfoList = stockMonthInfoService.getStockMonthInfoList(stockDayInfoList);

            if (stockMonthInfoList == null) {
                synchronized (StockController.class) {
                    updateCount++;
                }
                return;
            }
            synchronized (StockController.class) {
                updateCount++;
            }

            stockMonthInfoService.insertStockMonthInfo("stockmonthinfo_" + stockBean.getStockNum(), stockMonthInfoList);

            taskProcess.setTaskId(3);
            taskProcess.setTaskName("更新月线任务");
            taskProcess.setCompleteCount(updateCount);
            taskProcess.setTotalCount(totalCount);
            taskProcess.setTaskInfo("采集项：" + stockBean.getStockNum() + "-" + stockBean.getStockName() + ", 当前进度：" + updateCount + "/" + totalCount);
            taskProcess.setRunTask(totalCount != updateCount);
            StockWebsocket.pushTaskProcess(taskProcess);
        }
    }
}
