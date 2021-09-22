package com.qiwenshare.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiwenshare.stock.domain.ReplayBean;

import java.util.List;

public interface ReplayMapper extends BaseMapper<ReplayBean> {

    List<ReplayBean> getReplayList(long beginCount, long pageCount);
}
