package com.qiwenshare.stock.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qiwenshare.stock.api.IAbnormalaActionService;
import com.qiwenshare.stock.domain.AbnormalactionBean;
import com.qiwenshare.stock.mapper.AbnormalaActionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AbnormalaActionService implements IAbnormalaActionService {
    @Resource
    AbnormalaActionMapper abnormalaActionMapper;


    @Override
    public AbnormalactionBean getAbnormalactionBean(String stockNum) {
        LambdaQueryWrapper<AbnormalactionBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AbnormalactionBean::getStockNum, stockNum);
        List<AbnormalactionBean> list =  abnormalaActionMapper.selectList(lambdaQueryWrapper);
        return list.get(0);
    }

    @Override
    public void insertAbnormalaAction(AbnormalactionBean abnormalactionBean) {

        abnormalaActionMapper.insert(abnormalactionBean);
    }

    @Override
    public void updateAbnormalaAction(AbnormalactionBean abnormalactionBean) {
        LambdaUpdateWrapper<AbnormalactionBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AbnormalactionBean::getStockNum, abnormalactionBean.getStockNum());
        abnormalaActionMapper.update(abnormalactionBean, lambdaUpdateWrapper);
    }
}
