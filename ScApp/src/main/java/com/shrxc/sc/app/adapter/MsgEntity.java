package com.shrxc.sc.app.adapter;

import java.io.Serializable;

/**
 * Created by CH on 2018/11/28.
 */

public class MsgEntity implements Serializable {

    private String Title;
    private String Content;
    private String Statu;
    private String Id;
    private String Createtime;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
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
