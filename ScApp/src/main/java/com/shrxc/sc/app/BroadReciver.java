package com.shrxc.sc.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shrxc.sc.app.mine.MsgActivity;

import cn.jpush.android.api.JPushInterface;

public class BroadReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context arg0, Intent arg1) {

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(arg1.getAction())) {
            Intent intent = new Intent(arg0, MsgActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            arg0.startActivity(intent);
        }
    }

}
