package com.qiwenshare.stock.controller;

import com.alibaba.fastjson2.JSON;
import com.qiwenshare.common.result.RestResult;
import com.qiwenshare.stock.analysis.ReplayOperation;
import com.qiwenshare.stock.api.*;
import com.qiwenshare.stock.common.HttpsUtils;
import com.qiwenshare.stock.common.TableData;
import com.qiwenshare.stock.common.TableQueryBean;
import com.qiwenshare.stock.common.TaskProcess;
import com.qiwenshare.stock.constant.StockTaskTypeEnum;
import com.qiwenshare.stock.domain.*;
import com.qiwenshare.stock.executor.ReplayRunnable;
import com.qiwenshare.stock.executor.StockExecutor;
import com.qiwenshare.stock.websocket.StockWebsocket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Tag(name = "股票")
@RequestMapping("/stock")
@RestController
public class StockController {
    public static int totalCount = 0;
    public static volatile int updateCount = 0;

    /**
     * 当前模块
     */

    private static final int NTHREADS = 5;
    public static ExecutorService stockReplayexec = Executors.newFixedThreadPool(NTHREADS);
    public static ExecutorService stockListexec = Executors.newFixedThreadPool(NTHREADS);
    @Resource
    IStockDIService stockDIService;
    @Resource
    IStockTimeInfoService stockTimeInfoService;
    @Resource
    IStockDayInfoService stockDayInfoService;
    @Resource
    IStockWeekInfoService stockWeekInfoService;
    @Resource
    IStockMonthInfoService stockMonthInfoService;
    @Resource
    StockExecutor stockExecutor;
    @Resource
    IStockBidService stockBidService;
    @Resource
    IStockOptionalService stockOptionalService;
    @Resource
    IReplayService replayService;
    @Resource
    IEchnicalaspectService echnicalaspectService;
    @Resource
    IAbnormalaActionService abnormalaActionService;

    @Operation(summary = "日线")
    @RequestMapping("/getstockdaybar")
    @ResponseBody
    public List<StockDayInfo> getStockdaybar(String stockNum) {
        List<StockDayInfo> stockdayList = stockDayInfoService.getStockdaybar(stockNum);
        Collections.reverse(stockdayList);
        return stockdayList;
    }

    @Operation(summary = "得到交易信息")
    @RequestMapping("/getstockbid")
    @ResponseBody
    public StockBidBean getStockBid(String stockNum) {
        StockBidBean stockBidBean = stockBidService.getStockBidBean(stockNum);
        return stockBidBean;
    }

    @Operation(summary = "分时线")
    @RequestMapping("/getstocktimebar")
    @ResponseBody
    public List<StockTimeInfo> getStocktimebar(String stockNum) {
        List<StockTimeInfo> stocktimeList = stockTimeInfoService.getStocktimebar(stockNum);
        Collections.reverse(stocktimeList);
        return stocktimeList;
    }

    @Operation(summary = "周线")
    @RequestMapping("/getstockweekbar")
    @ResponseBody
    public List<StockWeekInfo> getStockweekbar(String stockNum) {
        List<StockWeekInfo> stockweekList = stockWeekInfoService.getStockweekbar(stockNum);
        Collections.reverse(stockweekList);
        return stockweekList;
    }

    @Operation(summary = "月线")
    @RequestMapping("/getstockmonthbar")
    @ResponseBody
    public List<StockMonthInfo> getStockmonthbar(String stockNum) {
        List<StockMonthInfo> stockmonthList = stockMonthInfoService.getStockmonthbar(stockNum);
        Collections.reverse(stockmonthList);
        return stockmonthList;
    }

    @Operation(summary = "添加自选")
    @RequestMapping("/addStockOptional")
    @ResponseBody
    public RestResult<String> addStockOptional(StockOptionalBean stockOptionalBean) {
        RestResult<String> restResult = new RestResult<String>();
        stockOptionalService.insertStockOptional(stockOptionalBean);

        restResult.setSuccess(true);
        return restResult;
    }

    @Operation(summary = "查询回测数据")
    @RequestMapping("/selectreplaylist")
    @ResponseBody
    public String selectReplayList(String stockNum) {
        TableData<List<ReplayBean>> miniuiTableData = new TableData<List<ReplayBean>>();
        List<ReplayBean> replayBeanList = replayService.selectReplayList(stockNum);

        miniuiTableData.setData(replayBeanList);
        miniuiTableData.setSuccess(true);
        miniuiTableData.setCount(replayBeanList.size());

        return JSON.toJSONString(miniuiTableData);
    }

