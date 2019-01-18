package com.shrxc.sc.app.bean;

import java.util.List;

/**
 * Created by CH on 2018/9/25.
 */

public class JzGroupGameEntity {

    private String time;
    private int ggfs;
    private String mcn;
    private String bs;
    private List<JzChildGameEntity> games;

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

    public int getGgfs() {
        return ggfs;
    }

    public void setGgfs(int ggfs) {
        this.ggfs = ggfs;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<JzChildGameEntity> getGames() {
        return games;
    }

    public void setGames(List<JzChildGameEntity> games) {
        this.games = games;
    }
}
