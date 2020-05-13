package com.qiwenshare.stock.executor;

import com.qiwenshare.common.domain.TaskProcess;
import com.qiwenshare.stock.analysis.ReplayOperation;
import com.qiwenshare.stock.api.IStockDIService;
import com.qiwenshare.stock.api.IStockDayInfoService;
import com.qiwenshare.stock.controller.StockDIController;
import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.websocket.StockWebsocket;

import java.util.Collections;
import java.util.List;

public class ReplayRunnable implements Runnable {

    public static volatile int updateCount = 0;
    public static int totalCount = 0;
    public StockBean stockBean;
    IStockDIService stockDIService;
    IStockDayInfoService stockDayInfoService;

    public ReplayRunnable(StockBean stockBean, IStockDIService stockDIService, IStockDayInfoService stockDayInfoService) {
        this.stockBean = stockBean;
        this.stockDIService = stockDIService;
        this.stockDayInfoService = stockDayInfoService;
    }

    @Override
    public void run() {
        if (!StockDIController.stockReplayexec.isShutdown()) {
            TaskProcess taskProcess = new TaskProcess();

            List<StockDayInfo> stockDayInfoList = stockDayInfoService.getStockdaybar(stockBean.getStocknum());
            Collections.reverse(stockDayInfoList);
            stockDIService.deleteReplay(stockBean.getStockid());
            List<ReplayBean> replayList = new ReplayOperation().getReplayInfo(stockDayInfoList, stockBean);
            stockDIService.insertReplay(replayList);
            synchronized (StockDIController.class) {
                updateCount++;
            }


            taskProcess.setCompleteCount(updateCount);
            taskProcess.setTotalCount(totalCount);
            taskProcess.setTaskInfo("复盘项：" + stockBean.getStocknum() + "完成进度：" + updateCount + "/" + totalCount);
            taskProcess.setRunTask(totalCount != updateCount);
            StockWebsocket.pushTaskProcess(taskProcess);
        }
    }
}
