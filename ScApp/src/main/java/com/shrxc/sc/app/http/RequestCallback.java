package com.shrxc.sc.app.http;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by CH on 2017/2/17.
 */

public abstract class RequestCallback {

    public void onStart() {
    }

    public void onSuccess(JSONObject result, String state, String msg, String data) {
    }

    public void onStringResult(String result) {
    }

    public void onErro(String erro) {
    }

    public void onFinish() {
    }
}
