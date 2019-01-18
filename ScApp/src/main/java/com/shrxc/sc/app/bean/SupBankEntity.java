package com.shrxc.sc.app.bean;

/**
 * Created by CH on 2018/12/14.
 */

public class SupBankEntity {

    /**
     * 银行代码
     */
    private String bankid;
    /**
     * 银行标志
     */
    private String logo;
    /**
     * 银行名称
     */
    private String name;
    /**
     * 单日限额
     */
    private String xiane;
    /**
     * 银行卡id
     */
    private String id;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 银行卡号
     */
    private String num;
    /**
     * 宝付验证类型
     */
    private String type;

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getXiane() {
        return xiane;
    }

    public void setXiane(String xiane) {
        this.xiane = xiane;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
