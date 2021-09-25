package com.qiwenshare.stock.indicator.product;

import com.qiwenshare.common.util.DateUtil;
import com.qiwenshare.stock.domain.AbnormalactionBean;
import com.qiwenshare.stock.domain.StockTimeInfo;

import java.text.ParseException;
import java.util.List;

public class Abnormalaction {
    public AbnormalactionBean getAbnormalaction(List<StockTimeInfo> stockTimeInfoList) {
        AbnormalactionBean abnormalactionBean = new AbnormalactionBean();
        double heightUpdownRange = -900000;
        double lowUpdownRange = 900000;
        int heightTime = 0;
        int lowTime = 0;
        for (int i = 0; i < stockTimeInfoList.size() - 10; i++) {
            heightUpdownRange = -900000;
            lowUpdownRange = 900000;
            int startTime = Integer.parseInt(stockTimeInfoList.get(i).getTime());
            for (int j = i; j < i + 10; j++) {

                if (stockTimeInfoList.get(j).getUpDownRange() > heightUpdownRange) {
                    heightUpdownRange = stockTimeInfoList.get(j).getUpDownRange();
                    heightTime = Integer.parseInt(stockTimeInfoList.get(j).getTime());

                }
                if (stockTimeInfoList.get(j).getUpDownRange() < lowUpdownRange) {
                    lowUpdownRange = stockTimeInfoList.get(j).getUpDownRange();
                    lowTime = Integer.parseInt(stockTimeInfoList.get(j).getTime());
                }
            }
            if (heightUpdownRange - lowUpdownRange > 0.02
                    && heightTime > lowTime
                    && startTime > 100000) {
                abnormalactionBean.setIsAbnormalaction(1);
                abnormalactionBean.setUpDownRange(heightUpdownRange - lowUpdownRange);
                try {
                    abnormalactionBean.setDate(new java.sql.Date(DateUtil.getDateByFormatString( stockTimeInfoList.get(i).getDate(),"yyyyMMdd").getTime()));
                    String time = stockTimeInfoList.get(i).getTime();
                    int hour = Integer.parseInt(time.substring(0, 2));
                    int minute = Integer.parseInt(time.substring(2, 4));
                    int second = Integer.parseInt(time.substring(4, 6));
                    abnormalactionBean.setTime(new java.sql.Time(hour, minute, second));
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return abnormalactionBean;
            }

        }
        abnormalactionBean.setIsAbnormalaction(0);
        return abnormalactionBean;
    }
}
