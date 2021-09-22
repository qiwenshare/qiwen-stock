package com.qiwenshare.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

/**
 * 异动
 */
@Entity
@Table(name = "abnormalaction")
//        , uniqueConstraints = {
//        @UniqueConstraint(name = "dateindex", columnNames = {"date","time"})
@TableName("abnormalaction")
public class AbnormalactionBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private long abnormalactionid;
    @Column
    private long stockid;
    @Column
    private String stocknum;
    @Column
    private int isabnormalaction;
    @Column(precision = 5, scale = 4)
    private double updownrange;

    @Column
    private Date date;

    @Column
    private Time time;

    public AbnormalactionBean() {

    }

    public AbnormalactionBean(long stockid) {
        this.stockid = stockid;
    }

    public long getAbnormalactionid() {
        return abnormalactionid;
    }

    public void setAbnormalactionid(long abnormalactionid) {
        this.abnormalactionid = abnormalactionid;
    }

    public long getStockid() {
        return stockid;
    }

    public void setStockid(long stockid) {
        this.stockid = stockid;
    }

    public int getIsabnormalaction() {
        return isabnormalaction;
    }

    public void setIsabnormalaction(int isabnormalaction) {
        this.isabnormalaction = isabnormalaction;
    }


    public String getStocknum() {
        return stocknum;
    }

    public void setStocknum(String stocknum) {
        this.stocknum = stocknum;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public double getUpdownrange() {
        return updownrange;
    }

    public void setUpdownrange(double updownrange) {
        this.updownrange = updownrange;
    }
}
