package com.qiwenshare.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "stock")
@TableName("stock")
public class StockBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long stockId;
    @Column
    private String stockNum;
    @Column
    private String stockName;
//    /**
//     * 表名，查询用，非实体属性
//     */
    @Transient
    private String stockTableName;
//    //公司代码，解析json用
//    private String COMPANY_CODE;
//    //公司简称，解析json用
//    private String COMPANY_ABBR;
//    //上市日期
//    @Column
//    private String LISTING_DATE;

    //流通股本
    @Column
    private double totalFlowShares;
    //总股本
    @Column
    private double totalShares;

    /**
     * 涨跌幅
     */
    @Column(precision = 5, scale = 4)
    private double upDownRange;

    @Column(precision = 5, scale = 4)
    private double upDownRange3;

    @Column(precision = 5, scale = 4)
    private double upDownRange5;

    /**
     * 换手率
     */
    @Column(precision = 5, scale = 4)
    private double turnOverrate;

    /**
     * 涨跌额
     */
    @Column(precision = 10, scale = 3)
    private double upDownPrices;

    @Column(precision = 10, scale = 3)
    private double open;

    @Column(precision = 10, scale = 3)
    private double close;

    @Column(precision = 10, scale = 3)
    private double high;

    @Column(precision = 10, scale = 3)
    private double low;

    @Column(precision = 10, scale = 3)
    private double preClose;

    @Column(precision = 15)
    private double volume;

    @Column(precision = 15)
    private double amount;

    /**
     * 振幅
     */
    @Column(precision = 5, scale = 4)
    private double amplitude;

    @Column(precision = 15)
    private double totalMarketValue;

    @Column(precision = 15)
    private double flowMarketValue;

    private String updateDate;
    private String listingDate;



    @Override
    public boolean equals(Object obj) {
        // 两个对象是同一个时，直接return true
        if (this == obj) {
            return true;
        }
        if (obj instanceof StockBean) {
            // 比较对象也是StockBean对象时，判断name和age是否都相同
            StockBean p = (StockBean) obj;
            return stockNum.equals(p.stockNum) && stockName.equals(p.stockName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
