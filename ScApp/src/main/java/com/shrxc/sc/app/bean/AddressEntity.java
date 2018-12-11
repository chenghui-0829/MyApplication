package com.shrxc.sc.app.bean;

import java.io.Serializable;

/**
 * Created by CH on 2018/10/11.
 */

public class AddressEntity implements Serializable {

    private String Id;
    private String Province; //省[必传]
    private String City; //市[必传]
    private String Area; //区[必传]
    private String DetailAddress; //juti[必传]
    private boolean IsDefault; //是否默认[必传]
    private String Tel; //手机号[必传]
    private String Name; //名字[必传]
    private String Door; //门牌号[必传]
    private String Tag;//标签[必传]
    private int Gender; //性别【必传】 1 男 2 女

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDoor() {
        return Door;
    }

    public void setDoor(String door) {
        Door = door;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getDetailAddress() {
        return DetailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        DetailAddress = detailAddress;
    }

    public boolean isDefault() {
        return IsDefault;
    }

    public void setDefault(boolean aDefault) {
        IsDefault = aDefault;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
