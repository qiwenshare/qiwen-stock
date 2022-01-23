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
    private Double sellPrice5;
    @Column
    private Double sellPrice4;
    @Column
    private Double sellPrice3;
    @Column
    private Double sellPrice2;
    @Column
    private Double sellPrice1;
    @Column
    private Double boughtPrice1;
    @Column
    private Double boughtPrice2;
    @Column
    private Double boughtPrice3;
    @Column
    private Double boughtPrice4;
    @Column
    private Double boughtPrice5;
    @Column
    private Integer sellCount5;
    @Column
    private Integer sellCount4;
    @Column
    private Integer sellCount3;
    @Column
    private Integer sellCount2;
    @Column
    private Integer sellCount1;
    @Column
    private Integer boughtCount1;
    @Column
    private Integer boughtCount2;
    @Column
    private Integer boughtCount3;
    @Column
    private Integer boughtCount4;
    @Column
    private Integer boughtCount5;

    public StockBidBean() {
    }

    public StockBidBean(String stockNum) {
        this.stockNum = stockNum;
    }
}
