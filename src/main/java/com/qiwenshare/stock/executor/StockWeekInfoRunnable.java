package com.qiwenshare.stock.executor;

import com.qiwenshare.stock.api.IStockDayInfoService;
import com.qiwenshare.stock.api.IStockWeekInfoService;
import com.qiwenshare.stock.common.TaskProcess;
import com.qiwenshare.stock.controller.StockController;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.domain.StockWeekInfo;
import com.qiwenshare.stock.websocket.StockWebsocket;

import java.util.List;

public class StockWeekInfoRunnable implements Runnable {

    public static volatile int updateCount = 0;
    public static int totalCount = 0;
    public StockBean stockBean;
    IStockDayInfoService stockDayInfoService;
    IStockWeekInfoService stockWeekInfoService;

    public StockWeekInfoRunnable(StockBean stockBean, IStockWeekInfoService stockWeekInfoService, IStockDayInfoService stockDayInfoService) {
        this.stockBean = stockBean;
        this.stockDayInfoService = stockDayInfoService;
        this.stockWeekInfoService = stockWeekInfoService;
    }

    @Override
    public void run() {
        if (!StockService.stockWeekInfoExecutor.isShutdown()) {
            TaskProcess taskProcess = new TaskProcess();

            List<StockDayInfo> stockDayInfoList = stockDayInfoService.getStockdaybar(stockBean.getStocknum());
            List<StockWeekInfo> stockWeekInfoList = stockWeekInfoService.getStockWeekInfoList(stockDayInfoList);

            if (stockWeekInfoList == null) {
                synchronized (StockController.class) {
                    updateCount++;
                }
                return;
            }
            synchronized (StockController.class) {
                updateCount++;
            }

            stockWeekInfoService.insertStockWeekInfo("stockweekinfo_" + stockBean.getStocknum(), stockWeekInfoList);

            taskProcess.setTaskId(2);
            taskProcess.setTaskName("更新周线任务");
            taskProcess.setCompleteCount(updateCount);
            taskProcess.setTotalCount(totalCount);
            taskProcess.setTaskInfo("采集项：" + stockBean.getStocknum() + "-" + stockBean.getStockname() + ", 当前进度：" + updateCount + "/" + totalCount);
            taskProcess.setRunTask(totalCount != updateCount);
            StockWebsocket.pushTaskProcess(taskProcess);
        }
    }
}