    @Operation(summary = "查询所有回测数据")
    @RequestMapping("/selectallreplaylist")
    @ResponseBody
    public TableData<List<ReplayBean>> selectAllReplayList(@RequestBody TableQueryBean tableQueryBean) {
        TableData<List<ReplayBean>> miniuiTableData = new TableData<List<ReplayBean>>();
        List<ReplayBean> replayBeanList = replayService.selectAllReplayList(tableQueryBean.getBeginCount(), tableQueryBean.getLimit());

        miniuiTableData.setData(replayBeanList);
        miniuiTableData.setSuccess(true);
        miniuiTableData.setCount(replayBeanList.size());

        return miniuiTableData;
    }

    @Operation(summary = "数据回测")
    @RequestMapping("/backtest")
    @ResponseBody
    public RestResult<String> backTest(@RequestBody StockBean stockBean) {
        RestResult<String> restResult = new RestResult<String>();
        List<StockDayInfo> stockDayInfoList = stockDayInfoService.getStockdaybar(stockBean.getStockNum());
        Collections.reverse(stockDayInfoList);
        replayService.deleteReplay(stockBean.getStockNum());
        List<ReplayBean> replayList = new ReplayOperation().getReplayInfo(stockDayInfoList, stockBean);
        replayService.insertReplay(replayList);
        restResult.setSuccess(true);
        return restResult;
    }

    @Operation(summary = "所有股票数据回测")
    @RequestMapping("/totalstockbacktest")
    @ResponseBody
    public RestResult<String> totalStockBackTest() {
        RestResult<String> restResult = new RestResult<String>();
        //1、获取所有股票列表
        List<StockBean> stocklist = stockDIService.selectTotalStockList();
        ReplayRunnable.totalCount = stocklist.size();
        ReplayRunnable.updateCount = 0;
        for (StockBean stockBean : stocklist) {
            Runnable task = new ReplayRunnable(stockBean, replayService, stockDayInfoService);
            stockReplayexec.execute(task);
        }

        restResult.setSuccess(true);
        return restResult;


    }

    @Operation(summary = "检测上证股票数据是否有需要更新")
    @RequestMapping("/checkstockisupdate")
    @ResponseBody
    public RestResult<String> checkStockIsUpdate() {
        RestResult<String> restResult = new RestResult<String>();
        List<StockBean> stockList = stockDIService.selectTotalStockList();
        List<StockBean> jsonArr = stockDIService.getStockListByScript();
        List<StockBean> stockBeanList = new ArrayList<StockBean>();
        for (int i = 0; i < jsonArr.size(); i++) {
            StockBean stockStr = jsonArr.get(i);
            stockBeanList.add(stockStr);
        }

        List<StockBean> stockBeanList1 = new ArrayList<>();
        for (int i = 0; i < stockBeanList.size(); i++) {
            StockBean stockBean = new StockBean();
            if (!stockList.contains(stockBeanList.get(i))) {
                stockBean.setStockNum(stockBeanList.get(i).getStockNum());
                stockBean.setStockName(stockBeanList.get(i).getStockName());
                stockBeanList1.add(stockBean);
            }
        }
        if (stockBeanList1.size() > 0) {
            restResult.setData(JSON.toJSONString(stockBeanList1));

            restResult.setSuccess(true);
            return restResult;
        }
        restResult.setData("暂无新增股票信息");

        restResult.setSuccess(false);
        return restResult;

    }

    @Operation(summary = "更新股票列表")
    @RequestMapping("/updatestocklist")
    @ResponseBody
    public RestResult<String> updateStockList(HttpServletRequest request) {

        RestResult<String> restResult = new RestResult<String>();
        List<StockBean> stockBeanList = stockDIService.getStockListByScript();
        //新增股票列表
        List<StockBean> newStockList = stockDIService.getNoExistStockList(stockBeanList);
        totalCount = newStockList.size();
        updateCount = 0;
        if (newStockList.size() > 0) {
            stockDIService.insertStockList(newStockList);
            for (StockBean stockBean : newStockList) {
                stockListexec.execute(new Runnable() {
                    @Override
                    public void run() {
                        TaskProcess taskProcess = new TaskProcess();
                        taskProcess.setTaskId(0);
                        synchronized (StockController.class) {
                            updateCount++;
                        }
                        taskProcess.setCompleteCount(updateCount);
                        taskProcess.setTotalCount(totalCount);

                        String stockNum = stockBean.getStockNum();
                        EchnicalaspectBean echnicalaspect = new EchnicalaspectBean(stockNum);
                        AbnormalactionBean abnormalactionBean = new AbnormalactionBean(stockNum);
                        StockBidBean stockBidBean = new StockBidBean(stockNum);
                        echnicalaspectService.insertEchnicalaspect(echnicalaspect);
                        abnormalaActionService.insertAbnormalaAction(abnormalactionBean);
                        stockBidService.insertStockBid(stockBidBean);
                        stockDIService.createStockInfoTable(stockBean.getStockNum());


                        taskProcess.setTaskInfo("股票列表更新,当前正在更新项为：" + stockBean.getStockName() +"(" + stockBean.getStockNum() + "), 完成进度：" + updateCount + "/" + totalCount);
                        taskProcess.setRunTask(totalCount != updateCount);
                        StockWebsocket.pushTaskProcess(taskProcess);
                    }
                });

            }

            restResult.setData("正在更新股票数量：" + newStockList.size());
        } else {
            restResult.setData("暂无可更新股票");
        }

        restResult.setSuccess(true);

        return restResult;
    }

