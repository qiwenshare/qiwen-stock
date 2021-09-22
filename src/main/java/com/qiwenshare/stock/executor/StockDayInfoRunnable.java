package com.qiwenshare.stock.executor;

import com.alibaba.fastjson.JSON;
import com.qiwenshare.common.util.DateUtil;
import com.qiwenshare.stock.api.IEchnicalaspectService;
import com.qiwenshare.stock.api.IStockDIService;
import com.qiwenshare.stock.api.IStockDayInfoService;
import com.qiwenshare.stock.common.TaskProcess;
import com.qiwenshare.stock.controller.StockController;
import com.qiwenshare.stock.domain.EchnicalaspectBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.indicator.proxy.IndicatorProxy;
import com.qiwenshare.stock.websocket.StockWebsocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.List;

public class StockDayInfoRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StockDayInfoRunnable.class);
    public static volatile int updateCount = 0;
    public static int totalCount = 0;
    public StockBean stockBean;

    IStockDIService stockDIService;

    IStockDayInfoService stockDayInfoService;

    IEchnicalaspectService echnicalaspectService;

    public StockDayInfoRunnable(StockBean stockBean,
                                IStockDayInfoService stockDayInfoService,
                                IStockDIService stockDIService,
                                IEchnicalaspectService echnicalaspectService) {
        this.stockBean = stockBean;
        this.stockDIService = stockDIService;
        this.stockDayInfoService = stockDayInfoService;
        this.echnicalaspectService = echnicalaspectService;
    }

    @Override
    public void run() {
        logger.info("StockDayInfoRunnable run start");
        if (!StockService.stockDayInfoExecutor.isShutdown()) {
            TaskProcess taskProcess = new TaskProcess();

            Date updateDate = stockBean.getUpdateDate();
            List<StockDayInfo> stockDayInfoList1 = stockDayInfoService.crawlStockDayInfoListByStockBean(stockBean);
            List<StockDayInfo> stockDayInfoList;
            if (updateDate == null) {
                stockDayInfoList = stockDayInfoList1;
            } else {
                stockDayInfoList = stockDayInfoService.selectStockDayInfoList(stockBean);

                Date currentDate = stockDayInfoList1.get(stockDayInfoList1.size() - 1).getDate();
                int differentDay = DateUtil.getDifferentDays(updateDate, currentDate);
                List<StockDayInfo> updateStockDayInfo = stockDayInfoList1.subList(stockDayInfoList1.size() - differentDay, stockDayInfoList1.size());
                stockDayInfoList.addAll(updateStockDayInfo);
            }

            stockDayInfoList = new IndicatorProxy().getDayIndicatorList(stockDayInfoList);
            if (stockDayInfoList == null) {
                synchronized (StockController.class) {
                    updateCount++;
                }
                return;
            }
            synchronized (StockController.class) {
                updateCount++;
            }
            try {
                EchnicalaspectBean echnicalaspectBean = echnicalaspectService.getEchnicalaspectInfo(stockDayInfoList, stockBean);
                StockBean stockInfo = stockDIService.getStockInfo(stockBean, stockDayInfoList);
                logger.error("stockInfo：{}", JSON.toJSONString(stockInfo));
                echnicalaspectService.updateEchnicalaspect(echnicalaspectBean);
                stockDIService.updateStock(stockInfo);
                stockDayInfoService.insertStockDayInfo(stockBean, stockDayInfoList);
            } catch (Exception e) {

                logger.error("updateDayInfo error: {}", e);
            }
            taskProcess.setTaskId(1);
            taskProcess.setTaskName("更新日线任务");
            taskProcess.setCompleteCount(updateCount);
            taskProcess.setTotalCount(totalCount);
            taskProcess.setTaskInfo("采集项：" + stockBean.getStocknum() + "-" + stockBean.getStockname() + ", 当前进度：" + updateCount + "/" + totalCount);
            taskProcess.setRunTask(totalCount != updateCount);
            StockWebsocket.pushTaskProcess(taskProcess);

        }

    }
}
