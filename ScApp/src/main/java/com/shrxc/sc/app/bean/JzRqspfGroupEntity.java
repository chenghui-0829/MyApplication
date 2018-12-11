package com.shrxc.sc.app.bean;

import java.util.List;

/**
 * Created by CH on 2018/9/4.
 */

public class JzRqspfGroupEntity {

    String data;
    List<JzRqspfChildEntity> childList;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<JzRqspfChildEntity> getChildList() {
        return childList;
    }

    public void setChildList(List<JzRqspfChildEntity> childList) {
        this.childList = childList;
    }
}
