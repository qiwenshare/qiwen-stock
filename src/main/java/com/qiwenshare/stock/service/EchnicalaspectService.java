package com.qiwenshare.stock.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qiwenshare.common.util.DateUtil;
import com.qiwenshare.stock.analysis.tactics.CattleCatchingAnalysis;
import com.qiwenshare.stock.analysis.tactics.TacticsAnalysis;
import com.qiwenshare.stock.api.IEchnicalaspectService;
import com.qiwenshare.stock.domain.EchnicalaspectBean;
import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.domain.StockBean;
import com.qiwenshare.stock.domain.StockDayInfo;
import com.qiwenshare.stock.indicator.product.KDJ;
import com.qiwenshare.stock.indicator.product.MA;
import com.qiwenshare.stock.indicator.product.MACD;
import com.qiwenshare.stock.indicator.product.RSI;
import com.qiwenshare.stock.mapper.EchnicalaspectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EchnicalaspectService implements IEchnicalaspectService {
    @Resource
    EchnicalaspectMapper echnicalaspectMapper;

    @Override
    public EchnicalaspectBean getEchnicalaspectBean(String stockNum) {
        LambdaQueryWrapper<EchnicalaspectBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EchnicalaspectBean::getStockNum, stockNum);
        List<EchnicalaspectBean> list = echnicalaspectMapper.selectList(lambdaQueryWrapper);
        return list.get(0);
    }

    @Override
    public void updateEchnicalaspect(EchnicalaspectBean echnicalaspectBean) {
        LambdaUpdateWrapper<EchnicalaspectBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(EchnicalaspectBean::getStockNum, echnicalaspectBean.getStockNum());
        echnicalaspectMapper.update(echnicalaspectBean, lambdaUpdateWrapper);
    }

    @Override
    public void insertEchnicalaspect(EchnicalaspectBean echnicalaspectBean) {
        echnicalaspectMapper.insert(echnicalaspectBean);
    }

    @Override
    public EchnicalaspectBean getEchnicalaspectInfo(String stockNum, List<StockDayInfo> stockDayInfoList) {
        MA ma = new MA();
        KDJ kdj = new KDJ();
        MACD macd = new MACD();
        RSI rsi = new RSI();
        TacticsAnalysis tacticsAnalysis = new CattleCatchingAnalysis();
        StockDayInfo preStockDayInfo = stockDayInfoList.get(stockDayInfoList.size() - 2);
        StockDayInfo stockDayInfo = stockDayInfoList.get(stockDayInfoList.size() - 1);
        StockBean stockBean = new StockBean();
        stockBean.setStockNum(stockNum);
        ReplayBean replayBean = tacticsAnalysis.getOperation(stockDayInfoList, stockBean);

        boolean isGoldenCross = ma.isGoldenCross(stockDayInfoList);
        boolean isDeathCross = ma.isDeathCross(stockDayInfoList);
        boolean isTechnicalreturn = ma.isTechnicalreturn(stockDayInfoList);
        boolean isOverBought = kdj.isOverbought(stockDayInfoList);
        boolean isOverSold = kdj.isOversold(stockDayInfoList);
        boolean isKdjGoldencross = kdj.isKdjGoldencross(stockDayInfoList);
        boolean isKdjdeathcross = kdj.isKdjdeathcross(stockDayInfoList);
        boolean isShortUpwardTrend = kdj.isShortUpwardTrend(stockDayInfoList);
        boolean isShortDownwardTrend = kdj.isShortDownwardTrend(stockDayInfoList);
        boolean isBullMarket = macd.isBullMarket(preStockDayInfo, stockDayInfo);
        boolean isBearMarket = macd.isBearMarket(preStockDayInfo, stockDayInfo);
        boolean isUpTrend = macd.isUpTrend(preStockDayInfo, stockDayInfo);
        boolean isDownTrend = macd.isDownTrend(preStockDayInfo, stockDayInfo);
        int prosperityLevel = rsi.prosperityLevel(stockDayInfoList);

        String currentTime = DateUtil.getCurrentTime();
        EchnicalaspectBean echnicalaspectBean = new EchnicalaspectBean();
        echnicalaspectBean.setStockNum(stockNum);
        echnicalaspectBean.setUpdateDate(currentTime);
        echnicalaspectBean.setGoldenCross(isGoldenCross ? 1 : 0);
        echnicalaspectBean.setDeathCross(isDeathCross ? 1 : 0);
        echnicalaspectBean.setTechnicalReturn(isTechnicalreturn ? 1 : 0);
        echnicalaspectBean.setOverBought(isOverBought ? 1 : 0);
        echnicalaspectBean.setOverSold(isOverSold ? 1 : 0);
        echnicalaspectBean.setKdjGoldenCross(isKdjGoldencross ? 1 : 0);
        echnicalaspectBean.setKdjDeathCross(isKdjdeathcross ? 1 : 0);
        echnicalaspectBean.setShortUpwardTrend(isShortUpwardTrend ? 1 : 0);
        echnicalaspectBean.setShortDownwardTrend(isShortDownwardTrend ? 1 : 0);
        echnicalaspectBean.setBullMarket(isBullMarket ? 1 : 0);
        echnicalaspectBean.setBearMarket(isBearMarket ? 1 : 0);
        echnicalaspectBean.setUpTrend(isUpTrend ? 1 : 0);
        echnicalaspectBean.setDownTrend(isDownTrend ? 1 : 0);
        echnicalaspectBean.setProsperityLevel(prosperityLevel);
        echnicalaspectBean.setCattleStart(replayBean.getBought());

        return echnicalaspectBean;
    }
}
