package com.qiwenshare.stock.executor;

import com.qiwenshare.common.domain.TaskProcess;
import com.qiwenshare.stock.api.IStockDIService;
import com.qiwenshare.stock.api.IStockTimeInfoService;
import com.qiwenshare.stock.controller.StockDIController;
import com.qiwenshare.stock.domain.AbnormalactionBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockBidBean;
import com.qiwenshare.stock.domain.StockTimeInfo;
import com.qiwenshare.stock.indicator.product.Abnormalaction;
import com.qiwenshare.stock.websocket.StockWebsocket;

import java.util.List;

public class StockTimeInfoRunnable implements Runnable {

    public static volatile int updateCount = 0;
    public static int totalCount = 0;
    public StockBean stockBean;
    IStockDIService stockDIService;
    IStockTimeInfoService stockTimeInfoService;

    public StockTimeInfoRunnable(StockBean stockBean, IStockDIService stockDIService, IStockTimeInfoService stockTimeInfoService) {
        this.stockBean = stockBean;
        this.stockDIService = stockDIService;
        this.stockTimeInfoService = stockTimeInfoService;
    }

    @Override
    public void run() {
        if (!StockService.stockTimeInfoExecutor.isShutdown()) {
            List<StockTimeInfo> stockTimeInfoList = stockTimeInfoService.getStockTimeInfoListByStockBean(stockBean);
            StockBidBean stockBidBean = stockDIService.getBidByStockBean(stockBean);
            if (stockTimeInfoList == null) {
                synchronized (StockDIController.class) {
                    updateCount++;
                }
                return;
            }
            synchronized (StockDIController.class) {
                updateCount++;
            }
            stockTimeInfoService.insertStockTimeInfo("stocktimeinfo_" + stockBean.getStocknum(), stockTimeInfoList);
            AbnormalactionBean abnormalaction = new Abnormalaction().getAbnormalaction(stockTimeInfoList);
            abnormalaction.setStockid(stockBean.getStockid());
            stockDIService.updateStockBid(stockBidBean);
            stockDIService.updateAbnormalaAction(abnormalaction);


            TaskProcess taskProcess = new TaskProcess();
            taskProcess.setCompleteCount(updateCount);
            taskProcess.setTotalCount(totalCount);
            taskProcess.setTaskInfo("采集项：" + stockBean.getStocknum() + "完成进度：" + updateCount + "/" + totalCount);
            taskProcess.setRunTask(totalCount != updateCount);
            StockWebsocket.pushTaskProcess(taskProcess);

        }
    }
}