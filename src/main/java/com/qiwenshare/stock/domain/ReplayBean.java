package com.qiwenshare.stock.domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "replay")
public class ReplayBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replayid;
    @Column
    private long stockid;

    @Column
    private Date date;
    @Column
    private int bought;
    @Column
    private int sold;
    @Column
    private String reason;
    @Column
    private int success;
    @Column(precision = 10, scale = 3)
    private double closeprice;
    @Column
    private int statusCode;

    private double profit;
    @Transient
    private StockBean stockBean;


    public StockBean getStockBean() {
        return stockBean;
    }

    public void setStockBean(StockBean stockBean) {
        this.stockBean = stockBean;
    }

    public long getReplayid() {
        return replayid;
    }

    public void setReplayid(long replayid) {
        this.replayid = replayid;
    }

    public long getStockid() {
        return stockid;
    }

    public void setStockid(long stockid) {
        this.stockid = stockid;
    }

    public Date getDate() {
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    public void setDate(Date date) {
        if (date == null) {
            this.date = null;
        } else {
            this.date = (Date) date.clone();
        }
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public double getCloseprice() {
        return closeprice;
    }

    public void setCloseprice(double closeprice) {
        this.closeprice = closeprice;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
