package com.qiwenshare.stock.domain;

import javax.persistence.*;

@Entity
@Table(name = "optional")
public class StockOptionalBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int optionalid;
    private int userid;
    private int stockid;

    public int getOptionalid() {
        return optionalid;
    }

    public void setOptionalid(int optionalid) {
        this.optionalid = optionalid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getStockid() {
        return stockid;
    }

    public void setStockid(int stockid) {
        this.stockid = stockid;
    }
}
