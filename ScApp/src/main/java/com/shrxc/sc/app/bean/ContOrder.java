package com.shrxc.sc.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CH on 2018/10/11.
 */

public class ContOrder implements Serializable {

    private String planid;
    private String gameid;
    private String league;
    private String hometeam;
    private String guestteam;
    private String result;
    private String type;
    private List<SelectOrder> json;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<SelectOrder> getJson() {
        return json;
    }

    public void setJson(List<SelectOrder> json) {
        this.json = json;
    }
}
