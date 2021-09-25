package com.qiwenshare.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

/**
 * 异动
 */
@Data
@Entity
@Table(name = "abnormalaction")
//        , uniqueConstraints = {
//        @UniqueConstraint(name = "dateindex", columnNames = {"date","time"})
@TableName("abnormalaction")
public class AbnormalactionBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long abnormalactionId;
    @Column
    private String stockNum;
    @Column
    private int isAbnormalaction;
    @Column(precision = 5, scale = 4)
    private double upDownRange;

    @Column
    private Date date;

    @Column
    private Time time;
    public AbnormalactionBean() {
    }
    public AbnormalactionBean(String stockNum) {
        this.stockNum = stockNum;
    }
}
