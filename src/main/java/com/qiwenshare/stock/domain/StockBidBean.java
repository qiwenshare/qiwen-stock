package com.qiwenshare.stock.domain;

import javax.persistence.*;

@Entity
@Table(name = "stockbid")
public class StockBidBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stockbidid;

    @Column
    private long stockid;
    @Column
    private String stocknum;
    @Column
    private String date;
    @Column
    private String time;
    @Column
    private double sellprice5;
    @Column
    private double sellprice4;
    @Column
    private double sellprice3;
    @Column
    private double sellprice2;
    @Column
    private double sellprice1;
    @Column
    private double boughtprice1;
    @Column
    private double boughtprice2;
    @Column
    private double boughtprice3;
    @Column
    private double boughtprice4;
    @Column
    private double boughtprice5;
    @Column
    private int sellcount5;
    @Column
    private int sellcount4;
    @Column
    private int sellcount3;
    @Column
    private int sellcount2;
    @Column
    private int sellcount1;
    @Column
    private int boughtcount1;
    @Column
    private int boughtcount2;
    @Column
    private int boughtcount3;
    @Column
    private int boughtcount4;
    @Column
    private int boughtcount5;


    public StockBidBean() {
    }

    public StockBidBean(long stockid) {
        this.stockid = stockid;
    }

    public int getStockbidid() {
        return stockbidid;
    }

    public void setStockbidid(int stockbidid) {
        this.stockbidid = stockbidid;
    }

    public String getStocknum() {
        return stocknum;
    }

    public void setStocknum(String stocknum) {
        this.stocknum = stocknum;
    }

    public long getStockid() {
        return stockid;
    }

    public void setStockid(long stockid) {
        this.stockid = stockid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSellprice5() {
        return sellprice5;
    }

    public void setSellprice5(double sellprice5) {
        this.sellprice5 = sellprice5;
    }

    public double getSellprice4() {
        return sellprice4;
    }

    public void setSellprice4(double sellprice4) {
        this.sellprice4 = sellprice4;
    }

    public double getSellprice3() {
        return sellprice3;
    }

    public void setSellprice3(double sellprice3) {
        this.sellprice3 = sellprice3;
    }

    public double getSellprice2() {
        return sellprice2;
    }

    public void setSellprice2(double sellprice2) {
        this.sellprice2 = sellprice2;
    }

    public double getSellprice1() {
        return sellprice1;
    }

    public void setSellprice1(double sellprice1) {
        this.sellprice1 = sellprice1;
    }

    public double getBoughtprice1() {
        return boughtprice1;
    }

    public void setBoughtprice1(double boughtprice1) {
        this.boughtprice1 = boughtprice1;
    }

    public double getBoughtprice2() {
        return boughtprice2;
    }

    public void setBoughtprice2(double boughtprice2) {
        this.boughtprice2 = boughtprice2;
    }

    public double getBoughtprice3() {
        return boughtprice3;
    }

    public void setBoughtprice3(double boughtprice3) {
        this.boughtprice3 = boughtprice3;
    }

    public double getBoughtprice4() {
        return boughtprice4;
    }

    public void setBoughtprice4(double boughtprice4) {
        this.boughtprice4 = boughtprice4;
    }

    public double getBoughtprice5() {
        return boughtprice5;
    }

    public void setBoughtprice5(double boughtprice5) {
        this.boughtprice5 = boughtprice5;
    }

    public int getSellcount5() {
        return sellcount5;
    }

    public void setSellcount5(int sellcount5) {
        this.sellcount5 = sellcount5;
    }

    public int getSellcount4() {
        return sellcount4;
    }

    public void setSellcount4(int sellcount4) {
        this.sellcount4 = sellcount4;
    }

    public int getSellcount3() {
        return sellcount3;
    }

    public void setSellcount3(int sellcount3) {
        this.sellcount3 = sellcount3;
    }

    public int getSellcount2() {
        return sellcount2;
    }

    public void setSellcount2(int sellcount2) {
        this.sellcount2 = sellcount2;
    }

    public int getSellcount1() {
        return sellcount1;
    }

    public void setSellcount1(int sellcount1) {
        this.sellcount1 = sellcount1;
    }

    public int getBoughtcount1() {
        return boughtcount1;
    }

    public void setBoughtcount1(int boughtcount1) {
        this.boughtcount1 = boughtcount1;
    }

    public int getBoughtcount2() {
        return boughtcount2;
    }

    public void setBoughtcount2(int boughtcount2) {
        this.boughtcount2 = boughtcount2;
    }

    public int getBoughtcount3() {
        return boughtcount3;
    }

    public void setBoughtcount3(int boughtcount3) {
        this.boughtcount3 = boughtcount3;
    }

    public int getBoughtcount4() {
        return boughtcount4;
    }

    public void setBoughtcount4(int boughtcount4) {
        this.boughtcount4 = boughtcount4;
    }

    public int getBoughtcount5() {
        return boughtcount5;
    }

    public void setBoughtcount5(int boughtcount5) {
        this.boughtcount5 = boughtcount5;
    }
}
