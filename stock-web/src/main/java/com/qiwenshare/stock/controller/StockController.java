package com.qiwenshare.stock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qiwenshare.common.cbb.HttpRequest;
import com.qiwenshare.common.cbb.RestResult;
import com.qiwenshare.common.domain.TableData;
import com.qiwenshare.common.domain.TableQueryBean;
import com.qiwenshare.stock.analysis.ReplayOperation;
import com.qiwenshare.stock.api.*;
import com.qiwenshare.stock.domain.*;
import com.qiwenshare.stock.executor.ReplayRunnable;
import com.qiwenshare.stock.executor.StockService;
import com.qiwenshare.stock.websocket.StockWebsocket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Tag(name = "股票")
@RequestMapping("/stock")
@RestController
public class StockController {

    /**
     * 当前模块
     */
    public static final String CURRENT_MODULE = "DI";
    private static final int NTHREADS = 5;
    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    public static ExecutorService stockReplayexec = Executors.newFixedThreadPool(NTHREADS);
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

    /**
     * 日线
     *
     * @return
     */
    @Operation(summary = "日线")
    @RequestMapping("/getstockdaybar")
    @ResponseBody
    public String getStockdaybar(String stockNum) {
        List<StockDayInfo> stockdayList = stockDayInfoService.getStockdaybar(stockNum);
        Collections.reverse(stockdayList);
        return JSON.toJSONString(stockdayList);
    }

    /**
     * 得到交易信息
     *
     * @return
     */
    @Operation(summary = "得到交易信息")
    @RequestMapping("/getstockbid")
    @ResponseBody
    public String getStockBid(String stockNum) {
        StockBidBean stockBidBean = stockDIService.getStockBidBean(stockNum);
        return JSON.toJSONString(stockBidBean);
    }

    /**
     * 分时线
     *
     * @return
     */
    @Operation(summary = "分时线")
    @RequestMapping("/getstocktimebar")
    @ResponseBody
    public String getStocktimebar(String stockNum) {
        List<StockTimeInfo> stocktimeList = stockTimeInfoService.getStocktimebar(stockNum);
        Collections.reverse(stocktimeList);
        return JSON.toJSONString(stocktimeList);
    }

    /**
     * 周线
     *
     * @return
     */
    @Operation(summary = "周线")
    @RequestMapping("/getstockweekbar")
    @ResponseBody
    public String getStockweekbar(String stockNum) {
        List<StockWeekInfo> stockweekList = stockWeekInfoService.getStockweekbar(stockNum);
        Collections.reverse(stockweekList);
        return JSON.toJSONString(stockweekList);
    }

    /**
     * 月线
     *
     * @return
     */
    @Operation(summary = "月线")
    @RequestMapping("/getstockmonthbar")
    @ResponseBody
    public String getStockmonthbar(String stockNum) {
        List<StockMonthInfo> stockmonthList = stockMonthInfoService.getStockmonthbar(stockNum);
        Collections.reverse(stockmonthList);
        return JSON.toJSONString(stockmonthList);
    }

