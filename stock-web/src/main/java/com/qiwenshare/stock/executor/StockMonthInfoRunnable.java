package com.qiwenshare.stock.executor;

import com.qiwenshare.common.domain.TaskProcess;
import com.qiwenshare.stock.api.IStockDayInfoService;
import com.qiwenshare.stock.api.IStockMonthInfoService;
import com.qiwenshare.stock.controller.StockDIController;
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


            List<StockDayInfo> stockDayInfoList = stockDayInfoService.getStockdaybar(stockBean.getStocknum());
            List<StockMonthInfo> stockMonthInfoList = stockMonthInfoService.getStockMonthInfoList(stockDayInfoList);

            if (stockMonthInfoList == null) {
                synchronized (StockDIController.class) {
                    updateCount++;
                }
                return;
            }
            synchronized (StockDIController.class) {
                updateCount++;
            }

            stockMonthInfoService.insertStockMonthInfo("stockmonthinfo_" + stockBean.getStocknum(), stockMonthInfoList);


            taskProcess.setCompleteCount(updateCount);
            taskProcess.setTotalCount(totalCount);
            taskProcess.setTaskInfo("采集项：" + stockBean.getStocknum() + "完成进度：" + updateCount + "/" + totalCount);
            taskProcess.setRunTask(totalCount != updateCount);
            StockWebsocket.pushTaskProcess(taskProcess);
        }
    }
}