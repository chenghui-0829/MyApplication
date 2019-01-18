package com.shrxc.sc.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CH on 2018/9/25.
 */

public class JzChildGameEntity implements Serializable {

    private String planid;
    private String endtime;
    private String starttime;
    private String league;
    private String gameid;
    private String hometeam;
    private String urlid;
    private String guestteam;
    private String spl;
    private String ppl;
    private String fpl;
    private String rspl;
    private String rppl;
    private String rfpl;
    private String IsSPF;
    private String IsRSPF;
    private String rangqiu;
    private JzTzTypeEntity tzTypeEntity;
    private List<Integer> selectedList;

    public String getIsSPF() {
        return IsSPF;
    }

    public void setIsSPF(String isSPF) {
        IsSPF = isSPF;
    }

    public String getIsRSPF() {
        return IsRSPF;
    }

    public void setIsRSPF(String isRSPF) {
        IsRSPF = isRSPF;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getUrlid() {
        return urlid;
    }

    public void setUrlid(String urlid) {
        this.urlid = urlid;
    }

    public String getPlanid() {
        return planid;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
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

    public String getSpl() {
        return spl;
    }

    public void setSpl(String spl) {
        this.spl = spl;
    }

    public String getPpl() {
        return ppl;
    }

    public void setPpl(String ppl) {
        this.ppl = ppl;
    }

    public String getFpl() {
        return fpl;
    }

    public void setFpl(String fpl) {
        this.fpl = fpl;
    }

    public String getRspl() {
        return rspl;
    }

    public void setRspl(String rspl) {
        this.rspl = rspl;
    }

    public String getRppl() {
        return rppl;
    }

    public void setRppl(String rppl) {
        this.rppl = rppl;
    }

    public String getRfpl() {
        return rfpl;
    }

    public void setRfpl(String rfpl) {
        this.rfpl = rfpl;
    }

    public String getRangqiu() {
        return rangqiu;
    }

    public void setRangqiu(String rangqiu) {
        this.rangqiu = rangqiu;
    }

    public JzTzTypeEntity getTzTypeEntity() {
        return tzTypeEntity;
    }

    public void setTzTypeEntity(JzTzTypeEntity tzTypeEntity) {
        this.tzTypeEntity = tzTypeEntity;
    }

    public List<Integer> getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(List<Integer> selectedList) {
        this.selectedList = selectedList;
    }
}
