package com.qiwenshare.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "stockbid")
@TableName("stockbid")
public class StockBidBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long stockBidId;

    @Column
    private String stockNum;
    @Column
    private String date;
    @Column
    private String time;
    @Column
    private double sellPrice5;
    @Column
    private double sellPrice4;
    @Column
    private double sellPrice3;
    @Column
    private double sellPrice2;
    @Column
    private double sellPrice1;
    @Column
    private double boughtPrice1;
    @Column
    private double boughtPrice2;
    @Column
    private double boughtPrice3;
    @Column
    private double boughtPrice4;
    @Column
    private double boughtPrice5;
    @Column
    private int sellCount5;
    @Column
    private int sellCount4;
    @Column
    private int sellCount3;
    @Column
    private int sellCount2;
    @Column
    private int sellCount1;
    @Column
    private int boughtCount1;
    @Column
    private int boughtCount2;
    @Column
    private int boughtCount3;
    @Column
    private int boughtCount4;
    @Column
    private int boughtCount5;

    public StockBidBean() {
    }

    public StockBidBean(String stockNum) {
        this.stockNum = stockNum;
    }
}
