package com.qiwenshare.stock.executor;

import com.qiwenshare.stock.analysis.ReplayOperation;
import com.qiwenshare.stock.api.IReplayService;
import com.qiwenshare.stock.api.IStockDayInfoService;
import com.qiwenshare.stock.common.TaskProcess;
import com.qiwenshare.stock.controller.StockController;
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

    IStockDayInfoService stockDayInfoService;

    IReplayService replayService;

    public ReplayRunnable(StockBean stockBean, IReplayService replayService, IStockDayInfoService stockDayInfoService) {
        this.stockBean = stockBean;
        this.stockDayInfoService = stockDayInfoService;
        this.replayService = replayService;
    }

    @Override
    public void run() {
        if (!StockController.stockReplayexec.isShutdown()) {
            TaskProcess taskProcess = new TaskProcess();

            List<StockDayInfo> stockDayInfoList = stockDayInfoService.getStockdaybar(stockBean.getStockNum());
            Collections.reverse(stockDayInfoList);
            replayService.deleteReplay(stockBean.getStockNum());
            List<ReplayBean> replayList = new ReplayOperation().getReplayInfo(stockDayInfoList, stockBean);
            replayService.insertReplay(replayList);
            synchronized (StockController.class) {
                updateCount++;
            }


            taskProcess.setCompleteCount(updateCount);
            taskProcess.setTotalCount(totalCount);
            taskProcess.setTaskInfo("复盘项：" + stockBean.getStockNum() + "完成进度：" + updateCount + "/" + totalCount);
            taskProcess.setRunTask(totalCount != updateCount);
            StockWebsocket.pushTaskProcess(taskProcess);
        }
    }
}
