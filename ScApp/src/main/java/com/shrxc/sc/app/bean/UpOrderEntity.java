package com.shrxc.sc.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CH on 2018/10/10.
 */

public class UpOrderEntity implements Serializable {

    private List<ChildOrder> BasketBallBuyNumJson;
    private List<ChildOrder> FootBallBuyNumJson;
    private String TotalMoney;
    private String Remark;
    private String Aid;
    private String WifiId;

    public List<ChildOrder> getBasketBallBuyNumJson() {
        return BasketBallBuyNumJson;
    }

    public void setBasketBallBuyNumJson(List<ChildOrder> basketBallBuyNumJson) {
        BasketBallBuyNumJson = basketBallBuyNumJson;
    }

    public List<ChildOrder> getFootBallBuyNumJson() {
        return FootBallBuyNumJson;
    }

    public void setFootBallBuyNumJson(List<ChildOrder> footBallBuyNumJson) {
        FootBallBuyNumJson = footBallBuyNumJson;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getAid() {
        return Aid;
    }

    public void setAid(String aid) {
        Aid = aid;
    }

    public String getWifiId() {
        return WifiId;
    }

    public void setWifiId(String wifiId) {
        WifiId = wifiId;
    }
}



