package com.qiwenshare.stock.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "echnicalaspect")
@TableName("echnicalaspect")
public class EchnicalaspectBean {
    /**
     * 序列id
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long echnicalaspectId;
    @Column
    private String stockNum;
    @Column
    private String updateDate;
    //ma
    @Column
    private int goldenCross;
    @Column
    private int deathCross;
    /**
     * 技术回档
     */
    @Column
    private int technicalReturn;
    //kdj
    @Column
    private int overBought;
    @Column
    private int overSold;
    @Column
    private int kdjGoldenCross;
    @Column
    private int kdjDeathCross;
    @Column
    private int shortUpwardTrend;
    @Column
    private int shortDownwardTrend;

    //macd
    @Column
    private int bullMarket;
    @Column
    private int bearMarket;
    @Column
    private int upTrend;
    @Column
    private int downTrend;


    //rsi
    @Column
    private int prosperityLevel;
    @Column
    private int cattleStart;

    public EchnicalaspectBean() {
    }
    public EchnicalaspectBean(String stockNum) {
        this.stockNum = stockNum;
    }
}
