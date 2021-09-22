package com.qiwenshare.stock.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiwenshare.stock.api.IReplayService;
import com.qiwenshare.stock.domain.ReplayBean;
import com.qiwenshare.stock.mapper.ReplayMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReplayService implements IReplayService {

    @Resource
    ReplayMapper replayMapper;
    @Override
    public List<ReplayBean> selectAllReplayList(long beginCount, long pageCount) {
        List<ReplayBean> replayBeanList = replayMapper.getReplayList(beginCount,pageCount);
        return replayBeanList;
    }

    @Override
    public List<ReplayBean> selectReplayList(long stockid) {
        LambdaQueryWrapper<ReplayBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ReplayBean::getStockid, stockid);
        List<ReplayBean> replayBeanList = replayMapper.selectList(lambdaQueryWrapper);
        return replayBeanList;
    }

    @Override
    public void deleteReplay(long stockid) {
        LambdaQueryWrapper<ReplayBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ReplayBean::getStockid, stockid);
        replayMapper.delete(lambdaQueryWrapper);

    }

    @Override
    public void insertReplay(List<ReplayBean> replayBeanList) {
        for (ReplayBean replay: replayBeanList) {
            replayMapper.insert(replay);
        }

    }
}
