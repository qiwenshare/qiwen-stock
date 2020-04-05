package com.qiwenshare.stock.domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "stock")
public class StockBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stockid;
    @Column
    private String stocknum;
    @Column
    private String stockname;
    /**
     * 表名，查询用，非实体属性
     */
    private String stockTableName;
    //公司代码，解析json用
    private String COMPANY_CODE;
    //公司简称，解析json用
    private String COMPANY_ABBR;
    //上市日期
    @Column
    private String LISTING_DATE;

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
    private double updownrange;

    @Column(precision = 5, scale = 4)
    private double updownrange3;

    @Column(precision = 5, scale = 4)
    private double updownrange5;

    /**
     * 换手率
     */
    @Column(precision = 5, scale = 4)
    private double turnoverrate;

    /**
     * 涨跌额
     */
    @Column(precision = 10, scale = 3)
    private double updownprices;

    @Column(precision = 10, scale = 3)
    private double open;

    @Column(precision = 10, scale = 3)
    private double close;

    @Column(precision = 10, scale = 3)
    private double high;

    @Column(precision = 10, scale = 3)
    private double low;

    @Column(precision = 10, scale = 3)
    private double preclose;

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
    private double totalmarketvalue;

    @Column(precision = 15)
    private double flowmarketvalue;

    private Date updateDate;


    public StockBean() {
    }

    public StockBean(int stockid) {
        this.stockid = stockid;
    }

    public long getStockid() {
        return stockid;
    }

    public void setStockid(long stockid) {
        this.stockid = stockid;
    }

    public String getStocknum() {
        return stocknum;
    }

    public void setStocknum(String stocknum) {
        this.stocknum = stocknum;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public String getStockTableName() {
        return stockTableName;
    }

    public void setStockTableName(String stockTableName) {
        this.stockTableName = stockTableName;
    }

    @Override
    public boolean equals(Object obj) {
        // 两个对象是同一个时，直接return true
        if (this == obj) {
            return true;
        }
        if (obj instanceof StockBean) {
            // 比较对象也是StockBean对象时，判断name和age是否都相同
            StockBean p = (StockBean) obj;
            return stocknum.equals(p.stocknum) && stockname.equals(p.stockname);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getCOMPANY_ABBR() {
        return COMPANY_ABBR;
    }

    public void setCOMPANY_ABBR(String cOMPANY_ABBR) {
        COMPANY_ABBR = cOMPANY_ABBR;
    }

    public String getCOMPANY_CODE() {
        return COMPANY_CODE;
    }

    public void setCOMPANY_CODE(String cOMPANY_CODE) {
        COMPANY_CODE = cOMPANY_CODE;
    }

    public String getLISTING_DATE() {
        return LISTING_DATE;
    }

    public void setLISTING_DATE(String lISTING_DATE) {
        LISTING_DATE = lISTING_DATE;
    }

    public double getTotalFlowShares() {
        return totalFlowShares;
    }

    public void setTotalFlowShares(double totalFlowShares) {
        this.totalFlowShares = totalFlowShares;
    }

    public double getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(double totalShares) {
        this.totalShares = totalShares;
    }

    public double getUpdownrange() {
        return updownrange;
    }

    public void setUpdownrange(double updownrange) {
        this.updownrange = updownrange;
    }

    public double getUpdownrange3() {
        return updownrange3;
    }

    public void setUpdownrange3(double updownrange3) {
        this.updownrange3 = updownrange3;
    }

    public double getUpdownrange5() {
        return updownrange5;
    }

    public void setUpdownrange5(double updownrange5) {
        this.updownrange5 = updownrange5;
    }

    public double getTurnoverrate() {
        return turnoverrate;
    }

    public void setTurnoverrate(double turnoverrate) {
        this.turnoverrate = turnoverrate;
    }

    public double getUpdownprices() {
        return updownprices;
    }

    public void setUpdownprices(double updownprices) {
        this.updownprices = updownprices;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getPreclose() {
        return preclose;
    }

    public void setPreclose(double preclose) {
        this.preclose = preclose;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getTotalmarketvalue() {
        return totalmarketvalue;
    }

    public void setTotalmarketvalue(double totalmarketvalue) {
        this.totalmarketvalue = totalmarketvalue;
    }

    public double getFlowmarketvalue() {
        return flowmarketvalue;
    }

    public void setFlowmarketvalue(double flowmarketvalue) {
        this.flowmarketvalue = flowmarketvalue;
    }

    public Date getUpdateDate() {
        if (updateDate == null) {
            return null;
        }
        return (Date) updateDate.clone();
    }

    public void setUpdateDate(Date updateDate) {
        if (updateDate == null) {
            this.updateDate = null;
        } else {
            this.updateDate = (Date) updateDate.clone();
        }

    }
}
