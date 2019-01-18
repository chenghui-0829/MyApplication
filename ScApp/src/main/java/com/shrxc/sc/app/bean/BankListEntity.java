package com.shrxc.sc.app.bean;

import java.io.Serializable;

/**
 * Created by CH on 2018/12/17.
 */

public class BankListEntity implements Serializable {

    private String UserId;
    private String BankNum;
    private String BankBelong;
    private String BankId;
    private String BankTel;
    private String BankLogo;
    private String IsDefault;
    private String IsRemove;
    private String Remark;
    private String Id;
    private String Createtime;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getBankNum() {
        return BankNum;
    }

    public void setBankNum(String bankNum) {
        BankNum = bankNum;
    }

    public String getBankBelong() {
        return BankBelong;
    }

    public void setBankBelong(String bankBelong) {
        BankBelong = bankBelong;
    }

    public String getBankId() {
        return BankId;
    }

    public void setBankId(String bankId) {
        BankId = bankId;
    }

    public String getBankTel() {
        return BankTel;
    }

    public void setBankTel(String bankTel) {
        BankTel = bankTel;
    }

    public String getBankLogo() {
        return BankLogo;
    }

    public void setBankLogo(String bankLogo) {
        BankLogo = bankLogo;
    }

    public String getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(String isDefault) {
        IsDefault = isDefault;
    }

    public String getIsRemove() {
        return IsRemove;
    }

    public void setIsRemove(String isRemove) {
        IsRemove = isRemove;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String createtime) {
        Createtime = createtime;
    }
}