    /**
     * 添加自选
     *
     * @return
     */
    @RequestMapping("/addStockOptional")
    @ResponseBody
    public String addStockOptional(StockOptionalBean stockOptionalBean) {
        RestResult<String> restResult = new RestResult<String>();
        stockDIService.insertStockOptional(stockOptionalBean);

        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);
    }

    /**
     * 查询回测数据
     *
     * @param stockid
     * @return
     */
    @Operation(summary = "查询回测数据")
    @RequestMapping("/selectreplaylist")
    @ResponseBody
    public String selectReplayList(int stockid) {
        TableData<List<ReplayBean>> miniuiTableData = new TableData<List<ReplayBean>>();
        List<ReplayBean> replayBeanList = stockDIService.selectReplayList(stockid);

        miniuiTableData.setData(replayBeanList);
        miniuiTableData.setSuccess(true);
        miniuiTableData.setCount(replayBeanList.size());

        return JSON.toJSONString(miniuiTableData);
    }

    /**
     * 查询所有回测数据
     *
     * @return
     */
    @Operation(summary = "查询所有回测数据")
    @RequestMapping("/selectallreplaylist")
    @ResponseBody
    public String selectAllReplayList(@RequestBody TableQueryBean tableQueryBean) {
        TableData<List<ReplayBean>> miniuiTableData = new TableData<List<ReplayBean>>();
        List<ReplayBean> replayBeanList = stockDIService.selectAllReplayList(tableQueryBean);

        miniuiTableData.setData(replayBeanList);
        miniuiTableData.setSuccess(true);
        miniuiTableData.setCount(replayBeanList.size());

        return JSON.toJSONString(miniuiTableData, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 数据回测
     *
     * @return
     */
    @Operation(summary = "数据回测")
    @RequestMapping("/backtest")
    @ResponseBody
    public String backTest(StockBean stockBean) {
        RestResult<String> restResult = new RestResult<String>();
        List<StockDayInfo> stockDayInfoList = stockDayInfoService.getStockdaybar(stockBean.getStocknum());
        Collections.reverse(stockDayInfoList);
        stockDIService.deleteReplay(stockBean.getStockid());
        List<ReplayBean> replayList = new ReplayOperation().getReplayInfo(stockDayInfoList, stockBean);
        stockDIService.insertReplay(replayList);
        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);
    }

    /**
     * 数据回测
     *
     * @return
     */
    @Operation(summary = "所有股票数据回测")
    @RequestMapping("/totalstockbacktest")
    @ResponseBody
    public String totalStockBackTest() {
        RestResult<String> restResult = new RestResult<String>();
        //1、获取所有股票列表
        List<StockBean> stocklist = stockDIService.selectTotalStockList();
        ReplayRunnable.totalCount = stocklist.size();
        ReplayRunnable.updateCount = 0;
        for (StockBean stockBean : stocklist) {
            Runnable task = new ReplayRunnable(stockBean, stockDIService, stockDayInfoService);
            stockReplayexec.execute(task);
        }

        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);


    }

    /**
     * 检测上证股票数据是否有需要更新
     *
     * @return
     */
    @Operation(summary = "检测上证股票数据是否有需要更新")
    @RequestMapping("/checkstockisupdate")
    @ResponseBody
    public String checkStockIsUpdate() {
        RestResult<String> restResult = new RestResult<String>();
        List<StockBean> stockList = stockDIService.selectTotalStockList();
        List<StockBean> jsonArr = stockDIService.getStockListByScript();
        List<StockBean> stockBeanList = new ArrayList<StockBean>();
        for (int i = 0; i < jsonArr.size(); i++) {
            StockBean stockStr = jsonArr.get(i);
            stockStr.setStocknum(stockStr.getCOMPANY_CODE());
            stockStr.setStockname(stockStr.getCOMPANY_ABBR());
            stockBeanList.add(stockStr);
        }

        List<StockBean> stockBeanList1 = new ArrayList<>();
        for (int i = 0; i < stockBeanList.size(); i++) {
            StockBean stockBean = new StockBean();
            if (!stockList.contains(stockBeanList.get(i))) {
                stockBean.setStocknum(stockBeanList.get(i).getStocknum());
                stockBean.setStockname(stockBeanList.get(i).getStockname());
                stockBeanList1.add(stockBean);
            }
        }
        if (stockBeanList1.size() > 0) {
            restResult.setData(JSON.toJSONString(stockBeanList1));

            restResult.setSuccess(true);
            return JSON.toJSONString(restResult);
        }
        restResult.setData("暂无新增股票信息");

        restResult.setSuccess(false);
        return JSON.toJSONString(restResult);

    }

    /**
     * 更新股票列表
     *
     * @param request
     * @return
     */
    @Operation(summary = "更新股票列表")
    @RequestMapping("/updatestocklist")
    @ResponseBody
    public String updateStockList(HttpServletRequest request) {
        RestResult<String> restResult = new RestResult<String>();
        List<StockBean> stockBeanList = stockDIService.getStockListByScript();
        //新增股票列表
        List<StockBean> newStockList = stockDIService.getNoExistStockList(stockBeanList);

        if (newStockList.size() > 0) {
            stockDIService.insertStockList(newStockList);
            new StockService(stockDIService).start(newStockList);

            stockDIService.initStockTable();
            restResult.setData("正在更新股票数量：" + newStockList.size());
        } else {
            restResult.setData("暂无可更新股票");
        }

        restResult.setSuccess(true);

        return JSON.toJSONString(restResult);
    }

    /**
     * 获取股票列表
     *
     * @return
     */
    @Operation(summary = "获取股票列表")
    @RequestMapping("/getstocklist")
    @ResponseBody
    public String getStockList(@RequestBody TableQueryBean tableQueryBean) {
        TableData<List<StockBean>> miniuiTableData = new TableData<List<StockBean>>();
        List<StockBean> stockList = stockDIService.selectStockList(tableQueryBean);
        int StockCount = stockDIService.getStockCountBySelect(tableQueryBean);
        miniuiTableData.setData(stockList);
        miniuiTableData.setSuccess(true);
        miniuiTableData.setCount(StockCount);

        return JSON.toJSONString(miniuiTableData);
    }

    /**
     * 获取技术面结果
     *
     * @return
     */
    @Operation(summary = "获取技术面结果")
    @RequestMapping("/getechnicalaspect")
    @ResponseBody
    public String getEchnicalaspect(int stockid) {
        EchnicalaspectBean echnicalaspectBean = stockDIService.getEchnicalaspectBean(stockid);

        return JSON.toJSONString(echnicalaspectBean);
    }

    /**
     * 获取异动
     *
     * @return
     */
    @Operation(summary = "获取异动")
    @RequestMapping("/getabnormalaction")
    @ResponseBody
    public String getAbnormalaction(int stockid) {
        AbnormalactionBean abnormalactionBean = stockDIService.getAbnormalactionBean(stockid);

        return JSON.toJSONString(abnormalactionBean);
    }

    /**
     * 获取股票信息通过Id
     *
     * @return
     */
    @Operation(summary = "获取股票信息通过Id")
    @RequestMapping("/getstockinfobyid")
    @ResponseBody
    public String getStockInfoById(String stockId) {
        return JSON.toJSONString(stockDIService.getStockInfoById(stockId));
    }


    /**
     * 更新分时数据
     *
     * @return
     */
    @Operation(summary = "更新分时数据")
    @RequestMapping("/updatestocktimeinfo")
    @ResponseBody
    public String updateStockTimeInfo() {
        RestResult<String> restResult = new RestResult<String>();
        //1、获取所有股票列表
        List<StockBean> stocklist = stockDIService.selectTotalStockList();

        new StockService(stockDIService, stockTimeInfoService).start(stocklist);

        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);
    }

    /**
     * 更新日线数据
     *
     * @return
     */
    @Operation(summary = "更新日线数据")
    @RequestMapping("/updatestockdayinfo")
    @ResponseBody
    public String updateStockDayInfo() {
        RestResult<String> restResult = new RestResult<String>();
        logger.info("updateStockDayInfo start...");

        //1、获取所有股票列表
        List<StockBean> stocklist = stockDIService.selectTotalStockList();
        new StockService(stockDIService, stockDayInfoService).start(stocklist);


        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);
    }

    /**
     * 更新周线数据
     *
     * @return
     */
    @Operation(summary = "更新周线数据")
    @RequestMapping("/updatestockweekinfo")
    @ResponseBody
    public String updateStockWeekInfo() {
        RestResult<String> restResult = new RestResult<String>();
        //1、获取所有股票列表
        List<StockBean> stocklist = stockDIService.selectTotalStockList();

        new StockService(stockDayInfoService, stockWeekInfoService).start(stocklist);
        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);
    }

    /**
     * 更新月线数据
     *
     * @return
     */
    @Operation(summary = "更新月线数据")
    @RequestMapping("/updatestockmonthinfo")
    @ResponseBody
    public String updateStockMonthInfo() {
        RestResult<String> restResult = new RestResult<String>();
        //exec = Executors.newFixedThreadPool(NTHREADS);
        //1、获取所有股票列表
        List<StockBean> stocklist = stockDIService.selectTotalStockList();
        new StockService(stockDayInfoService, stockMonthInfoService).start(stocklist);
        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);
    }

    /**
     * 停止更新股票信息
     *
     * @return
     */
    @Operation(summary = "停止更新股票信息")
    @RequestMapping("/stopupdatestockdata")
    @ResponseBody
    public String stopUpdateStockData() {
        RestResult<String> restResult = new RestResult<String>();

        try {
            Boolean isStopSuccess = new StockService().stop(0);
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

        StockWebsocket.pushTaskState("任务已经停止", false);

        System.out.println("停止成功！");
        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);
    }

    /**
     * 停止更新股票分时信息
     *
     * @return
     */
    @Operation(summary = "停止更新股票分时信息")
    @RequestMapping("/stopupdatestocktimedata")
    @ResponseBody
    public String stopUpdateStockTimeData() {
        RestResult<String> restResult = new RestResult<String>();

        try {
            Boolean isStopSuccess = new StockService().stop(1);
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
        return JSON.toJSONString(restResult);
    }

    /**
     * 通过过滤条件查询股票信息
     *
     * @return 返回结果
     */
    @Operation(summary = "通过过滤条件查询股票信息")
    @RequestMapping("/querystockbyfilter")
    @ResponseBody
    public String queryStockByFilter(String filter) {
        RestResult<List<StockBean>> restResult = new RestResult<List<StockBean>>();

        List<StockBean> stockBean = stockDIService.selectStockBeanList(filter);
        restResult.setData(stockBean);
        restResult.setSuccess(true);
        return JSON.toJSONString(restResult);
    }

    /**
     * 检测上证股票实时数据
     *
     * @return
     */
    @Operation(summary = "检测上证股票实时数据")
    @RequestMapping("/getshstock")
    @ResponseBody
    public String getShStock(){
        String result = HttpRequest.sendGet("http://yunhq.sse.com.cn:32041//v1/sh1/list/self/000001_000016_000010_000009_000300","select=code%2Cname%2Clast%2Cchg_rate%2Camount%2Copen%2Cprev_close&_=1585456053043");
        return result;
    }

}
