package com.shrxc.sc.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CH on 2018/10/11.
 */

public class ChildOrder implements Serializable {

    private List<ContOrder> BuyNumJson;
    private String Sheets;
    private String Tcount;
    private String Multiple;
    private String TotalMoney;
    private String PlayType;
    private String Remark;
    private String MCN;

    public String getSheets() {
        return Sheets;
    }

    public void setSheets(String sheets) {
        Sheets = sheets;
    }

    public String getTcount() {
        return Tcount;
    }

    public void setTcount(String tcount) {
        Tcount = tcount;
    }

    public String getMultiple() {
        return Multiple;
    }

    public void setMultiple(String multiple) {
        Multiple = multiple;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getPlayType() {
        return PlayType;
    }

    public void setPlayType(String playType) {
        PlayType = playType;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getMCN() {
        return MCN;
    }

    public void setMCN(String MCN) {
        this.MCN = MCN;
    }

    public List<ContOrder> getBuyNumJson() {
        return BuyNumJson;
    }

    public void setBuyNumJson(List<ContOrder> buyNumJson) {
        BuyNumJson = buyNumJson;
    }

}
