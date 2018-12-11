package com.shrxc.sc.app.http;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.shrxc.sc.app.R;

/**
 * Created by CH on 2017/2/17.
 */

public class RequestLoadDialog extends RequestCallback {

    private Dialog loadDialog;

    private void initDialog(Context context) {
        loadDialog = new Dialog(context, R.style.dialog);
        loadDialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(context).inflate(
                R.layout.app_loading_dialog, null);
        loadDialog.setContentView(view);
    }

    public RequestLoadDialog(Context context) {
        if (context != null) {
            initDialog(context);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //网络请求前显示对话框
        if (loadDialog != null && !loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        //网络请求结束后关闭对话框
        if (loadDialog != null && loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
    }
}
