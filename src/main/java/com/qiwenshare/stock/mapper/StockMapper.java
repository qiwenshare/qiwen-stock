package com.qiwenshare.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiwenshare.stock.common.TableQueryBean;
import com.qiwenshare.stock.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StockMapper  extends BaseMapper<StockBean> {

    void insertStockDayInfo(Map<String, Object> stockDayInfoMap);

    void insertStockWeekInfo(Map<String, Object> stockWeekInfoMap);

    void insertStockMonthInfo(Map<String, Object> stockMonthInfoMap);

    void insertStockTimeInfo(Map<String, Object> stockDayInfoMap);

    void insertStockList(List<StockBean> stockBeanList);

    List<StockBean> selectStockList(@Param("key") String key, @Param("beginCount") Long beginCount, @Param("pageCount") Long pageCount);

    int getStockCount(@Param("key") String key, @Param("beginCount") Long beginCount, @Param("pageCount") Long pageCount);

    List<StockDayInfo> selectStockDayInfoList(StockBean stockBean);

    List<StockTimeInfo> selectStocktimeListByStockNum(StockTimeInfo stockTimeInfo);

    List<StockDayInfo> getStockDayInfoBySelect(TableQueryBean miniuiTableQueryBean);

    int getStockDayInfoCountBySelect(TableQueryBean miniuiTableQueryBean);

    List<StockBean> selectTotalStockList();

    void createStockDayInfoTable(String stockDayInfoTableName);

    StockBean getStockInfoById(String stockId);

    void createStockTimeInfoTable(String stockTimeInfoTableName);

    void createStockWeekInfoTable(String stockWeekInfoTableName);

    void createStockMonthInfoTable(String stockMonthInfoTableName);

    public List<StockWeekInfo> selectStockweekListByStockNum(StockBean stockBean);

    List<StockMonthInfo> selectStockmonthListByStockNum(StockBean stockBean);
}
