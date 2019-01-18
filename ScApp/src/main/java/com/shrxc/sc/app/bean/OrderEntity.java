package com.shrxc.sc.app.bean;

/**
 * Created by CH on 2018/12/11.
 */

public class OrderEntity {

    private String OAID;
    private String ProTypeStr;
    private String StatusStr;
    private String MerchantName;
    private String TotalMoney;
    private String BuyNum;

    public String getBuyNum() {
        return BuyNum;
    }

    public void setBuyNum(String buyNum) {
        BuyNum = buyNum;
    }

    public String getOAID() {
        return OAID;
    }

    public void setOAID(String OAID) {
        this.OAID = OAID;
    }

    public String getProTypeStr() {
        return ProTypeStr;
    }

    public void setProTypeStr(String proTypeStr) {
        ProTypeStr = proTypeStr;
    }

    public String getStatusStr() {
        return StatusStr;
    }

    public void setStatusStr(String statusStr) {
        StatusStr = statusStr;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }
}
