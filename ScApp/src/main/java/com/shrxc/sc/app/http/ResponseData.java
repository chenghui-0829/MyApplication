package com.shrxc.sc.app.http;

/**
 * Created by CH on 2018/4/2.
 */

public class ResponseData {


    private String Status;
    private String Description;
    private String Data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
