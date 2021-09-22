package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.ReplayBean;

import java.util.List;

public interface IReplayService {


    void insertReplay(List<ReplayBean> replayBeanList);

    void deleteReplay(long stockid);

    List<ReplayBean> selectReplayList(long stockid);

    List<ReplayBean> selectAllReplayList(long beginCount, long pageCount);
}
