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
    public EchnicalaspectBean getEchnicalaspectBean(long stockid) {
        LambdaQueryWrapper<EchnicalaspectBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EchnicalaspectBean::getStockid, stockid);
        List<EchnicalaspectBean> list = echnicalaspectMapper.selectList(lambdaQueryWrapper);
        return list.get(0);
    }

    @Override
    public void updateEchnicalaspect(EchnicalaspectBean echnicalaspectBean) {
        LambdaUpdateWrapper<EchnicalaspectBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(EchnicalaspectBean::getStockid, echnicalaspectBean.getStockid());
        echnicalaspectMapper.update(echnicalaspectBean, lambdaUpdateWrapper);
    }

    @Override
    public void insertEchnicalaspect(EchnicalaspectBean echnicalaspectBean) {
        echnicalaspectMapper.insert(echnicalaspectBean);
    }

    @Override
    public EchnicalaspectBean getEchnicalaspectInfo(List<StockDayInfo> stockDayInfoList, StockBean stockBean) {
        MA ma = new MA();
        KDJ kdj = new KDJ();
        MACD macd = new MACD();
        RSI rsi = new RSI();
        TacticsAnalysis tacticsAnalysis = new CattleCatchingAnalysis();
        StockDayInfo preStockDayInfo = stockDayInfoList.get(stockDayInfoList.size() - 2);
        StockDayInfo stockDayInfo = stockDayInfoList.get(stockDayInfoList.size() - 1);
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
        echnicalaspectBean.setStockid(stockBean.getStockid());
        echnicalaspectBean.setUpdatedate(currentTime);
        echnicalaspectBean.setGoldencross(isGoldenCross ? 1 : 0);
        echnicalaspectBean.setDeathcross(isDeathCross ? 1 : 0);
        echnicalaspectBean.setTechnicalreturn(isTechnicalreturn ? 1 : 0);
        echnicalaspectBean.setOverbought(isOverBought ? 1 : 0);
        echnicalaspectBean.setOversold(isOverSold ? 1 : 0);
        echnicalaspectBean.setKdjgoldencross(isKdjGoldencross ? 1 : 0);
        echnicalaspectBean.setKdjdeathcross(isKdjdeathcross ? 1 : 0);
        echnicalaspectBean.setShortupwardtrend(isShortUpwardTrend ? 1 : 0);
        echnicalaspectBean.setShortdownwardtrend(isShortDownwardTrend ? 1 : 0);
        echnicalaspectBean.setBullMarket(isBullMarket ? 1 : 0);
        echnicalaspectBean.setBearMarket(isBearMarket ? 1 : 0);
        echnicalaspectBean.setUptrend(isUpTrend ? 1 : 0);
        echnicalaspectBean.setDowntrend(isDownTrend ? 1 : 0);
        echnicalaspectBean.setProsperitylevel(prosperityLevel);
        echnicalaspectBean.setCattlestart(replayBean.getBought());

        return echnicalaspectBean;
    }
}
