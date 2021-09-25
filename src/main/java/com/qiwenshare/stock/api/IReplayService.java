package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.ReplayBean;

import java.util.List;

public interface IReplayService {


    void insertReplay(List<ReplayBean> replayBeanList);

    void deleteReplay(String stockNum);

    List<ReplayBean> selectReplayList(String stockNum);

    List<ReplayBean> selectAllReplayList(long beginCount, long pageCount);
}
