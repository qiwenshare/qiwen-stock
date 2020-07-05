package com.qiwenshare.stock.executor;

import com.qiwenshare.common.domain.TaskProcess;
import com.qiwenshare.stock.api.IStockDIService;
import com.qiwenshare.stock.controller.StockController;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.websocket.StockWebsocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StockRunnable.class);
    public static volatile int updateCount = 0;

    public static int totalCount = 0;

    public StockBean stockBean;
    IStockDIService stockDIService;

    public StockRunnable(StockBean stockBean, IStockDIService stockDIService) {
        this.stockBean = stockBean;
        this.stockDIService = stockDIService;
    }

    @Override
    public void run() {
        logger.info("StockRunnable run start");
        if (!StockService.stockExecutor.isShutdown()) {
            TaskProcess taskProcess = new TaskProcess();

            synchronized (StockController.class) {
                updateCount++;
            }
            stockDIService.createStockInfoTable(stockBean.getStocknum());


            taskProcess.setCompleteCount(updateCount);
            taskProcess.setTotalCount(totalCount);
            taskProcess.setTaskInfo("采集项：" + stockBean.getStocknum() + "完成进度：" + updateCount + "/" + totalCount);
            taskProcess.setRunTask(totalCount != updateCount);
            StockWebsocket.pushTaskProcess(taskProcess);
        }

    }
}
