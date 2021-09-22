package com.qiwenshare.stock.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.*;

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
    private int id;
    @Column
    private long stockid;
    @Column
    private String updatedate;
    //ma
    @Column
    private int goldencross;
    @Column
    private int deathcross;
    /**
     * 技术回档
     */
    @Column
    private int technicalreturn;
    //kdj
    @Column
    private int overbought;
    @Column
    private int oversold;
    @Column
    private int kdjgoldencross;
    @Column
    private int kdjdeathcross;
    @Column
    private int shortupwardtrend;
    @Column
    private int shortdownwardtrend;

    //macd
    @Column
    private int bullMarket;
    @Column
    private int bearMarket;
    @Column
    private int uptrend;
    @Column
    private int downtrend;


    //rsi
    @Column
    private int prosperitylevel;
    @Column
    private int cattlestart;


    public EchnicalaspectBean() {

    }

    public EchnicalaspectBean(long stockid) {
        this.stockid = stockid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStockid() {
        return stockid;
    }

    public void setStockid(long stockid) {
        this.stockid = stockid;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public int getGoldencross() {
        return goldencross;
    }

    public void setGoldencross(int goldencross) {
        this.goldencross = goldencross;
    }

    public int getDeathcross() {
        return deathcross;
    }

    public void setDeathcross(int deathcross) {
        this.deathcross = deathcross;
    }

    public int getTechnicalreturn() {
        return technicalreturn;
    }

    public void setTechnicalreturn(int technicalreturn) {
        this.technicalreturn = technicalreturn;
    }

    public int getCattlestart() {
        return cattlestart;
    }

    public void setCattlestart(int cattlestart) {
        this.cattlestart = cattlestart;
    }

    public int getOverbought() {
        return overbought;
    }

    public void setOverbought(int overbought) {
        this.overbought = overbought;
    }

    public int getOversold() {
        return oversold;
    }

    public void setOversold(int oversold) {
        this.oversold = oversold;
    }

    public int getKdjgoldencross() {
        return kdjgoldencross;
    }

    public void setKdjgoldencross(int kdjgoldencross) {
        this.kdjgoldencross = kdjgoldencross;
    }

    public int getKdjdeathcross() {
        return kdjdeathcross;
    }

    public void setKdjdeathcross(int kdjdeathcross) {
        this.kdjdeathcross = kdjdeathcross;
    }

    public int getShortupwardtrend() {
        return shortupwardtrend;
    }

    public void setShortupwardtrend(int shortupwardtrend) {
        this.shortupwardtrend = shortupwardtrend;
    }

    public int getShortdownwardtrend() {
        return shortdownwardtrend;
    }

    public void setShortdownwardtrend(int shortdownwardtrend) {
        this.shortdownwardtrend = shortdownwardtrend;
    }

    public int getBullMarket() {
        return bullMarket;
    }

    public void setBullMarket(int bullMarket) {
        this.bullMarket = bullMarket;
    }

    public int getBearMarket() {
        return bearMarket;
    }

    public void setBearMarket(int bearMarket) {
        this.bearMarket = bearMarket;
    }

    public int getUptrend() {
        return uptrend;
    }

    public void setUptrend(int uptrend) {
        this.uptrend = uptrend;
    }

    public int getDowntrend() {
        return downtrend;
    }

    public void setDowntrend(int downtrend) {
        this.downtrend = downtrend;
    }


    public int getProsperitylevel() {
        return prosperitylevel;
    }

    public void setProsperitylevel(int prosperitylevel) {
        this.prosperitylevel = prosperitylevel;
    }
}