    @Operation(summary = "查询股票列表信息")
    @RequestMapping("/getstocklist")
    @ResponseBody
    public RestResult getStockList(@Parameter(description = "当前页", required = false) long currentPage,
                               @Parameter(description = "页面数量", required = false) long pageCount,
                               @Parameter(description = "关键词", required = false) String key) {

        List<StockBean> stockList = null;
        int stockCount;
        if (currentPage == 0 || pageCount == 0) {
            stockCount = stockDIService.getStockCount(key, 0L, 10L);
            stockList = stockDIService.selectStockList(key, 0L, 10L);

        } else {
            long beginCount = (currentPage - 1) * pageCount;

            stockCount = stockDIService.getStockCount(key, beginCount, pageCount);
            stockList = stockDIService.selectStockList(key, beginCount, pageCount);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total", stockCount);
        map.put("list", stockList);
        return RestResult.success().data(map);
    }

    @Operation(summary = "获取技术面结果")
    @RequestMapping("/getechnicalaspect")
    @ResponseBody
    public EchnicalaspectBean getEchnicalaspect(String stockNum) {
        EchnicalaspectBean echnicalaspectBean = echnicalaspectService.getEchnicalaspectBean(stockNum);

        return echnicalaspectBean;
    }

    @Operation(summary = "获取异动")
    @RequestMapping("/getabnormalaction")
    @ResponseBody
    public AbnormalactionBean getAbnormalaction(String stockNum) {
        AbnormalactionBean abnormalactionBean = abnormalaActionService.getAbnormalactionBean(stockNum);

        return abnormalactionBean;
    }

    @Operation(summary = "获取股票信息通过Id")
    @RequestMapping("/getstockinfobyid")
    @ResponseBody
    public StockBean getStockInfoById(String stockId) {
        return stockDIService.getStockInfoById(stockId);
    }

    @Operation(summary = "更新股票详情")
    @RequestMapping("/updatestocktimeinfo")
    @ResponseBody
    public RestResult<String> updateStockTimeInfo() {
        RestResult<String> restResult = new RestResult<String>();
        //1、获取所有股票列表
        List<StockBean> stocklist = stockDIService.selectTotalStockList();
        stockExecutor.start(stocklist);

        restResult.setSuccess(true);
        return restResult;
    }

    @Operation(summary = "停止更新股票信息")
    @RequestMapping("/stopupdatetaskbytype")
    @ResponseBody
    public RestResult<String> stopUpdateTaskByType(int taskType) {
        RestResult<String> restResult = new RestResult<String>();

        StockTaskTypeEnum enum1 = null;

        try {
            Boolean isStopSuccess = stockExecutor.stop();
            if (isStopSuccess) {
                StockWebsocket.pushTaskState("任务已经停止", false);
                restResult.setData("任务停止成功");
            } else {
                StockWebsocket.pushTaskState("不要重复停止", false);
                restResult.setData("暂无任务可以停止");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StockWebsocket.pushTaskState("任务已经停止", false);

        restResult.setSuccess(true);
        return restResult;
    }

    @Operation(summary = "停止更新股票分时信息")
    @RequestMapping("/stopupdatestocktimedata")
    @ResponseBody
    public RestResult<String> stopUpdateStockTimeData() {
        RestResult<String> restResult = new RestResult<String>();

        try {
            Boolean isStopSuccess = new StockExecutor().stop();
            if (isStopSuccess) {
                StockWebsocket.pushTaskState("任务已经停止", false);
                restResult.setData("任务停止成功");
            } else {
                StockWebsocket.pushTaskState("不要重复停止，小心我打你", false);
                restResult.setData("暂无任务可以停止");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("停止成功！");

        restResult.setSuccess(true);
        return restResult;
    }

    @Operation(summary = "检测上证股票实时数据")
    @RequestMapping("/getshstock")
    @ResponseBody
    public String getShStock(){
        String result = "";
        String url = "http://yunhq.sse.com.cn:32041//v1/sh1/list/self/000001_000016_000010_000009_000300&select=code%2Cname%2Clast%2Cchg_rate%2Camount%2Copen%2Cprev_close&_=1585456053043";
        try {
            result = IOUtils.toString(HttpsUtils.doGet(url), "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
