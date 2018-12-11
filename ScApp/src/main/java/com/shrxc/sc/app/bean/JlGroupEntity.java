package com.shrxc.sc.app.bean;

import java.util.List;

/**
 * Created by CH on 2018/10/22.
 */

public class JlGroupEntity {

    private String time;
    private List<JlChildEntity> list;
    private int ggfs;
    private String mcn;
    private String bs;

    public int getGgfs() {
        return ggfs;
    }

    public void setGgfs(int ggfs) {
        this.ggfs = ggfs;
    }

    public String getMcn() {
        return mcn;
    }

    public void setMcn(String mcn) {
        this.mcn = mcn;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<JlChildEntity> getList() {
        return list;
    }

    public void setList(List<JlChildEntity> list) {
        this.list = list;
    }
}
