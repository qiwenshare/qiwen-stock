package com.qiwenshare.stock.api;

import com.qiwenshare.stock.domain.AbnormalactionBean;

public interface IAbnormalaActionService {

    void insertAbnormalaAction(AbnormalactionBean abnormalactionBean);

    void updateAbnormalaAction(AbnormalactionBean abnormalactionBean);

    AbnormalactionBean getAbnormalactionBean(String stockNum);
}
