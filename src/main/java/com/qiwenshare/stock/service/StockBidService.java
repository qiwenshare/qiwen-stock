package com.qiwenshare.stock.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qiwenshare.stock.api.IStockBidService;
import com.qiwenshare.stock.common.HttpsUtils;
import com.qiwenshare.stock.domain.StockBidBean;
import com.qiwenshare.stock.mapper.StockBidMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StockBidService implements IStockBidService {

    @Resource
    StockBidMapper stockBidMapper;


    @Override
    public void updateStockBid(StockBidBean stockBidBean) {
        LambdaUpdateWrapper<StockBidBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(StockBidBean::getStockNum, stockBidBean.getStockNum());
        stockBidMapper.update(stockBidBean, lambdaUpdateWrapper);

    }

    @Override
    public StockBidBean getStockBidBean(String stockNum) {
        LambdaQueryWrapper<StockBidBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StockBidBean::getStockNum, stockNum);
        List<StockBidBean> stockBeanList = stockBidMapper.selectList(lambdaQueryWrapper);
        if (stockBeanList.isEmpty()) {
            return null;
        }
        return stockBeanList.get(0);
    }

    @Override
    public void insertStockBid(StockBidBean stockBidBean) {
        stockBidMapper.insert(stockBidBean);
    }

    @Override
    public StockBidBean crawlBidByStockBean(String stockNum) {
        String url = "http://push2.eastmoney.com/api/qt/stock/get";
        //String param = "select=ask,bid";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        param.put("invt", "2");
        param.put("fltt", "2");
        param.put("fields", "f43,f57,f58,f169,f170,f46,f44,f51,f168,f47,f164,f163,f116,f60,f45,f52,f50,f48,f167,f117,f71,f161,f49,f530,f135,f136,f137,f138,f139,f141,f142,f144,f145,f147,f148,f140,f143,f146,f149,f55,f62,f162,f92,f173,f104,f105,f84,f85,f183,f184,f185,f186,f187,f188,f189,f190,f191,f192,f107,f111,f86,f177,f78,f110,f262,f263,f264,f267,f268,f250,f251,f252,f253,f254,f255,f256,f257,f258,f266,f269,f270,f271,f273,f274,f275,f127,f199,f128,f193,f196,f194,f195,f197,f80,f280,f281,f282,f284,f285,f286,f287,f292");
        param.put("secid", "1." + stockNum);
        String sendResult = HttpsUtils.doGetString(url, param);

        JSONObject data = new JSONObject();
        try {
            data = JSONObject.parseObject(sendResult).getJSONObject("data");
        } catch (JSONException e) {
            log.error("解析jsonb报错：" + data.toJSONString());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        StockBidBean stockBidBean = new StockBidBean();
        stockBidBean.setStockNum(stockNum);
//        stockBidBean.setStockId(stockBean.getStockId());
        stockBidBean.setSellPrice5(objectToBigDecimal(data.get("f31")));

        stockBidBean.setSellCount5(objectToInteger(data.get("f32").toString()));
        stockBidBean.setSellPrice4(objectToBigDecimal(data.get("f33")));
        stockBidBean.setSellCount4(objectToInteger(data.get("f34").toString()));
        stockBidBean.setSellPrice3(objectToBigDecimal(data.get("f35")));
        stockBidBean.setSellCount3(objectToInteger(data.get("f36").toString()));
        stockBidBean.setSellPrice2(objectToBigDecimal(data.get("f37")));
        stockBidBean.setSellCount2(objectToInteger(data.get("f38").toString()));
        stockBidBean.setSellPrice1(objectToBigDecimal(data.get("f39")));
        stockBidBean.setSellCount1(objectToInteger(data.get("f40").toString()));

        stockBidBean.setBoughtPrice1(objectToBigDecimal(data.get("f19")));
        stockBidBean.setBoughtCount1(objectToInteger(data.get("f20").toString()));
        stockBidBean.setBoughtPrice2(objectToBigDecimal(data.get("f17")));
        stockBidBean.setBoughtCount2(objectToInteger(data.get("f18").toString()));
        stockBidBean.setBoughtPrice3(objectToBigDecimal(data.get("f15")));
        stockBidBean.setBoughtCount3(objectToInteger(data.get("f16").toString()));
        stockBidBean.setBoughtPrice4(objectToBigDecimal(data.get("f13")));
        stockBidBean.setBoughtCount4(objectToInteger(data.get("f14").toString()));
        stockBidBean.setBoughtPrice5(objectToBigDecimal(data.get("f11")));
        stockBidBean.setBoughtCount5(objectToInteger(data.get("f12").toString()));


        return stockBidBean;
    }


    public Double objectToBigDecimal(Object o) {
        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return Double.valueOf(0);
        }


    }

    public Integer objectToInteger(Object o) {
        if (o == null) {
            return null;
        }
        String str = o.toString();
        if (str.equals("-")) {
            return null;
        }
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }




    }
}
