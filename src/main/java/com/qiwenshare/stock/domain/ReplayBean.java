package com.qiwenshare.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "replay")
@TableName("replay")
public class ReplayBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long replayId;
    @Column
    private String stockNum;

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
    private double closePrice;
    @Column
    private int statusCode;

    private double profit;
    @Transient
    @TableField(exist = false)
    private StockBean stockBean;


}
