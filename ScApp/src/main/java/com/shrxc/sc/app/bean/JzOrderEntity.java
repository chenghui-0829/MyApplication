package com.shrxc.sc.app.bean;

import java.util.List;

/**
 * Created by CH on 2018/9/27.
 */

public class JzOrderEntity {

    private String planid;
    private String gameid;
    private String league;
    private String hometeam;
    private String guestteam;
    private List<String> selects;
    private String type;

    public List<String> getSelects() {
        return selects;
    }

    public void setSelects(List<String> selects) {
        this.selects = selects;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getHometeam() {
        return hometeam;
    }

    public void setHometeam(String hometeam) {
        this.hometeam = hometeam;
    }

    public String getGuestteam() {
        return guestteam;
    }

    public void setGuestteam(String guestteam) {
        this.guestteam = guestteam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
